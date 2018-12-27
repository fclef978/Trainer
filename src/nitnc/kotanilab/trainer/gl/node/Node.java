package nitnc.kotanilab.trainer.gl.node;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import nitnc.kotanilab.trainer.gl.util.Position;
import nitnc.kotanilab.trainer.gl.style.Style;
import nitnc.kotanilab.trainer.gl.util.Vector;

import java.awt.*;

/**
 * ノード
 */
public abstract class Node {

    protected Parent parent = Parent.dummy;
    protected Style style = new Style(this);
    protected GL2 gl = null;
    protected String id = "";
    protected String group = "";

    public Node() {
    }

    public Node(String style) {
        this.getStyle().put(style);
    }

    /**
     * 描画
     */
    public abstract void draw();

    public Style getStyle() {
        return style;
    }

    public Node getParent() {
        return parent;
    }

    /**
     * 描画中かどうかのロックを取得します。
     * @return 描画中かどうかのロック
     */
    public Object getLock() {
        return getParent().getLock();
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public void removeParent() {
        this.parent = Parent.dummy;
    }

    protected GLAutoDrawable getDrawable() {
        return parent.getDrawable();
    }

    protected void setThickness(double thickness) {
        gl.glLineWidth((float) thickness);
    }

    protected void setMode(int mode) {
        gl.glBegin(mode);
    }

    protected void setColor(Color color) {
        gl.glColor3d(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0);
    }

    protected Vector scaleVector(Vector vector) {
        Position position = parent.getPosition();
        return new Vector(vector.getX() * position.getXScale() + position.getXOffset(),
                vector.getY() * position.getYScale() + position.getYOffset());
    }

    protected void setVertex(Vector vector) {
        Vector scaled = scaleVector(vector);
        gl.glVertex2d(scaled.getX(), scaled.getY());
    }

    protected void end() {
        gl.glEnd();
    }

    public int getWindowWidth() {
        return this.getDrawable().getSurfaceWidth();
    }

    public int getWindowHeight() {
        return this.getDrawable().getSurfaceHeight();
    }

    public abstract double getWidth();

    public abstract double getHeight();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return super.toString() + "#'" + id + "." + group;
    }
}

