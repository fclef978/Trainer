package nitnc.kotanilab.trainer.gl.style;

import nitnc.kotanilab.trainer.gl.node.Node;

import java.util.function.Predicate;

public class Color extends Property {
    public Color(String value, Node node) {
        super("color", value, node, false, true, Property::isColor);
    }
}
