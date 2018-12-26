package jp.ac.numazu_ct.d14122.math.analysis;

import jp.ac.numazu_ct.d14122.gl.chart.Axis;
import jp.ac.numazu_ct.d14122.gl.chart.LineGraph;
import jp.ac.numazu_ct.d14122.gl.chart.LogAxis;
import jp.ac.numazu_ct.d14122.gl.pane.HEnumPane;
import jp.ac.numazu_ct.d14122.gl.pane.Pane;
import jp.ac.numazu_ct.d14122.gl.pane.StackPane;
import jp.ac.numazu_ct.d14122.gl.shape.Text;
import jp.ac.numazu_ct.d14122.gl.util.Vector;
import jp.ac.numazu_ct.d14122.math.WaveBuffer;
import jp.ac.numazu_ct.d14122.math.series.Unit;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Analyzer {
    protected Pane masterPane;
    protected WaveBuffer source;
    protected List<Pane> panes = new ArrayList<>();
    protected String borderStyle;
    protected String title;

    protected Analyzer(Pane masterPane, String title) {
        this.masterPane = masterPane;
        this.title = title;
        StringBuilder colorCode = new StringBuilder("#");
        for (int i = 0; i < 6; i++) {
            String tmp = Integer.toHexString((int) Math.round(Math.random() * 0xF)).toUpperCase();
            colorCode.append(tmp);
        }
        borderStyle = "border:" + colorCode + ";";
    }

    public void start(double fs, int n, double waveXMax, double waveYMax) {
    }

    public void stop() {
        masterPane.getChildren().removeAll(panes);
    }

    public abstract Analyzer setVisible(boolean... visible);

    public abstract void execute();

    public void setSource(WaveBuffer source) {
        this.source = source;
    }

    protected Pane createWrapperPane(int size) {
        int width = 25 * size;
        Pane ret =  new StackPane("height:100%;margin-x:0;margin-y:0;");
        ret.getStyle().put("width", width + "%");
        ret.getStyle().put(borderStyle);
        return ret;
    }

    protected void clearVectorList(LineGraph lineGraph) {
        lineGraph.getKeys().forEach(key -> lineGraph.getVectorList(key).clear());
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

    private static Color[] lineColors = {
            Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.GREEN, Color.RED, Color.BLUE
    };

    private static Color getLineColor(int i) {
        return lineColors[i % lineColors.length];
    }

    private static void addLines(LineGraph lineGraph, String... lines) {
        int i = 0;
        for (String lineName : lines) {
            lineGraph.addLine(lineName, getLineColor(i++), 1.0);
        }
    }

    protected static LineGraph createGraph(Axis xAxis, Axis yAxis, String... lines) {
        LineGraph ret;
        ret = new LineGraph(xAxis, yAxis);
        addLines(ret, lines);
        return ret;
    }

    protected static LineGraph createWaveGraph(double xMax, Unit yUnit, double yMin, double yMax, String... lines) {
        Axis xAxis = new Axis("Time[sec]", 0.0, xMax, xMax / 10.0);
        xAxis.setReverse(true);
        return createGraph(xAxis, new Axis(yUnit.toString(), yMin, yMax, (yMax - yMin) / 5.0), lines);
    }

    protected static LineGraph createWaveGraph(double xMax, Unit yUnit, double yRange, String... lines) {
        Axis xAxis = new Axis("Time[sec]", 0.0, xMax, xMax / 10.0);
        xAxis.setReverse(true);
        return createGraph(xAxis, new Axis(yUnit.toString(), -yRange, yRange, yRange / 5.0), lines);
    }

    protected static LineGraph createTimeSeriesGraph(double xMax, Axis yAxis, String... lines) {
        Axis xAxis = new Axis("Time[sec]", 0.0, xMax, xMax / 10.0);
        // xAxis.setReverse(true);
        return createGraph(xAxis, yAxis, lines);
    }

    protected static LineGraph createSpctrumGraph(double xMin, double xMax, Axis yAxis, String... lines) {
        return createGraph(
                new LogAxis("Frequency[Hz]", xMin, xMax, 0.1),
                yAxis, lines);
    }
}
