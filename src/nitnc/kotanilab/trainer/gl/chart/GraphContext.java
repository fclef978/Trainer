package nitnc.kotanilab.trainer.gl.chart;

import nitnc.kotanilab.trainer.gl.chart.plot.LinePlot;
import nitnc.kotanilab.trainer.gl.pane.Pane;

import java.util.List;
import java.util.function.Consumer;

/**
 * グラフ描画系の一連のクラスをまとめたユーティリティクラスです。
 */
public class GraphContext {
    LinePlot plot;
    Chart chart;
    Pane wrapper;
    boolean visible;
    protected boolean pause = false;
    Consumer<? super LinePlot> plotSetter;

    public GraphContext(LinePlot plot, Chart chart, Pane wrapper, boolean visible) {
        this.plot = plot;
        this.chart = chart;
        this.wrapper = wrapper;
        wrapper.getChildren().add(chart);
        this.visible = visible;
    }

    public void confirm(Pane masterPane) {
        if (visible) {
            if (plotSetter != null) {
                plotSetter.accept(plot);
            }
            chart.redraw();
            masterPane.getChildren().add(wrapper);
        }
    }

    public void update(String key, List<? extends Double> xc, List<? extends Double> yc) {
        if (visible && !pause) {
            plot.getVectorList(key).set(xc, yc);
        }
    }

    public void update(String key, double[] xc, double[] yc) {
        if (visible && !pause) {
            plot.getVectorList(key).set(xc, yc);
        }
    }

    public void ifVisible(Consumer<? super GraphContext> action) {
        if (visible) action.accept(this);
    }

    public void setPlotSetter(Consumer<? super LinePlot> plotSetter) {
        this.plotSetter = plotSetter;
    }

    public LinePlot getPlot() {
        return plot;
    }

    public void setPlot(LinePlot plot) {
        this.plot = plot;
    }

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public Pane getWrapper() {
        return wrapper;
    }

    public void setWrapper(Pane wrapper) {
        this.wrapper = wrapper;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void inversePause() {
        this.pause = !this.pause;
    }
}
