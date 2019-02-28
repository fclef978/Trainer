package nitnc.kotanilab.trainer.gl.style;

import nitnc.kotanilab.trainer.gl.node.Node;
import nitnc.kotanilab.trainer.util.Dbg;

/**
 * オブジェクトの揃え位置を表すプロパティです。
 */
public class Align extends PropertyXY {
    /**
     * コンストラクタです。
     *
     * @param value 値
     * @param node  ノード
     */
    public Align(String value, Node node) {
        super("align", value, node, true, Property::isPosition, Property::isCenter);
    }

    public static void main(String... args) {
        Property align = new Align("center center", null);
    }
}
