package nitnc.kotanilab.trainer.fx.controller;

import nitnc.kotanilab.trainer.gl.chart.Axis;
import nitnc.kotanilab.trainer.gl.chart.Chart;
import nitnc.kotanilab.trainer.gl.pane.Pane;

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

    /**
     * コンストラクタです。
     * @param chart 使用するChart
     * @param wrapper Chartを保持するラッパーになるPane
     * @param visible 初期表示するかどうか
     */
    public GraphContext(Chart chart, Pane wrapper, boolean visible) {
        this.chart = chart;
        this.wrapper = wrapper;
        wrapper.getChildren().add(chart);
        this.visible = visible;
    }

    /**
     * axisSetterを呼び出し、軸や凡例の表示を更新し、指定した親Paneに追加することで描画を開始するユーティリティメソッドです。
     * @param masterPane 親Pane
     */
    public void confirm(Pane masterPane) {
        if (visible) {
            if (axisSetter != null) {
                axisSetter.accept(chart.getXAxis(), chart.getYAxis());
            }
            chart.updateAxisElements();
            chart.updateLegend();
            masterPane.getChildren().add(wrapper);
        }
    }

    /**
     * 表示可能かつ一時停止中で無ければ指定したConsumerを呼び出し、それ以外の場合は何も行いません。
     * @param action 表示可能かつ一時停止中で無い場合に実行されるブロック
     */
    public void ifVisible(Consumer<? super GraphContext> action) {
        if (isVisible()) action.accept(this);
    }

    /**
     * 軸の設定ブロックをセットします。
     * @param plotSetter 軸の設定ブロック (xAxis, yAxis) -> {}
     */
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
        return visible && !pause;
    }

    /**
     * 表示可能かつ一時停止中でないかを返します。
     * @param visible 表示可能かつ一時停止中でないか
     */
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
