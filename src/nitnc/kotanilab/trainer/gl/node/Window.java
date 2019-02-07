package nitnc.kotanilab.trainer.gl.node;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import nitnc.kotanilab.trainer.gl.pane.StackPane;
import nitnc.kotanilab.trainer.gl.shape.BackGround;
import nitnc.kotanilab.trainer.gl.util.Position;
import nitnc.kotanilab.trainer.util.Dbg;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.io.File;
import java.nio.ByteBuffer;

/**
 * ウィンドウクラスです
 */
public class Window extends StackPane implements GLEventListener {

    private GLWindow window;
    private Animator animator;
    private BackGround bg = new BackGround(Color.WHITE);
    private GLAutoDrawable drawable = null;
    private final Object lock = new Object();
    private boolean shotFrag = false;
    private final Object shotFragLock = new Object();

    /**
     * コンストラクタです。
     *
     * @param name   ウィンドウの名前
     * @param width  幅[px]
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
     *
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
    public Window getRoot() {
        return this;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        bg.init(drawable);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        if (animator != null) animator.stop();
        window.getWindowListener(0).windowDestroyed(null);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        this.drawable = drawable;
        draw();
        if (shotFrag) {
            imaging("castStream.png", drawable);
            synchronized (shotFragLock) {
                shotFrag = false;
            }
        }
    }

    private static int calcTripleLoopIndex(int i, int j, int k, int jNum, int kNum) {
        return (i * jNum + j) * kNum + k;
    }

    private static int calcDoubleLoopIndex(int i, int j, int jNum) {
        return i * jNum + j;
    }

    public static void imaging(String filename, GLAutoDrawable drawable) {
        imaging(filename, drawable, 0, 0, drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
    }

    public static void imaging(String filename, GLAutoDrawable drawable, int x1, int y1, int x2, int y2) {
        GL2 gl = drawable.getGL().getGL2();
        int imageWidth = x2 - x1;
        int imageHeight = y2 - y1;
        int colorNum = 3;
        int imageSize = (int) Math.round(imageWidth * imageHeight * colorNum * 1.1); // なんかバッファサイズが足らなくなる
        ByteBuffer rawBuffer = ByteBuffer.allocate(imageSize);
        byte[] imageArray = new byte[imageSize];

        gl.glReadBuffer(GL2.GL_BACK);
        gl.glReadPixels(x1, y1, imageWidth, imageHeight, GL2.GL_BGR, GL2.GL_UNSIGNED_BYTE, rawBuffer);
        byte[] rawArray = rawBuffer.array();
        for (int iRaw = 0; iRaw < imageHeight; iRaw++) {
            int iProcessed = imageHeight - iRaw - 1;
            int jNum = imageWidth * colorNum;
            for (int j = 0; j < jNum; j++) {
                imageArray[calcDoubleLoopIndex(iProcessed, j, jNum)] = rawArray[calcDoubleLoopIndex(iRaw, j, jNum)];
                /*
                for (int k = 0; k < colorNum; k++) {
                    int processedIndex = calcTripleLoopIndex(iProcessed, j, k, imageWidth, colorNum);
                    int rawIndex = calcTripleLoopIndex(iRaw, j, k, imageWidth, colorNum);
                    imageArray[processedIndex] = rawArray[rawIndex];
                }
                */
            }
        }

        BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR);
        SampleModel sampleModel = bufferedImage.getSampleModel();
        DataBufferByte dataBufferByte = new DataBufferByte(imageArray, imageArray.length);
        Raster raster = Raster.createRaster(sampleModel, dataBufferByte, null);
        bufferedImage.setData(raster);
        try {
            String[] split = filename.split("\\.");
            String extension;
            if (split.length == 2) extension = split[1].toUpperCase();
            else extension = "JPG";
            switch (extension) {
                case "BMP":
                    ImageIO.write(bufferedImage, "BMP", new File(filename));
                    break;
                case "PNG":
                    ImageIO.write(bufferedImage, "PNG", new File(filename));
                    break;
                case "JPG":
                    ImageIO.write(bufferedImage, "JPG", new File(filename));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shot() {
        synchronized (shotFragLock) {
            shotFrag = true;
        }
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }
}
