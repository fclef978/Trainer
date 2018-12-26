package jp.ac.numazu_ct.d14122.gl.style;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.ac.numazu_ct.d14122.gl.node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Property {

    protected static boolean isLength(String value) {
        return value.endsWith("%") || value.endsWith("px") || value.equals("0");
    }

    protected static boolean isPosition(String value) {
        return value.equals("left") || value.equals("center") || value.equals("right") || value.equals("top") || value.equals("bottom");
    }

    protected static boolean isColor(String value) {
        return value.startsWith("#") || value.equals("none");
    }

    protected String name;
    protected StringProperty value = new SimpleStringProperty("");
    protected Node node;
    protected boolean isVertical;
    protected boolean isExtendable;
    protected Predicate<String> rule = str -> true;
    protected List<Property> children = new ArrayList<>();

    public Property(String name, String value, Node node, boolean isVertical, boolean isExtendable, Predicate<String> rule) {
        this.name = name;
        this.value.set(value);
        this.node = node;
        this.isVertical = isVertical;
        this.isExtendable = isExtendable;
        this.rule = rule;
        children.add(this);
    }

    public void registerToMap(Map<String, Property> map) {
        children.forEach(property -> map.put(property.getName(), property));
    }

    public void setValue(String value) {
        if (rule.test(value)) this.value.setValue(value);
        else this.value.setValue("");
    }

    public String getValue() {
        return value.getValue();
    }

    public double getNumber() {
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

    public void setRule(Predicate<String> rule) {
        this.rule = rule;
    }
}
