package nitnc.kotanilab.trainer.main;


import nitnc.kotanilab.trainer.gl.node.Window;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.gl.pane.StackPane;
import nitnc.kotanilab.trainer.gl.shape.Line;

import java.awt.*;

/**
 * Created by Hirokazu SUZUKI on 2019/03/06.
 * a
 */
public class Test {
    Window window = new Window("", 480, 360);
    Pane p1 = new StackPane("size:90% 90%;border:solid 6px #000000");
    Pane p2 = new StackPane("size:50% 50%;margin: 50% 50%;border:solid 6px #888888");
    Pane p3 = new StackPane("size:50% 50%;margin: -50% -50%;border:solid 6px #888888");
    Line l1x = new Line(0.0, false, Color.BLUE, 1.0);
    Line l1y = new Line(0.0, true, Color.BLUE, 1.0);
    Line l2x = new Line(0.0, false, Color.RED, 1.0);
    Line l2y = new Line(0.0, true, Color.RED, 1.0);
    Line l3x = new Line(0.0, false, Color.GREEN, 1.0);
    Line l3y = new Line(0.0, true, Color.GREEN, 1.0);

    public static void main(final String... args) {
        new Test();
    }

    public Test() {
        window.getChildren().add(p1);
        p1.getChildren().addAll(p2, p3, l1x, l1y);
        p2.getChildren().addAll(l2x, l2y);
        p3.getChildren().addAll(l3x, l3y);
        window.launch();
    }
}
