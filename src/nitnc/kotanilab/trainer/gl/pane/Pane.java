package nitnc.kotanilab.trainer.gl.pane;

import com.jogamp.opengl.GL2;
import nitnc.kotanilab.trainer.gl.node.Node;
import nitnc.kotanilab.trainer.gl.node.Parent;
import nitnc.kotanilab.trainer.gl.shape.Border;
import nitnc.kotanilab.trainer.util.Dbg;

import java.awt.*;
import java.util.Objects;

/**
 * 子を持ちレイアウト可能な親ノードです。
 */
public abstract class Pane extends Parent {

    private Border border = new Border();

    /**
     * コンストラクタです。
     */
    public Pane() {
    }

    /**
     * 指定したスタイルで作成します。
     *
     * @param style スタイルシート
     */
    public Pane(String style) {
        super(style);
    }

    /**
     * 描画プロセスです。
     */
    public void drawingProcess() {
        children.stream().filter(node -> node.getParent() == this).forEach(Node::draw);
    }

    @Override
    public void draw() {
        super.draw();
        gl = getDrawable().getGL().getGL2();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glTranslatef((float) style.get("margin-x").getValueAsRatio(), (float) style.get("margin-y").getValueAsRatio(), 0.0f);
        gl.glScalef((float) style.get("width").getValueAsRatio(),(float) style.get("height").getValueAsRatio(),1.0f);
        drawingProcess();

        for (String key : nitnc.kotanilab.trainer.gl.style.Border.positionArray) {
            if (!style.get("border-" + key + "-style").getValue().equals("none")) {
                border.getSettingMap().put(key, new Border.BorderSetting(
                        style.get("border-" + key + "-style").getValue(),
                        style.get("border-" + key + "-width").getRawValueAsNumber(),
                        Color.decode(style.get("border-" + key + "-color").getValue()))
                );
            }
        }
        border.setParent(this);
        border.draw();
        border.removeParent();
        gl.glPopMatrix();
    }
}
