package nitnc.kotanilab.trainer.gl.style;

import nitnc.kotanilab.trainer.gl.node.Node;
import nitnc.kotanilab.trainer.gl.style.Property;
import nitnc.kotanilab.trainer.gl.style.PropertyXY;

import java.util.function.Predicate;

/**
 * 親オブジェクトを基準(100%)としたときのオブジェクトの大きさを表すプロパティです。
 */
public class Size extends PropertyXY {
    /**
     * コンストラクタです。
     *
     * @param value 値
     * @param node  ノード
     */
    public Size(String value, Node node) {
        super("size", "width", "height", value, node, false, Property::isLength);
    }
}
