package jp.ac.numazu_ct.d14122.gl.pane;

import jp.ac.numazu_ct.d14122.gl.node.Node;
import jp.ac.numazu_ct.d14122.gl.node.Parent;
import jp.ac.numazu_ct.d14122.gl.shape.Square;
import jp.ac.numazu_ct.d14122.util.Dbg;

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
