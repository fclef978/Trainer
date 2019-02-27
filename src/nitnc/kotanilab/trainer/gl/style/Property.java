package nitnc.kotanilab.trainer.gl.style;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import nitnc.kotanilab.trainer.gl.node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * スタイルシートのスタイルの種類であるプロパティのスーパークラスです。
 */
public class Property {

    /**
     * 文字列が長さに関する値であるか判定します。
     *
     * @param value 文字列
     * @return 長さに関する値ならtrue
     */
    protected static boolean isLength(String value) {
        return value.endsWith("%") || value.endsWith("px") || value.equals("0");
    }

    /**
     * 文字列が位置に関する値であるか判定します。
     *
     * @param value 文字列
     * @return 位置に関する値ならtrue
     */
    protected static boolean isPosition(String value) {
        return value.equals("left") || value.equals("right") || value.equals("top") || value.equals("bottom");
    }

    /**
     * 文字列が中央に関する値であるか判定します。
     *
     * @param value 文字列
     * @return 中央に関する値ならtrue
     */
    protected static boolean isCenter(String value) {
        return value.equals("center");
    }

    /**
     * 文字列が色に関する値であるか判定します。
     *
     * @param value 文字列
     * @return 色に関する値ならtrue
     */
    protected static boolean isColor(String value) {
        return value.startsWith("#");
    }

    /**
     * 文字列が"none"かどうか判定します。
     *
     * @param value 文字列
     * @return noneならtrue
     */
    protected static boolean isNone(String value) {
        return value.equals("none");
    }

    /**
     * 文字列が線のスタイルに関する値であるか判定します。
     *
     * @param value 文字列
     * @return 線のスタイルに関する値ならtrue
     */
    protected static boolean isLineStyle(String value) {
        return value.equals("solid") || value.equals("dashed");
    }

    /**
     * プロパティの名前
     */
    protected String name;
    /**
     * プロパティの値
     */
    protected StringProperty value = new SimpleStringProperty("");
    /**
     * このプロパティが所属しているノード
     */
    protected Node node;
    /**
     * 垂直なものに関するプロパティかどうか
     */
    protected boolean isVertical;
    /**
     * 継承可能なプロパティかどうか
     */
    protected boolean isExtendable;
    /**
     * このプロパティにセットできる値がどうか判別する述語
     */
    protected Predicate<String> rule = str -> true;

    /**
     * コンストラクタです。
     *
     * @param name         プロパティの名前
     * @param value        プロパティの初期値
     * @param node         プロパティが所属するノード
     * @param isVertical   垂直なものに関するかどうか
     * @param isExtendable 継承可能かどうか
     * @param rules        このプロパティにセットできる値がどうか判別する述語
     *                     全ての述語はORで連結される
     */
    public Property(String name, String value, Node node, boolean isVertical, boolean isExtendable, Predicate<String>... rules) {
        this.name = name;
        this.value.set(value);
        this.node = node;
        this.isVertical = isVertical;
        this.isExtendable = isExtendable;
        this.rule = combineByOr(rules);
    }

    /**
     * 指定した述語全てをORで連結し一つの述語にします。
     *
     * @param predicates 連結する述語
     * @return 連結された述語
     */
    @SafeVarargs
    protected static Predicate<String> combineByOr(Predicate<String>... predicates) {
        return s -> {
            boolean ret = false;
            for (Predicate<String> predicate : predicates) {
                ret = ret || predicate.test(s);
            }
            return ret;
        };
    }

    /**
     * 指定したMapにこのプロパティのnameをキー、このプロパティ自体を値としてput()するユーティリティメソッドです。
     *
     * @param map put()するMap
     */
    public void putsToMap(Map<String, Property> map) {
        map.put(getName(), this);
    }

    /**
     * このプロパティに値をセットします。
     * もし不正な値ならセットされません。
     *
     * @param value セットする値
     */
    public void setValue(String value) {
        if (rule.test(value)) this.value.setValue(value);
    }

    public String getValue() {
        return value.getValue();
    }

    /**
     * 値を数値に変換して返します。
     * 数値に変換できない場合、0.0が返ります。
     *
     * @return 数値
     */
    public double getRawValueAsNumber() {
        String tmp = value.toString().replaceAll("[^0-9+]", "");
        if (tmp.isEmpty()) return 0.0;
        else return Double.parseDouble(tmp);
    }

    /**
     * 長さなどを親オブジェクトに対する割合で返します。
     *
     * @return 画面幅や画面高に対する割合
     */
    public double getValueAsRatio() {
        double ret = 0.0;
        String value = this.value.getValue();
        try {
            if (value.endsWith("%")) {
                ret = Double.parseDouble(value.split("%")[0]) / 100.0;
            } else if (value.endsWith("px")) {
                if (isVertical) {
                    ret = Double.parseDouble(value.split("px")[0]) / node.getWindowHeight();
                } else {
                    ret = Double.parseDouble(value.split("px")[0]) / node.getWindowWidth();
                }
            } else if (value.equals("0")) {
                ret = 0.0;
            }
        } catch (NumberFormatException e) {
            throw new UnsupportedOperationException("サポートされていないプロパティです " + name + ":" + value);
        }
        return ret;
    }

    public String getName() {
        return name;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public boolean isVertical() {
        return isVertical;
    }

    public boolean isExtendable() {
        return isExtendable;
    }

    public Predicate<String> getRule() {
        return rule;
    }

    public void setRule(Predicate<String> rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        return "Property{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
