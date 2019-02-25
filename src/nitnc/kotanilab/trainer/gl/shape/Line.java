package nitnc.kotanilab.trainer.gl.shape;

import nitnc.kotanilab.trainer.gl.node.Child;
import nitnc.kotanilab.trainer.gl.util.Vector;

import java.awt.*;

import static com.jogamp.opengl.GL.GL_LINES;

/**
 * 一本線のオブジェクトです。
 */
public class Line extends Child {

    private Vector startPoint;
    private Vector endPoint;
    private Color color;
    private float thick;

    /**
     * 開始点と終了点を指定して作成します。
     *
     * @param startPoint 開始点
     * @param endPoint   終了点
     * @param color      線の色
     * @param thick      線の太さ
     */
    public Line(Vector startPoint, Vector endPoint, Color color, double thick) {
        setVector(startPoint, endPoint);
        this.color = color;
        this.thick = (float) thick;
    }

    /**
     * 水平または垂直で、端から端までの長さで作成します。
     *
     * @param position 位置座標
     * @param vertical 垂直かどうか
     * @param color    線の色
     * @param thick    線の太さ
     */
    public Line(double position, boolean vertical, Color color, double thick) {
        setVector(position, vertical);
        this.color = color;
        this.thick = (float) thick;
    }

    /**
     * 開始点と終了点をセットします。
     *
     * @param startPoint 開始点
     * @param endPoint   終了点
     */
    public void setVector(Vector startPoint, Vector endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    /**
     * 向きと位置をセットします。
     *
     * @param position 位置座標
     * @param vertical 垂直かどうか
     */
    public void setVector(double position, boolean vertical) {
        if (vertical) {
            startPoint = new Vector(position, -1.0);
            endPoint = new Vector(position, 1.0);
        } else {
            startPoint = new Vector(-1.0, position);
            endPoint = new Vector(1.0, position);
        }
    }

    @Override
    public void drawingProcess() {
        setThickness(thick);
        setMode(GL_LINES);
        setColor(color);
        setVertex(startPoint);
        setVertex(endPoint);
        end();
    }

}
