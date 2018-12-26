package jp.ac.numazu_ct.d14122.gl.shape;

import jp.ac.numazu_ct.d14122.gl.node.Node;
import jp.ac.numazu_ct.d14122.gl.style.Property;
import jp.ac.numazu_ct.d14122.gl.style.PropertyXY;

import java.util.function.Predicate;

public class Size extends PropertyXY {
    public Size(String value, Node node) {
        super("size", "width", "height", value, node, false, Property::isLength);
    }
}
