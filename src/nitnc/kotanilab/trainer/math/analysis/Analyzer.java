package nitnc.kotanilab.trainer.math.analysis;

import nitnc.kotanilab.trainer.gl.chart.*;
import nitnc.kotanilab.trainer.gl.chart.plot.LinePlot;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.gl.pane.StackPane;
import nitnc.kotanilab.trainer.math.WaveBuffer;
import nitnc.kotanilab.trainer.math.Unit;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Analyzer {
    protected Pane masterPane;
    protected WaveBuffer source;
    protected List<Pane> panes = new ArrayList<>();
    protected String borderStyle;
    protected String title;
    protected Map<String, GraphContext> graphContextMap = new LinkedHashMap<>();
    protected boolean pause = false;

    protected Analyzer(Pane masterPane, String title) {
        this.masterPane = masterPane;
        this.title = title;
        StringBuilder colorCode = new StringBuilder("#");
        for (int i = 0; i < 6; i++) {
            String tmp = Integer.toHexString((int) Math.round(Math.random() * 0xF)).toUpperCase();
            colorCode.append(tmp);
        }
        borderStyle = "border:solid 3px " + colorCode + ";";
    }

    public abstract void start(double fs, int n);

    public void stop() {
        masterPane.getChildren().removeAll(panes);
    }

    public Analyzer setVisible(Map<String, Boolean> visible) {
        visible.keySet().forEach(key -> graphContextMap.get(key).setVisible(visible.get(key)));
        return this;
    }

    public abstract void execute();

    public void setSource(WaveBuffer source) {
        this.source = source;
    }

    public Pane createWrapperPane(int size) {
        int width = 25 * size;
        Pane ret = new StackPane("height:100%;margin-x:0;margin-y:0;");
        ret.getStyle().put("width", width + "%");
        ret.getStyle().put(borderStyle);
        return ret;
    }

    public void addGraphContext(String title, LinePlot graph) {
        Chart chart = new Chart(title, graph);
        graphContextMap.put(title, new GraphContext(graph, chart, createWrapperPane(1), false));
    }

    protected List<Pane> getGraphWrappers() {
        return graphContextMap.values().stream().map(GraphContext::getWrapper).collect(Collectors.toList());
    }

    public List<Chart> getCharts() {
        return graphContextMap.values().stream().map(GraphContext::getChart).collect(Collectors.toList());
    }

    protected List<LinePlot> getGraphs() {
        return graphContextMap.values().stream().map(GraphContext::getPlot).collect(Collectors.toList());
    }

    protected void clearVectorList(LinePlot linePlot) {
        linePlot.getKeys().forEach(key -> linePlot.getVectorList(key).clear());
    }

    protected double getTime() {
        return System.currentTimeMillis() / 1000.0;
    }

    protected double previousX = 0.0;

    protected boolean isPassedInterval(double interval) {
        return getTime() - previousX > interval;
    }

    protected void updatePreviousTime() {
        previousX = getTime();
    }

    public void setPause(boolean pause) {
        this.pause = pause;
        graphContextMap.values().forEach(graphContext -> graphContext.setPause(pause));
    }

    public void inversePause() {
        this.pause = !this.pause;
        graphContextMap.values().forEach(GraphContext::inversePause);
    }

    public void shot(String filename) {

    }

    private static Color[] lineColors = {
            Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW.darker(), Color.MAGENTA.darker(), Color.CYAN.darker()
    };

    private static Color getLineColor(int i) {
        return lineColors[i % lineColors.length];
    }

    private static void addLines(LinePlot linePlot, String... lines) {
        int i = 0;
        for (String lineName : lines) {
            linePlot.addLine(lineName, getLineColor(i++), 1.0);
        }
    }

    protected static LinePlot createGraph(Axis xAxis, Axis yAxis, String... lines) {
        LinePlot ret;
        ret = new LinePlot(xAxis, yAxis);
        addLines(ret, lines);
        return ret;
    }

    protected static LinePlot createWaveGraph(double xMax, Unit yUnit, double yMin, double yMax, String... lines) {
        Axis xAxis = new Axis("Time[sec]", 0.0, xMax, xMax / 10.0);
        xAxis.setReverse(true);
        return createGraph(xAxis, new Axis(yUnit.toString(), yMin, yMax, (yMax - yMin) / 10.0), lines);
    }

    protected static LinePlot createWaveGraph(double xMax, Unit yUnit, double yRange, String... lines) {
        Axis xAxis = new Axis("Time[sec]", 0.0, xMax, xMax / 10.0);
        xAxis.setReverse(true);
        return createGraph(xAxis, new Axis(yUnit.toString(), -yRange, yRange, yRange / 5.0), lines);
    }

    protected static LinePlot createTimeSeriesGraph(double xMax, Axis yAxis, String... lines) {
        Axis xAxis = new Axis("Time[sec]", 0.0, xMax, xMax / 10.0);
        // xAxis.setReverse(true);
        return createGraph(xAxis, yAxis, lines);
    }

    protected static LinePlot createSpectrumGraph(double xMin, double xMax, Axis yAxis, String... lines) {
        return createGraph(
                new LogAxis("Frequency[Hz]", xMin, xMax),
                yAxis, lines);
    }
}
