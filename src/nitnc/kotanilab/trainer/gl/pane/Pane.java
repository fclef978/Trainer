package nitnc.kotanilab.trainer.gl.pane;

import nitnc.kotanilab.trainer.gl.node.Node;
import nitnc.kotanilab.trainer.gl.node.Parent;
import nitnc.kotanilab.trainer.gl.shape.Border;
import nitnc.kotanilab.trainer.util.Dbg;

import java.awt.*;

public abstract class Pane extends Parent {

    private Border border = new Border();

    public Pane() {
    }

    public Pane(String style) {
        super(style);
    }

    public void drawingProcess() {
        children.stream().filter(node -> node.getParent() == this).forEach(Node::draw);
    }

    @Override
    public void draw() {
        super.draw();

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
    }
}
