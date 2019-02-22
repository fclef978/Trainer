package nitnc.kotanilab.trainer.gl.chart;

import nitnc.kotanilab.trainer.gl.chart.plot.LinePlot;
import nitnc.kotanilab.trainer.gl.chart.plot.Plot;
import nitnc.kotanilab.trainer.gl.pane.Pane;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * グラフ描画系の一連のクラスをまとめたユーティリティクラスです。
 */
public class GraphContext {
    Chart chart;
    Pane wrapper;
    boolean visible;
    protected boolean pause = false;
    BiConsumer<? super Axis, ? super Axis> axisSetter;

    public GraphContext(Chart chart, Pane wrapper, boolean visible) {
        this.chart = chart;
        this.wrapper = wrapper;
        wrapper.getChildren().add(chart);
        this.visible = visible;
    }

    public void confirm(Pane masterPane) {
        if (visible) {
            if (axisSetter != null) {
                axisSetter.accept(chart.getXAxis(), chart.getYAxis());
            }
            chart.updateAxisElements();
            masterPane.getChildren().add(wrapper);
        }
    }

    public void ifVisible(Consumer<? super GraphContext> action) {
        if (visible && !pause) action.accept(this);
    }

    public void setAxisSetter(BiConsumer<? super Axis, ? super Axis> plotSetter) {
        this.axisSetter = plotSetter;
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
