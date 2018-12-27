package nitnc.kotanilab.trainer.gl.shape;

import nitnc.kotanilab.trainer.gl.node.Child;
import nitnc.kotanilab.trainer.gl.util.Vector;

import java.awt.*;

import static com.jogamp.opengl.GL.GL_LINES;

/**
 * Created by Hirokazu SUZUKI on 2018/07/16.
 * 線オブジェクト
 */
public class Line extends Child {

    private Vector startPoint;
    private Vector endPoint;
    private Color color;
    private float thick;

    public Line(Vector startPoint, Vector endPoint, Color color, double thick) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.color = color;
        this.thick = (float) thick;
    }

    public Line(double position, boolean vertical, Color color, double thick) {
        if (vertical) {
            startPoint = new Vector(position, -1.0);
            endPoint = new Vector(position, 1.0);
        } else {
            startPoint = new Vector(-1.0, position);
            endPoint = new Vector(1.0, position);
        }
        this.color = color;
        this.thick = (float) thick;
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
