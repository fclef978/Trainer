package jp.ac.numazu_ct.d14122.gl.style;

import jp.ac.numazu_ct.d14122.gl.node.Node;

public class Border extends Property {
    public Border(String value, Node node) {
        super("border", value, node, false, false, Property::isColor);
    }
}
