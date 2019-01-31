package nitnc.kotanilab.trainer.gl.style;

import nitnc.kotanilab.trainer.gl.node.Node;
import nitnc.kotanilab.trainer.util.Dbg;

public class Align extends PropertyXY {
    public Align(String value, Node node) {
        super("align", value, node, true, Property::isPosition, Property::isCenter);
    }

    public static void main(String... args) {
        Property align = new Align("center center", null);
        Dbg.p(align.getValue());
    }
}
