package nitnc.kotanilab.trainer.gl.style;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import nitnc.kotanilab.trainer.gl.node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Property {

    protected static boolean isLength(String value) {
        return value.endsWith("%") || value.endsWith("px") || value.equals("0");
    }

    protected static boolean isPosition(String value) {
        return value.equals("left") || value.equals("right") || value.equals("top") || value.equals("bottom");
    }

    protected static boolean isCenter(String value) {
        return value.equals("center");
    }

    protected static boolean isColor(String value) {
        return value.startsWith("#");
    }

    protected static boolean isNone(String value) {
        return value.equals("none");
    }

    protected static boolean isLineStyle(String value) {
        return value.equals("solid") || value.equals("dashed");
    }

    protected String name;
    protected StringProperty value = new SimpleStringProperty("");
    protected Node node;
    protected boolean isVertical;
    protected boolean isExtendable;
    protected Predicate<String> rule = str -> true;

    public Property(String name, String value, Node node, boolean isVertical, boolean isExtendable, Predicate<String>... rules) {
        this.name = name;
        this.value.set(value);
        this.node = node;
        this.isVertical = isVertical;
        this.isExtendable = isExtendable;
        this.rule = combineByOr(rules);
    }

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

    public void registerToMap(Map<String, Property> map) {
        map.put(getName(), this);
    }

    public void setValue(String value) {
        if (rule.test(value)) this.value.setValue(value);
        else this.value.setValue("");
    }

    public String getValue() {
        return value.getValue();
    }

    public double getRawValueAsNumber() {
        String tmp = value.toString().replaceAll("[^0-9+]", "");
        if (tmp.isEmpty()) return 0.0;
        else return Double.parseDouble(tmp);
    }

    public double getValueAsRltNumber() {
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

    public boolean getValueAsBoolean() {
        return Boolean.parseBoolean(getValue());
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
