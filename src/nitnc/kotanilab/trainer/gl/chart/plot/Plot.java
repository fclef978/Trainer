package nitnc.kotanilab.trainer.gl.chart.plot;

import nitnc.kotanilab.trainer.gl.chart.Axis;
import nitnc.kotanilab.trainer.gl.node.Node;
import nitnc.kotanilab.trainer.gl.pane.StackPane;
import nitnc.kotanilab.trainer.gl.shape.Line;
import nitnc.kotanilab.trainer.gl.shape.Text;
import nitnc.kotanilab.trainer.gl.util.Vector;

import java.util.Set;

public abstract class Plot extends StackPane {
    protected String name;
    protected Axis xAxis;
    protected Axis yAxis;

    /**
     * 指定した名前で作成します。
     *
     * @param name 名前
     */
    public Plot(String name) {
        this.name = name;
    }

    /**
     * X軸とY軸をセットします。
     *
     * @param xAxis セットするX軸
     * @param yAxis セットするY軸
     */
    public void setAxises(Axis xAxis, Axis yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    /**
     * 指定した開始、終了点で凡例のLineを返します。
     * 線の色、太さはこのLinePlotのもの同じになります。
     *
     * @param start 開始点
     * @param end   終了点
     * @return 凡例のLine
     */
    public Node getLegend(Vector start, Vector end) {
        return null;
    }

    public Axis getXAxis() {
        return xAxis;
    }

    public Axis getYAxis() {
        return yAxis;
    }

    public String getName() {
        return name;
    }
}
