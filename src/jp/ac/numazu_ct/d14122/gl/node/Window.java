package jp.ac.numazu_ct.d14122.gl.node;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;
import jp.ac.numazu_ct.d14122.gl.pane.StackPane;
import jp.ac.numazu_ct.d14122.gl.shape.BackGround;
import jp.ac.numazu_ct.d14122.gl.util.Position;
import jp.ac.numazu_ct.d14122.util.Dbg;

import java.awt.*;

/**
 * ウィンドウクラスです
 */
public class Window extends StackPane implements GLEventListener {

    private GLWindow window;
    private Animator animator;
    private BackGround bg = new BackGround(Color.BLACK);
    private GLAutoDrawable drawable = null;
    private final Object lock = new Object();

    /**
     * コンストラクタです。
     * @param name ウィンドウの名前
     * @param width 幅[px]
     * @param height 高さ[px]
     */
    public Window(String name, int width, int height) {
        GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
        window = GLWindow.create(caps); // 遅い
        window.setTitle(name);
        window.setSize(width, height);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyed(WindowEvent evt) {
                System.exit(0);
            }
        });

        animator = new Animator();
        animator.add(window);

        window.addGLEventListener(this);

        position = new Position();
        children.add(bg);
    }

    /**
     * ウィンドウをスタートします。
     */
    public void launch() {
        animator.start();
        window.setVisible(true);
    }

    public void terminate() {
        window.setVisible(false);
        window.destroy();
    }

    /**
     * JOGLのGLWindowオブジェクトを返します。
     * @return GLWindowオブジェクト
     */
    public GLWindow getWindow() {
        return window;
    }

    public void setSize(int w, int h) {
        window.setSize(w, h);
    }

    @Override
    protected GLAutoDrawable getDrawable() {
        return drawable;
    }

    @Override
    public void draw() {
        synchronized (lock) {
            children.each(Node::draw);
        }
    }

    @Override
    public Object getLock() {
        return lock;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        bg.init(drawable);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        if(animator != null) animator.stop();
        window.getWindowListener(0).windowDestroyed(null);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        this.drawable = drawable;
        draw();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }
}
