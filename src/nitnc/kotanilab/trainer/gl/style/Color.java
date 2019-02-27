package nitnc.kotanilab.trainer.gl.style;

import nitnc.kotanilab.trainer.gl.node.Node;

import java.util.function.Predicate;

/**
 * 文字色のプロパティです。
 */
public class Color extends Property {
    /**
     * コンストラクタです。
     *
     * @param value 値
     * @param node  ノード
     */
    public Color(String value, Node node) {
        super("color", value, node, false, true, Property::isColor);
    }
}
