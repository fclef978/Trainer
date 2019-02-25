package nitnc.kotanilab.trainer.gl.shape;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLAutoDrawable;
import nitnc.kotanilab.trainer.gl.node.Child;

import java.awt.*;

/**
 * 背景ノードです。
 * Windowクラスが持ちます。
 */
public class BackGround extends Child {
    private Color color;

    /**
     * コンストラクタです。
     *
     * @param color 背景色
     */
    public BackGround(Color color) {
        this.color = color;
    }

    /**
     * 指定したGLAutoDrawableを用いて背景を設定します。
     * 最初に一度呼び出せば設定されます。
     *
     * @param drawable 設定するウィンドウのGLAutoDrawable
     */
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
