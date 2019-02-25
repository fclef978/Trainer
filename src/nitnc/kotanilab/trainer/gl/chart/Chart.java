package nitnc.kotanilab.trainer.gl.chart;

import nitnc.kotanilab.trainer.gl.chart.plot.Plot;
import nitnc.kotanilab.trainer.gl.node.Node;
import nitnc.kotanilab.trainer.gl.node.Window;
import nitnc.kotanilab.trainer.gl.pane.HPane;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.gl.pane.StackPane;
import nitnc.kotanilab.trainer.gl.shape.*;
import nitnc.kotanilab.trainer.gl.util.Vector;
import nitnc.kotanilab.trainer.util.CallbackArrayList;
import nitnc.kotanilab.trainer.util.Dbg;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * グラフの大元のクラスです。
 * タイトル・軸・プロット部・凡例を管理します。
 */
public class Chart extends StackPane {

    private Axis xAxis;
    private Axis yAxis;
    private CallbackArrayList<Plot> plots = new CallbackArrayList<>();
    private final Object lock = new Object();
    private String title;

    private Pane titlePane = new StackPane("size:100% 5%;margin:0 95%;border:solid #000000 1px;border-bottom:none;");
    private Pane legendPane = new HPane("size:100% 5%;margin:0 -95%;border:solid #000000 1px;border-top:none;");
    private Pane graphPane = new StackPane("size:100% 90%;margin:0 0;border:solid #000000 1px;");
    private Pane xAxisPane = new StackPane("size:86% 10%;margin:6% -90%;");
    private Pane yAxisPane = new StackPane("size:10% 86%;margin:-90% 6%;");
    private Pane plotPane = new StackPane("size:86% 86%;margin:6% 6%;border:solid #000000 1px;");

    private boolean shotFrag = false;
    private final Object shotFragLock = new Object();
    private String filenamePrefix;

    /**
     * コンストラクタです。
     *
     * @param title グラフのタイトル
     * @param xAxis X軸
     * @param yAxis Y軸
     */
    public Chart(String title, Axis xAxis, Axis yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.yAxis.setVertical(true);
        getStyle().put("size:95% 95%;margin:0 0;");
        plots.setAddCallback(plot -> {
            plot.getStyle().put("size:86% 86%;margin:6% 6%;border:solid #000000 1px;");
            plot.setAxises(xAxis, yAxis);
            graphPane.getChildren().add(plot);
            return plot;
        });
        plots.setRemoveCallback(plot -> {
            graphPane.getChildren().removeAll(plot);
            return plot;
        });
        this.title = title;

        titlePane.getChildren().add(new Text(title, new Vector(0.0, 0.0), false));
        graphPane.getChildren().addAll(xAxisPane, yAxisPane, plotPane);
        children.addAll(titlePane, graphPane, legendPane);
        updateLegend();
        updateAxisElements();
    }

    /**
     * 軸ラベルや目盛り線の更新を行います。
     * Axisオブジェクトを変更した際はこのメソッドを呼び出さないと表示に反映されません。
     */
    public void updateAxisElements() {
        synchronized (lock) {
            xAxisPane.getChildren().clear();
            yAxisPane.getChildren().clear();
            plotPane.getChildren().clear();

            List<Line> xGrids = xAxis.getGraduationLines();
            List<Line> yGrids = yAxis.getGraduationLines();
            List<Text> xAxisLabels = xAxis.getTickMarks();
            List<Text> yAxisLabels = yAxis.getTickMarks();
            Text xLabel = new Text(xAxis.getName(), new Vector(0.0, -0.4), false);
            Text yLabel = new Text(yAxis.getName(), new Vector(-0.4, 0.0), true);

            plotPane.getChildren().addAll(xGrids);
            plotPane.getChildren().addAll(yGrids);
            xAxisPane.getChildren().addAll(xAxisLabels);
            yAxisPane.getChildren().addAll(yAxisLabels);
            xAxisPane.getChildren().add(xLabel);
            yAxisPane.getChildren().add(yLabel);
        }
    }

    /**
     * 凡例の更新を行います。
     * Plotを増やしたり、見た目を変えてもこのメソッドを呼び出さないと表示に反映されません。
     */
    public void updateLegend() {
        synchronized (lock) {
            legendPane.getChildren().clear();
            plots.forEach(plot -> {
                Pane pane = new StackPane("size:33% 100%;margin:0 0;");
                Node legend = plot.getLegend(new Vector(-0.9, 0.0), new Vector(-0.3, 0.0));
                if (legend == null) return;
                pane.getChildren().add(legend);
                pane.getChildren().add(new Text(plot.getName(), new Vector(0.4, 0.0), false));
                legendPane.getChildren().add(pane);
            });
        }
    }

    @Override
    public void draw() {
        synchronized (lock) {
            super.draw();
        }
        imaging();
    }

    /**
     * 指定したファイル名(拡張子抜き)でスクリーンショットします。
     * フォーマットはPNGです。
     *
     * @param filenamePrefix ファイル名(拡張子抜き)
     */
    public void shot(String filenamePrefix) {
        synchronized (shotFragLock) {
            shotFrag = true;
            this.filenamePrefix = filenamePrefix;
        }
    }

    private void imaging() {
        if (shotFrag) {
            Vector begin = calcAbsVector(new Vector(-1.0, -1.0));
            Vector end = calcAbsVector(new Vector(1.0, 1.0));
            StringBuilder filename = new StringBuilder(filenamePrefix.trim().replaceAll("\\.", "").replaceAll("\\s", "_"));
            filename.append("_").append(title.replaceAll("\\s", "_")).append("_");
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
            filename.append(sdf.format(calendar.getTime())).append(".png");
            Window.imaging(filename.toString(), getDrawable(),
                    calcPx(begin.getX(), getWindowWidth()), calcPx(begin.getY(), getWindowHeight()),
                    calcPx(end.getX(), getWindowWidth()), calcPx(end.getY(), getWindowHeight()));
            synchronized (shotFragLock) {
                shotFrag = false;
            }
        }
    }

    public List<Plot> getPlots() {
        return plots;
    }

    public Axis getXAxis() {
        return xAxis;
    }

    public void setXAxis(Axis xAxis) {
        this.xAxis = xAxis;
    }

    public Axis getYAxis() {
        return yAxis;
    }

    public void setYAxis(Axis yAxis) {
        this.yAxis = yAxis;
    }

    public String getTitle() {
        return title;
    }
}
