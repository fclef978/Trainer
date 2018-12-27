package nitnc.kotanilab.trainer.gl.style;

import nitnc.kotanilab.trainer.gl.node.Node;
import nitnc.kotanilab.trainer.gl.shape.Size;
import nitnc.kotanilab.trainer.util.Dbg;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Style {

    private Map<String, Property> map = new HashMap<>();
    private Node node;

    public Style(Node node) {
        this.node = node;
        initMap();
    }

    private void initMap() {
        List<Property> list = Arrays.asList(
                new Margin("0 0", node),
                new Size("100% 100%", node),
                new Align("center center", node),
                new Border("none", node),
                new Color("#FFFFFF", node)
        );
        list.forEach(property -> property.registerToMap(map));
    }

    public void put(String key, String val) {
        key = key.trim();
        val = val.trim();
        map.get(key).setValue(val); // ぬるぽ
    }

    public void put(String statements) {
        for (String statement : statements.trim().split(";")) {
            String[] set = statement.trim().split(":");
            this.put(set[0], set[1]);
        }
    }

    public Property get(String key) {
        Property ret = map.get(key);
        if (ret.getValue().isEmpty() && ret.isExtendable()) {
            ret = node.getParent().getStyle().get(key);
        }
        return ret;
    }

}
