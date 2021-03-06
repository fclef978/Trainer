package nitnc.kotanilab.trainer.gl.style;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import nitnc.kotanilab.trainer.gl.node.Node;
import nitnc.kotanilab.trainer.util.Dbg;

/**
 * 親オブジェクトを基準(0)としたときの中心からのずれを表すプロパティです。
 */
public class Margin extends PropertyXY {


    /**
     * コンストラクタです。
     *
     * @param value 値
     * @param node  ノード
     */
    public Margin(String value, Node node) {
        super("margin", value, node, false, Property::isLength);
    }

    public static void main(String... args) {
        Margin margin = new Margin("50% 70%", null);
        margin.setValue("20% 12px");
        Dbg.p(margin.getValue());
    }
}




