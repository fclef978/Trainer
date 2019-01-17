package nitnc.kotanilab.trainer.gl.chart;

import nitnc.kotanilab.trainer.gl.pane.StackPane;
import nitnc.kotanilab.trainer.gl.shape.*;
import nitnc.kotanilab.trainer.gl.util.Vector;
import nitnc.kotanilab.trainer.gl.util.VectorList;
import nitnc.kotanilab.trainer.math.point.Point;
import nitnc.kotanilab.trainer.util.Dbg;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 折れ線グラフの線部分
 * 表示部とデータ部でスレッドが異なるため衝突しないように気をつける必要がある。
 */
public class LineGraph extends StackPane {

    protected Axis xAxis;
    protected Axis yAxis;
    protected Map<String, PolygonalLine> lineMap = new LinkedHashMap<>();
    protected Map<String, Line> gideLineMap = new HashMap<>();
    protected Map<String, Text> gideLineLabelMap = new HashMap<>();

    /**
     * コンストラクタです
     *
     * @param xAxis x軸
     * @param yAxis y軸
     */
    public LineGraph(Axis xAxis, Axis yAxis) {
        getStyle().put("size:100% 100%;margin:0 0;border:none;");
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        yAxis.setVertical();
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
        children.add(line);
        lineMap.put(key, line);
    }

    public VectorList getVectorList(String key) {
        if (lineMap.isEmpty()) return null;
        if (!lineMap.keySet().contains(key)) throw new IllegalArgumentException("不正なキーです。" + key);
        return lineMap.get(key).getVectorList();
    }

    public void putGideLine(String label, double value, Color color) {
        double y = yAxis.scale(value);
        Line gideLine = new Line(y, false, color, 1.0);
        Text gideLineLabel = new Text(label, new Vector(-0.95, y), false);
        gideLineLabel.getStyle().put("align:left bottom;");
        gideLineMap.put(label, gideLine);
        gideLineLabelMap.put(label, gideLineLabel);
        children.addAll(gideLine, gideLineLabel);
    }

    public void setGideLine(String label, double value) {
        double y = yAxis.scale(value);
        gideLineMap.get(label).setVector(y, false);
        gideLineLabelMap.get(label).setVector(new Vector(-0.95, y));
    }

    public void clearGideLine() {
        getChildren().removeAll(gideLineMap.values());
        getChildren().removeAll(gideLineLabelMap.values());
        gideLineMap.clear();
        gideLineLabelMap.clear();
    }

    protected Vector createVector(Point point) {
        return new Vector(xAxis.scale(point.getX()), yAxis.scale(point.getY()));
    }

    protected Point createLinearInterpolatedPoint(Point a, Point base, Point b) {
        double slope = (b.getY() - a.getY()) / (b.getX() - a.getX());
        double x = base.getX() - a.getX();
        double y = slope * x + a.getY();
        return new Point(x, y);
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
}
