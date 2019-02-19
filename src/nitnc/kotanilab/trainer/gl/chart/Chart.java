package nitnc.kotanilab.trainer.gl.chart;

import nitnc.kotanilab.trainer.gl.chart.plot.Plot;
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
 * グラフです。
 */
public class Chart extends StackPane {

    private Axis xAxis;
    private Axis yAxis;
    private CallbackArrayList<Plot> plots = new CallbackArrayList<>();
    private final Object lock = new Object();
    private String title;

    private Pane titleArea = new StackPane("size:100% 5%;margin:0 95%;border:solid #000000;border-bottom:none;");
    private Pane legendArea = new HPane("size:100% 5%;margin:0 -95%;border:solid #000000;border-top:none;");
    private Pane xAxisArea = new StackPane("size:86% 10%;margin:6% -90%;");
    private Pane yAxisArea = new StackPane("size:10% 86%;margin:-90% 6%;");
    private Pane plotArea = new StackPane("size:86% 86%;margin:6% 6%;border:solid #000000;");

    private boolean shotFrag = false;
    private final Object shotFragLock = new Object();
    private String filenamePrefix;

    /**
     * コンストラクタです。
     *
     * @param title グラフのタイトル
     */
    public Chart(String title) {
        getStyle().put("size:95% 95%;margin:0 0;");
        plots.setAddCallback(plot -> {
            plot.getStyle().put("size:100% 90%;margin:0 0;border:solid #000000;");
            children.add(plot);
            return plot;
        });
        plots.setRemoveCallback(plot -> {
            children.removeAll(plot);
            return plot;
        });
        this.title = title;

        titleArea.getChildren().add(new Text(title, new Vector(0.0, 0.0), false));
        children.addAll(titleArea, legendArea);
        setLegend();
        updateAxisElements();
        Dbg.p(titleArea.getStyle().get("border-top-width"));
    }

    /**
     * スレッドセーフ
     */
    public void updateAxisElements() {
        synchronized (lock) {
            xAxisArea.getChildren().clear();
            yAxisArea.getChildren().clear();
            plotArea.getChildren().clear();

            List<Line> xGrids = xAxis.getGraduationLines();
            List<Line> yGrids = yAxis.getGraduationLines();
            List<Text> xAxisLabels = xAxis.getTickMarks();
            List<Text> yAxisLabels = yAxis.getTickMarks();
            Text xLabel = new Text(xAxis.getName(), new Vector(0.0, -0.4), false);
            Text yLabel = new Text(yAxis.getName(), new Vector(-0.4, 0.0), true);

            plotArea.getChildren().addAll(xGrids);
            plotArea.getChildren().addAll(yGrids);
            xAxisArea.getChildren().addAll(xAxisLabels);
            yAxisArea.getChildren().addAll(yAxisLabels);
            xAxisArea.getChildren().add(xLabel);
            yAxisArea.getChildren().add(yLabel);
        }
    }

    public void setLegend() {
        plots.forEach(plot -> {
            plot.getKeys().forEach(key -> {
                Pane pane = new StackPane("size:33% 100%;margin:0 0;");
                Line line = plot.getLegendLine(key, new Vector(-0.9, 0.0), new Vector(-0.3, 0.0));
                if (line == null) return ;
                pane.getChildren().add(line);
                pane.getChildren().add(new Text(key, new Vector(0.4, 0.0), false));
                legendArea.getChildren().add(pane);
            });
        });
    }

    @Override
    public void draw() {
        synchronized (lock) {
            super.draw();
        }
        imaging();
    }

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

    public List<? extends Plot> getPlots() {
        return plots;
    }
}
