package nitnc.kotanilab.trainer.gl.chart;

import nitnc.kotanilab.trainer.gl.node.Window;
import nitnc.kotanilab.trainer.gl.pane.HPane;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.gl.pane.StackPane;
import nitnc.kotanilab.trainer.gl.shape.*;
import nitnc.kotanilab.trainer.gl.util.Vector;
import nitnc.kotanilab.trainer.util.Dbg;


import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * グラフです。
 */
public final class Chart extends StackPane {

    private LineGraph graph;
    private Text xLabel;
    private Text yLabel;
    private final Object lock = new Object();
    private String title;

    private BackGround bg = new BackGround(Color.BLACK);

    private Pane titleArea = new StackPane("size:100% 5%;margin:0 95%;border:solid #000000;border-bottom:none;");
    private Pane graphArea = new StackPane("size:100% 90%;margin:0 0;border:solid #000000;");
    private Pane legendArea = new HPane("size:100% 5%;margin:0 -95%;border:solid #000000;border-top:none;");
    private Pane plotArea = new StackPane("size:86% 86%;margin:6% 6%;border:solid #000000;");
    private Pane xAxisArea = new StackPane("size:86% 10%;margin:6% -90%;");
    private Pane yAxisArea = new StackPane("size:10% 86%;margin:-90% 6%;");

    private boolean shotFrag = false;
    private final Object shotFragLock = new Object();
    private String filenamePrefix;

    /**
     * コンストラクタです。
     *
     * @param title グラフのタイトル
     * @param graph 折れ線グラフ
     */
    public Chart(String title, LineGraph graph) {
        getStyle().put("size:95% 95%;margin:0 0;");
        this.title = title;
        this.graph = graph;

        setGraph();
        titleArea.getChildren().add(new Text(title, new Vector(0.0, 0.0), false));

        children.addAll(titleArea, legendArea, graphArea);
        setLegend();
        Dbg.p(titleArea.getStyle().get("border-top-width"));
    }

    /**
     * スレッドセーフ
     */
    public void setGraph() {
        synchronized (lock) {
            plotArea.getChildren().clear();
            xAxisArea.getChildren().clear();
            yAxisArea.getChildren().clear();
            graphArea.getChildren().clear();

            List<Line> xGrid = graph.getXAxis().getGrids();
            List<Line> yGrid = graph.getYAxis().getGrids();
            List<Text> xAxisLabel = graph.getXAxis().getGridStrings();
            List<Text> yAxisLabel = graph.getYAxis().getGridStrings();

            // 描画順に気をつけること
            plotArea.getChildren().addAll(xGrid);
            plotArea.getChildren().addAll(yGrid);
            plotArea.getChildren().add(graph);
            xAxisArea.getChildren().addAll(xAxisLabel);
            yAxisArea.getChildren().addAll(yAxisLabel);
            graphArea.getChildren().addAll(plotArea, xAxisArea, yAxisArea);

            xLabel = new Text(graph.getXAxis().getName(), new Vector(0.0, -0.4), false);
            xAxisArea.getChildren().add(xLabel);
            yLabel = new Text(graph.getYAxis().getName(), new Vector(-0.4, 0.0), true);
            yAxisArea.getChildren().add(yLabel);
        }
    }

    public void setLegend() {
        graph.getKeys().forEach(key -> {
            Pane pane = new StackPane("size:33% 100%;margin:0 0;");
            pane.getChildren().add(graph.getLegendLine(key, new Vector(-0.9, 0.0), new Vector(-0.3, 0.0)));
            pane.getChildren().add(graph.getLegendText(key, new Vector(0.4, 0.0)));
            legendArea.getChildren().add(pane);
        });
    }

    @Override
    public void draw() {
        xLabel.setString(graph.getXLabel());
        yLabel.setString(graph.getYLabel());
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

    public LineGraph getGraph() {
        return graph;
    }
}
