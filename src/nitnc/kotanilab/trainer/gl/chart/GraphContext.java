package nitnc.kotanilab.trainer.gl.chart;

import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.math.analysis.Analyzer;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * グラフ描画系の一連のクラスをまとめたユーティリティクラスです。
 */
public class GraphContext {
    LineGraph graph;
    Chart chart;
    Pane wrapper;
    boolean visible;
    protected boolean pause = false;
    Consumer<? super LineGraph> graphSetter;

    public GraphContext(LineGraph graph, Chart chart, Pane wrapper, boolean visible) {
        this.graph = graph;
        this.chart = chart;
        this.wrapper = wrapper;
        wrapper.getChildren().add(chart);
        this.visible = visible;
    }

    public void confirm(Pane masterPane) {
        if (visible) {
            if (graphSetter != null) {
                graphSetter.accept(graph);
            }
            chart.setGraph();
            masterPane.getChildren().add(wrapper);
        }
    }

    public void update(String key, List<? extends Double> xc, List<? extends Double> yc) {
        if (visible && !pause) {
            graph.getVectorList(key).set(xc, yc);
        }
    }

    public void update(String key, double[] xc, double[] yc) {
        if (visible && !pause) {
            graph.getVectorList(key).set(xc, yc);
        }
    }

    public void ifVisible(Consumer<? super GraphContext> action) {
        if (visible) action.accept(this);
    }

    public void setGraphSetter(Consumer<? super LineGraph> graphSetter) {
        this.graphSetter = graphSetter;
    }

    public LineGraph getGraph() {
        return graph;
    }

    public void setGraph(LineGraph graph) {
        this.graph = graph;
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
