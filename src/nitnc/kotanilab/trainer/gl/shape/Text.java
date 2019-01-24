package nitnc.kotanilab.trainer.gl.shape;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import nitnc.kotanilab.trainer.gl.node.Child;
import nitnc.kotanilab.trainer.gl.util.Vector;
import nitnc.kotanilab.trainer.util.Dbg;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

/**
 * Created by Hirokazu SUZUKI on 2018/07/17.
 * 文字
 */
public class Text extends Child {

    public static final Font DEFAULT_FONT = new Font("", Font.PLAIN, 11);

    protected Color color;
    protected TextRenderer tr;
    protected String str;
    protected Vector vector;
    protected boolean vertical;

    public Text(String str, Vector vector, boolean vertical) {
        this.str = str;
        this.vector = vector;
        this.vertical = vertical;
        this.color = Color.decode(style.get("color").getValue());
        tr = new TextRenderer(DEFAULT_FONT, true, true);
    }

    public Text(Font font, Color color, String str, Vector vector, boolean vertical) {
        this.color = color;
        this.str = str;
        this.vector = vector;
        this.vertical = vertical;
        tr = new TextRenderer(font, true, true);
    }

    @Override
    protected void drawingProcess() {
        int width = this.getWindowWidth();
        int height = this.getWindowHeight();
        Vector vector = scaleVector(this.vector);
        //  アンチエイリアス
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL.GL_BLEND);
        gl.glEnable(GL.GL_LINE_SMOOTH);

        tr.beginRendering(width, height);
        tr.setColor(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
        if (vertical) {
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glPushMatrix();
            gl.glRotated(90, 0.0, 0.0, 1.0);
            // 秘伝のタレ
            render(str, vector, width, height);
            tr.endRendering();
            tr.flush();
            gl.glPopMatrix();
        } else {
            render(str, vector, width, height);
            tr.endRendering();
            tr.flush();
        }
        gl.glDisable(GL.GL_BLEND);
        gl.glDisable(GL.GL_LINE_SMOOTH);
    }

    protected void render(String str, Vector vector, int width, int height) {
        double x, y;
        double tmpX = getShiftQuantity(str, style.get("align-x").getValue(), false);
        double tmpY = getShiftQuantity(str, style.get("align-y").getValue(), true);
        if (vertical) {
            x = changeScale(vector.getY(), height) - tmpX;
            y = -changeScale(vector.getX(), width) - tmpY;
        } else {
            x = changeScale(vector.getX(), width) - tmpX;
            y = changeScale(vector.getY(), height) - tmpY;
        }
        render(str, x, y);
    }

    private double getShiftQuantity(String str, String align, boolean vertical) {
        if (vertical) {
            Rectangle2D bounds = tr.getBounds(str + "|qypjg");
            if (align == null || align.equals("center")) {
                return -bounds.getCenterY();
            } else if (align.equals("top")) {
                return -bounds.getMinY();
            } else if (align.equals("bottom")) {
                return -bounds.getMaxY();
            } else {
                return -bounds.getMaxY();
            }
        } else {
            Rectangle2D bounds = tr.getBounds(str);
            if (align == null || align.equals("center")) {
                return bounds.getCenterX();
            } else if (align.equals("right")) {
                return bounds.getWidth();
            } else if (align.equals("left")) {
                return 0.0;
            } else {
                return 0.0;
            }
        }
    }

    private void render(String str, double x, double y) {
        tr.draw(str, (int) Math.round(x), (int) Math.round(y));
    }

    private static double changeScale(double a, double b) {
        return ((a + 1.0) * b / 2.0);
    }

    public void setString(String str) {
        this.str = str;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }
}
