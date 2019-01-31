package nitnc.kotanilab.trainer.gl.style;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import nitnc.kotanilab.trainer.gl.node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class PropertyXY extends Property {
    protected Property x;
    protected Property y;

    public PropertyXY(String name, String value, Node node, boolean isExtendable, Predicate<String>... rules) {
        super(name, value, node, false, isExtendable, rules);
        x = new Property(name + "-x", "", node, false, isExtendable, rule);
        y = new Property(name + "-y", "", node, true, isExtendable, rule);
        setValue(value);
    }

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
    public void registerToMap(Map<String, Property> map) {
        super.registerToMap(map);
        x.registerToMap(map);
        y.registerToMap(map);
    }

    public Property getX() {
        return x;
    }

    public Property getY() {
        return y;
    }
}
