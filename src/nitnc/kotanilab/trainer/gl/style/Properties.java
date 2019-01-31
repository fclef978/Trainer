package nitnc.kotanilab.trainer.gl.style;

import nitnc.kotanilab.trainer.gl.node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Properties extends Property {
    List<Property> children = new ArrayList<>();

    public Properties(String name, Node node, boolean isExtendable) {
        super(name, "", node, false, isExtendable);
    }

    public void setValueByRule(String value) {
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
    public void registerToMap(Map<String, Property> map) {
        super.registerToMap(map);
        children.forEach(property -> property.registerToMap(map));
    }

    public List<Property> getChildren() {
        return children;
    }
}
