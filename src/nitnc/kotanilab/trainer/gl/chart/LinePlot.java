package nitnc.kotanilab.trainer.gl.chart;

import nitnc.kotanilab.trainer.gl.node.Node;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.gl.pane.StackPane;
import nitnc.kotanilab.trainer.gl.shape.*;
import nitnc.kotanilab.trainer.gl.util.Vector;
import nitnc.kotanilab.trainer.gl.util.VectorList;
import nitnc.kotanilab.trainer.math.point.Point;
import nitnc.kotanilab.trainer.util.Dbg;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * 折れ線グラフのプロットクラスです。
 * 表示部とデータ部でスレッドが異なるため衝突しないように気をつける必要があります。
 */
public class LinePlot extends StackPane {

    protected Axis xAxis;
    protected Axis yAxis;
    protected Map<String, PolygonalLine> lineMap = new LinkedHashMap<>();
    protected Map<String, GideLineContext> gideLineContextMap = new HashMap<>();
    private Text xLabel;
    private Text yLabel;
    private Pane plotArea = new StackPane("size:86% 86%;margin:6% 6%;border:solid #000000;");
    private Pane xAxisArea = new StackPane("size:86% 10%;margin:6% -90%;");
    private Pane yAxisArea = new StackPane("size:10% 86%;margin:-90% 6%;");

    /**
     * コンストラクタです
     *
     * @param xAxis x軸
     * @param yAxis y軸
     */
    public LinePlot(Axis xAxis, Axis yAxis) {
        getStyle().put("size:100% 100%;margin:0 0;border:none;");
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        yAxis.setVertical(true);
        children.addAll(xAxisArea, yAxisArea, plotArea);
    }

    /**
     * 指定したキーと色と太さで系列を追加します。
     *
     * @param key   キー
     * @param color 線の色
     * @param thick 線の太さ
     */
    public void addLine(String key, Color color, double thick) {
        VectorList vectorList = new VectorList(xAxis::scale, yAxis::scale);
        PolygonalLine line = new PolygonalLine(vectorList, color, thick);
        line.setGroup(key);
        plotArea.getChildren().add(line);
        lineMap.put(key, line);
    }

    /**
     * 軸の再描画を行います。
     * Axisの設定を変えた後に実行すると描画に反映されます。
     */
    public void redraw() {
        xAxisArea.getChildren().clear();
        yAxisArea.getChildren().clear();
        plotArea.getChildren().clear();

        List<Line> xGrids = xAxis.getGraduationLines();
        List<Line> yGrids = yAxis.getGraduationLines();
        List<Text> xAxisLabels = xAxis.getTickMarks();
        List<Text> yAxisLabels = yAxis.getTickMarks();

        plotArea.getChildren().addAll(xGrids);
        plotArea.getChildren().addAll(yGrids);
        gideLineContextMap.values().forEach(glc -> {
            glc.getNodes().forEach(node -> plotArea.getChildren().add(node));
        });
        plotArea.getChildren().addAll(lineMap.values());
        xAxisArea.getChildren().addAll(xAxisLabels);
        yAxisArea.getChildren().addAll(yAxisLabels);

        xLabel = new Text(xAxis.getName(), new Vector(0.0, -0.4), false);
        xAxisArea.getChildren().add(xLabel);
        yLabel = new Text(yAxis.getName(), new Vector(-0.4, 0.0), true);
        yAxisArea.getChildren().add(yLabel);
    }

    /**
     * 指定したキーの折れ線の描画に使用しているPolygonalLineを返します。
     *
     * @param key キー
     * @return 折れ線の描画に使用しているPolygonalLine
     */
    public PolygonalLine getLine(String key) {
        return lineMap.get(key);
    }

    /**
     * 指定したキーの折れ線の描画に使用しているPolygonalLineの絶対描画位置を持つVectorListを返します。
     *
     * @param key キー
     * @return PolygonalLineのVectorList
     */
    public VectorList getVectorList(String key) {
        if (lineMap.isEmpty()) return null;
        if (!lineMap.keySet().contains(key)) throw new IllegalArgumentException("不正なキーです。" + key);
        return lineMap.get(key).getVectorList();
    }

    /*
    TODO Chartへ移す
     */
    public void putGideLine(String label, double value, Color color, double width, boolean vertical) {
        GideLineContext gideLineContext = new GideLineContext(label, value, color, width, vertical);
        gideLineContextMap.put(label, gideLineContext);
        plotArea.getChildren().addAll(gideLineContext.getNodes());
    }

    public void setGideLine(String label, double value) {
        gideLineContextMap.get(label).setPosition(value);
    }

    public void clearGideLine() {
        gideLineContextMap.values().forEach(gideLineContext -> getChildren().removeAll(gideLineContext.getNodes()));
        gideLineContextMap.clear();
    }

    public Set<String> getKeys() {
        return lineMap.keySet();
    }

    /**
     * 凡例の線を返します。
     *
     * @param key   キー
     * @param start 開始点
     * @param end   終了点
     * @return 凡例線
     */
    public Line getLegendLine(String key, Vector start, Vector end) {
        PolygonalLine pl = lineMap.get(key);
        return new Line(start, end, pl.getColor(), pl.getThick());
    }

    /**
     * X軸のラベルを返します。
     *
     * @return X軸ラベル
     */
    public String getXLabel() {
        return xAxis.getName();
    }

    /**
     * Y軸のラベルを返します。
     *
     * @return Y軸ラベル
     */
    public String getYLabel() {
        return yAxis.getName();
    }

    /**
     * 凡例テキストを返します。
     *
     * @param key    キー
     * @param vector 位置
     * @return 凡例テキスト
     */
    public Text getLegendText(String key, Vector vector) {
        return new Text(key, vector, false);
    }

    public Axis getXAxis() {
        return xAxis;
    }

    public Axis getYAxis() {
        return yAxis;
    }

    private class GideLineContext {
        Line gideLine;
        double width;
        Text label;
        Color color;
        boolean vertical;
        List<Node> nodes;

        public GideLineContext(String label, double position, Color color, double width, boolean vertical) {
            this.gideLine = new Line(0.0, vertical, color, width);
            this.label = new Text(new Font("", Font.ITALIC, 10), Color.BLACK, label, new Vector(0.0, 0.0), vertical);
            this.color = color;
            this.width = width;
            this.vertical = vertical;
            this.nodes = Arrays.asList(this.gideLine, this.label);
            ;
            if (vertical) {
                this.label.getStyle().put("align:right bottom;");
            } else {
                this.label.getStyle().put("align:left bottom;");
            }
            setPosition(position);
        }

        public void setPosition(double position) {
            Vector vector;
            double absPosition;
            if (vertical) {
                absPosition = xAxis.scale(position);
                vector = new Vector(absPosition, 0.95);
            } else {
                absPosition = yAxis.scale(position);
                vector = new Vector(-0.95, absPosition);
            }
            gideLine.setVector(absPosition, vertical);
            label.setVector(vector);
        }

        public List<Node> getNodes() {
            return nodes;
        }
    }
}
