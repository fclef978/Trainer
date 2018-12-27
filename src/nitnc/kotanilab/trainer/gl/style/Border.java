package nitnc.kotanilab.trainer.gl.style;

import nitnc.kotanilab.trainer.gl.node.Node;

public class Border extends Property {
    public Border(String value, Node node) {
        super("border", value, node, false, false, Property::isColor);
    }
}
