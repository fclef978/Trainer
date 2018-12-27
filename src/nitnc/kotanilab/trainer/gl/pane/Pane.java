package nitnc.kotanilab.trainer.gl.pane;

import nitnc.kotanilab.trainer.gl.node.Node;
import nitnc.kotanilab.trainer.gl.node.Parent;
import nitnc.kotanilab.trainer.gl.shape.Square;
import nitnc.kotanilab.trainer.util.Dbg;

import java.awt.*;

public abstract class Pane extends Parent {

    Square border = new Square(1.0, 1.0, Color.WHITE, 1.0);

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

        if (!style.get("border").getValue().equals("none")) {
            border.setColor(Color.decode(style.get("border").getValue()));
            border.setParent(this);
            border.draw();
            border.removeParent();
        }
    }
}
