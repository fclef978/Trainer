package nitnc.kotanilab.trainer.gl.shape;

import nitnc.kotanilab.trainer.gl.node.Node;
import nitnc.kotanilab.trainer.gl.style.Property;
import nitnc.kotanilab.trainer.gl.style.PropertyXY;

import java.util.function.Predicate;

public class Size extends PropertyXY {
    public Size(String value, Node node) {
        super("size", "width", "height", value, node, false, Property::isLength);
    }
}
