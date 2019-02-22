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

    /**
     * 新しいGraphContextを指定したChartで作成し、graphContextMapに指定したキーで追加するユーティリティメソッドです。
     * wrapperPaneの幅は1マスで初期非表示です。
     *
     * @param key   Mapのキー
     * @param chart GraphContextのChart
     */
    public void putNewGraphContext(String key, Chart chart) {
        graphContextMap.put(key, new GraphContext(chart, createWrapperPane(1), false));
    }

    /**
     * 新しいGraphContextをgraphContextMapに追加するユーティリティメソッドです。
     * キーは指定したChartのtitleで、wrapperPaneの幅は1マスで初期非表示です。
     *
     * @param chart GraphContextのChart
     */
    public void putNewGraphContext(Chart chart) {
        graphContextMap.put(chart.getTitle(), new GraphContext(chart, createWrapperPane(1), false));
    }

    protected List<Pane> getGraphWrappers() {
        return graphContextMap.values().stream().map(GraphContext::getWrapper).collect(Collectors.toList());
    }

    public List<Chart> getCharts() {
        return graphContextMap.values().stream().map(GraphContext::getChart).collect(Collectors.toList());
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

    private static void addLinePlotsToChart(Chart chart, String... lines) {
        int i = 0;
        for (String lineName : lines) {
            chart.getPlots().add(new LinePlot(lineName, getLineColor(i++), 1.0));
        }
    }

    protected static Chart createChart(String name, Axis xAxis, Axis yAxis) {
        Chart ret;
        ret = new Chart(name, xAxis, yAxis);
        return ret;
    }

    protected static Chart createWaveChart(String name, double xMax, Unit yUnit, double yMin, double yMax) {
        Axis xAxis = new Axis("Time[sec]", 0.0, xMax, xMax / 10.0);
        Axis yAxis = new Axis(yUnit.toString(), yMin, yMax, (yMax - yMin) / 10.0);
        xAxis.setReverse(true);
        return new Chart(name, xAxis, yAxis);
    }

    protected static Chart createWaveChart(String name, double xMax, Unit yUnit, double yRange) {
        Axis xAxis = new Axis("Time[sec]", 0.0, xMax, xMax / 10.0);
        xAxis.setReverse(true);
        return new Chart(name, xAxis, new Axis(yUnit.toString(), -yRange, yRange, yRange / 5.0));
    }

    protected static Chart createTimeSeriesChart(String name, double xMax, Axis yAxis) {
        Axis xAxis = new Axis("Time[sec]", 0.0, xMax, xMax / 10.0);
        return new Chart(name, xAxis, yAxis);
    }

    protected static Chart createSpectrumChart(String name, double xMin, double xMax, Axis yAxis) {
        Axis xAxis = new LogAxis("Frequency[Hz]", xMin, xMax);
        return new Chart(name, xAxis, yAxis);
    }
}
