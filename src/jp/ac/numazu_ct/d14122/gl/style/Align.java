package jp.ac.numazu_ct.d14122.gl.style;

import jp.ac.numazu_ct.d14122.gl.node.Node;
import jp.ac.numazu_ct.d14122.util.Dbg;

public class Align extends PropertyXY {
    public Align(String value, Node node) {
        super("align", value, node, true, Property::isPosition);
    }

    public static void main(String... args) {
        Property align = new Align("center center", null);
        Dbg.p(align.getValue());
    }
}
