package nitnc.kotanilab.trainer.gl.style;

import nitnc.kotanilab.trainer.gl.node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 同じようなプロパティが複数存在する際にそれらをまとめるスーパークラスです。
 */
public class Properties extends Property {
    /**
     * 子のプロパティのリスト
     */
    protected List<Property> children = new ArrayList<>();

    /**
     * コンストラクタです。
     *
     * @param name         プロパティの名前
     * @param node         プロパティが所属するノード
     * @param isExtendable 継承可能かどうか
     */
    protected Properties(String name, Node node, boolean isExtendable) {
        super(name, "", node, false, isExtendable);
    }

    /**
     * 子のプロパティ全てに値をセットします。
     *
     * @param value セットする値
     */
    protected void setValueToChildren(String value) {
        String[] split = value.trim().split(" ");
        for (String s : split) {
            children.forEach(property -> {
                if (property.rule.test(s)) {
                    property.setValue(s);
                }
            });
        }
    }

    @Override
    public void putsToMap(Map<String, Property> map) {
        super.putsToMap(map);
        children.forEach(property -> property.putsToMap(map));
    }

    public List<Property> getChildren() {
        return children;
    }
}
