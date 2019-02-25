package nitnc.kotanilab.trainer.gl.shape;

import nitnc.kotanilab.trainer.gl.node.Child;
import nitnc.kotanilab.trainer.gl.util.VectorList;

import java.awt.*;

import static com.jogamp.opengl.GL.GL_LINE_STRIP;

/**
 * 折れ線のオブジェクトです。
 */
public class PolygonalLine extends Child {
    private VectorList vectorList;
    private Color color;
    private float thick;
    // im = intermediate

    /**
     * コンストラクタです。
     * @param vectorList 折れ線の座標のリスト
     * @param color 折れ線の色
     * @param thick 折れ線の太さ
     */
    public PolygonalLine(VectorList vectorList, Color color, double thick) {
        this.vectorList = vectorList;
        this.color = color;
        this.thick = (float) thick;
    }

    @Override
    public void drawingProcess() {
        setThickness(thick);
        setMode(GL_LINE_STRIP);
        setColor(color);
        // setするときにparentScaleがnullになる
        VectorList vectorList = this.vectorList;
        vectorList.forEach(this::setVertex);
        end();
    }

    public Color getColor() {
        return color;
    }

    public float getThick() {
        return thick;
    }

    public VectorList getVectorList() {
        return vectorList;
    }

    public void setThick(double thick) {
        this.thick = (float) thick;
    }
}
