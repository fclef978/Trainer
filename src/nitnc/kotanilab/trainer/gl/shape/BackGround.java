package nitnc.kotanilab.trainer.gl.shape;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLAutoDrawable;
import nitnc.kotanilab.trainer.gl.node.Child;

import java.awt.*;

/**
 * Created by Hirokazu SUZUKI on 2018/07/16.
 * 背景
 */
public class BackGround extends Child {
    private Color color;

    public BackGround(Color color) {
        this.color = color;
    }

    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();
        gl.glClearColor(
                color.getRed() / 255.0f,
                color.getGreen() / 255.0f,
                color.getBlue() / 255.0f,
                1.0f
        );
    }

    public Color getColor() {
        return color;
    }

    @Override
    protected void drawingProcess() {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    }
}
