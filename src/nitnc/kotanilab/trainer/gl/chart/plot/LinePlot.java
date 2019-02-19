package nitnc.kotanilab.trainer.gl.chart.plot;

import nitnc.kotanilab.trainer.gl.chart.Axis;
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
public class LinePlot extends Plot {

    protected Axis xAxis;
    protected Axis yAxis;
    protected Map<String, PolygonalLine> lineMap = new LinkedHashMap<>();
    private Text xLabel;
    private Text yLabel;

    /**
     * コンストラクタです
     */
    public LinePlot() {
        getStyle().put("size:100% 100%;margin:0 0;border:none;");
        yAxis.setVertical(true);
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
        children.addAll(gideLineContext.getNodes());
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

    public Axis getXAxis() {
        return xAxis;
    }

    public Axis getYAxis() {
        return yAxis;
    }
}
