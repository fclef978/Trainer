package nitnc.kotanilab.trainer.gl.chart;

import nitnc.kotanilab.trainer.gl.chart.plot.LinePlot;
import nitnc.kotanilab.trainer.gl.node.Window;
import nitnc.kotanilab.trainer.gl.pane.HPane;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.gl.pane.StackPane;
import nitnc.kotanilab.trainer.gl.shape.*;
import nitnc.kotanilab.trainer.gl.util.Vector;
import nitnc.kotanilab.trainer.util.Dbg;


import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * グラフです。
 */
public class Chart extends StackPane {

    private LinePlot plot;
    private final Object lock = new Object();
    private String title;

    private Pane titleArea = new StackPane("size:100% 5%;margin:0 95%;border:solid #000000;border-bottom:none;");
    private Pane legendArea = new HPane("size:100% 5%;margin:0 -95%;border:solid #000000;border-top:none;");

    private boolean shotFrag = false;
    private final Object shotFragLock = new Object();
    private String filenamePrefix;

    /**
     * コンストラクタです。
     *
     * @param title グラフのタイトル
     * @param plot 折れ線グラフ
     */
    public Chart(String title, LinePlot plot) {
        getStyle().put("size:95% 95%;margin:0 0;");
        plot.getStyle().put("size:100% 90%;margin:0 0;border:solid #000000;");
        this.title = title;
        this.plot = plot;

        titleArea.getChildren().add(new Text(title, new Vector(0.0, 0.0), false));
        children.addAll(titleArea, legendArea, plot);
        setLegend();
        redraw();
        Dbg.p(titleArea.getStyle().get("border-top-width"));
    }

    /**
     * スレッドセーフ
     */
    public void redraw() {
        synchronized (lock) {
            plot.redraw();
        }
    }

    public void setLegend() {
        plot.getKeys().forEach(key -> {
            Pane pane = new StackPane("size:33% 100%;margin:0 0;");
            pane.getChildren().add(plot.getLegendLine(key, new Vector(-0.9, 0.0), new Vector(-0.3, 0.0)));
            pane.getChildren().add(plot.getLegendText(key, new Vector(0.4, 0.0)));
            legendArea.getChildren().add(pane);
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

    public LinePlot getPlot() {
        return plot;
    }
}
