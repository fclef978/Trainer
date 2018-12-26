package jp.ac.numazu_ct.d14122.gl.shape;

import jp.ac.numazu_ct.d14122.gl.node.Child;
import jp.ac.numazu_ct.d14122.gl.util.Vector;

import java.awt.*;

import static com.jogamp.opengl.GL.GL_LINE_LOOP;

public class Square extends Child {

    private double width;
    private double height;
    private Color color;
    private float thick;
    private Vector a;
    private Vector b;
    private Vector c;
    private Vector d;

    public Square(double width, double height, Color color, double thick) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.thick = (float) thick;
        double x = width;
        double y = height;
        a = new Vector(-x, -y);
        b = new Vector(x, -y);
        c = new Vector(x, y);
        d = new Vector(-x, y);
    }

    @Override
    protected void drawingProcess() {
        setThickness(thick);
        setMode(GL_LINE_LOOP);
        super.setColor(color);
        setVertex(a);
        setVertex(b);
        setVertex(c);
        setVertex(d);
        end();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
