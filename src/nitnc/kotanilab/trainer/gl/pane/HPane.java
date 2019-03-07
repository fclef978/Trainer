package nitnc.kotanilab.trainer.gl.pane;

import com.jogamp.opengl.GL2;
import nitnc.kotanilab.trainer.gl.node.Node;
import nitnc.kotanilab.trainer.gl.util.Position;
import nitnc.kotanilab.trainer.util.Dbg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 単一の水平行に子をレイアウトするPaneです。
 */
public class HPane extends Pane {


    /**
     * コンストラクタです。
     */
    public HPane() {
    }

    /**
     * 指定したスタイルで作成します。
     *
     * @param style スタイルシート
     */
    public HPane(String style) {
        super(style);
    }

    @Override
    public void drawingProcess() {
        double totalWidth = children.stream().mapToDouble(Node::getWidth).sum();
        final double[] previousWidth = {0};
        children.forEach(child -> {
            double center = child.getWidth();
            double offset = 2.0 * previousWidth[0] + center - totalWidth;
            previousWidth[0] += center;
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glPushMatrix();
            gl.glTranslatef((float) offset, 0, 0.0f);
            child.draw();
            gl.glPopMatrix();
        });

        // this.position = new Position(parent.getPosition(), getStyle());

    }
}
