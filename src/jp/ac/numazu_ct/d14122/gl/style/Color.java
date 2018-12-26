package jp.ac.numazu_ct.d14122.gl.style;

import jp.ac.numazu_ct.d14122.gl.node.Node;

import java.util.function.Predicate;

public class Color extends Property {
    public Color(String value, Node node) {
        super("color", value, node, false, true, Property::isColor);
    }
}
