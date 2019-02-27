package nitnc.kotanilab.trainer.gl.style;

import nitnc.kotanilab.trainer.gl.node.Node;

import java.util.Map;
import java.util.function.Predicate;

/**
 * 縦と横について同じようなプロパティが存在する際、それらをまとめるスーパークラスです。
 */
public class PropertyXY extends Property {
    /**
     * X軸(横)のプロパティ
     */
    protected Property x;
    /**
     * Y軸(縦)のプロパティ
     */
    protected Property y;

    /**
     * XとYのプロパティの名前をこのプロパティのnameの末尾に"-x","-y"をつけた名前にして作成します。
     *
     * @param name         このプロパティの名前
     * @param value        初期値
     * @param node         このプロパティが所属するノード
     * @param isExtendable 継承可能かどうか
     * @param rules        このプロパティにセットできる値がどうか判別する述語
     *                     全ての述語はORで連結される
     */
    public PropertyXY(String name, String value, Node node, boolean isExtendable, Predicate<String>... rules) {
        super(name, value, node, false, isExtendable, rules);
        x = new Property(name + "-x", "", node, false, isExtendable, rule);
        y = new Property(name + "-y", "", node, true, isExtendable, rule);
        setValue(value);
    }

    /**
     * XとYのプロパティにそれぞれ別の名前をつけて作成します。
     *
     * @param name         このプロパティの名前
     * @param xName        Xのプロパティの名前
     * @param yName        Yのプロパティの名前
     * @param value        初期値
     * @param node         このプロパティが所属するノード
     * @param isExtendable 継承可能かどうか
     * @param rules        このプロパティにセットできる値がどうか判別する述語
     *                     全ての述語はORで連結される
     */
    public PropertyXY(String name, String xName, String yName, String value, Node node, boolean isExtendable, Predicate<String>... rules) {
        super(name, value, node, false, isExtendable, rules);
        x = new Property(xName, "", node, false, isExtendable, rule);
        y = new Property(yName, "", node, true, isExtendable, rule);
        setValue(value);
    }

    @Override
    public void setValue(String value) {
        String[] split = value.trim().split(" ");
        if (split.length == 0) {
            x.setValue(value);
            y.setValue(value);
        } else {
            x.setValue(split[0]);
            y.setValue(split[1]);
        }
    }

    @Override
    public String getValue() {
        return x.getValue() + " " + y.getValue();
    }

    @Override
    public void setRule(Predicate<String> rule) {
        super.setRule(rule);
        x.setRule(rule);
        y.setRule(rule);
    }

    @Override
    public void putsToMap(Map<String, Property> map) {
        super.putsToMap(map);
        x.putsToMap(map);
        y.putsToMap(map);
    }

    public Property getX() {
        return x;
    }

    public Property getY() {
        return y;
    }
}
