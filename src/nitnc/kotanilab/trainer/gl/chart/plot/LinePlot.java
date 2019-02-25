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
 * 折れ線グラフのPlotです。
 * 表示部とデータ部でスレッドが異なるため衝突しないように気をつける必要があります。
 */
public class LinePlot extends Plot {

    protected PolygonalLine line;
    private Color color;
    private double thick;

    /**
     * 指定した名前、ランダムカラー、太さ1.0で作成します。
     * @param name 名前
     */
    public LinePlot(String name) {
        this(name, Color.getHSBColor((float) Math.random(), 1.0f, 0.5f), 1.0);
    }

    /**
     * 指定した名前と色と太さで作成します。
     *
     * @param name 名前
     * @param color 線の色
     * @param thick 線の太さ
     */
    public LinePlot(String name, Color color, double thick) {
        super(name);
        this.color = color;
        this.thick = thick;
        getStyle().put("size:100% 100%;margin:0 0;border:none;");
    }

    @Override
    public void setAxises(Axis xAxis, Axis yAxis) {
        super.setAxises(xAxis, yAxis);
        VectorList vectorList = new VectorList(xAxis::scale, yAxis::scale);
        line = new PolygonalLine(vectorList, color, thick);
        children.add(line);
    }

    @Override
    public Line getLegend(Vector start, Vector end) {
        return new Line(start, end, line.getColor(), line.getThick());
    }

    public PolygonalLine getLine() {
        return line;
    }
}
