package nitnc.kotanilab.trainer.gl.style;

import nitnc.kotanilab.trainer.gl.node.Node;
import nitnc.kotanilab.trainer.util.Dbg;

import java.util.ArrayList;
import java.util.List;

/**
 * オブジェクトの枠線であるボーダーを表すプロパティです。
 */
public class Border extends Properties {
    private List<Properties> positions = new ArrayList<>(4);
    private List<Property> types = new ArrayList<>(4);

    /**
     * プロパティのうち、位置の値を持つ配列です。
     */
    public static String[] positionArray = {"top", "right", "bottom", "left"};

    /**
     * コンストラクタです。
     *
     * @param value 値
     * @param node  ノード
     */
    public Border(String value, Node node) {
        super("border", node, false);
        children.add(new Style(name, "", node) {
            @Override
            public void setValue(String value) {
                super.setValue(value);
                positions.forEach(property -> property.setValue(value));
            }
        });
        children.add(new Width(name, "", node) {
            @Override
            public void setValue(String value) {
                super.setValue(value);
                positions.forEach(property -> property.setValue(value));
            }
        });
        children.add(new Color(name, "", node) {
            @Override
            public void setValue(String value) {
                super.setValue(value);
                positions.forEach(property -> property.setValue(value));
            }
        });
        types.addAll(children);
        for (String position : positionArray) {
            Properties tmp = new BorderByPosition(position, "", node);
            positions.add(tmp);
            children.add(tmp);
        }
        setValue(value);
    }

    @Override
    public String getValue() {
        StringBuilder ret = new StringBuilder();
        types.forEach(property -> {
            ret.append(property.getValue()).append(" ");
        });
        return ret.toString();
    }

    @Override
    public void setValue(String value) {
        String[] split = value.trim().split(" ");
        for (String s : split) {
            types.forEach(property -> {
                if (property.rule.test(s)) {
                    property.setValue(s);
                }
            });
        }
    }

    public static class BorderByPosition extends Properties {
        public BorderByPosition(String position, String value, Node node) {
            super("border-" + position, node, false);
            children.add(new Style(name, "none", node));
            children.add(new Width(name, "1px", node));
            children.add(new Color(name, "#000000", node));
            setValue(value);
        }

        @Override
        public String getValue() {
            StringBuilder ret = new StringBuilder();
            children.forEach(property -> {
                ret.append(property.getValue()).append(" ");
            });
            return ret.toString();
        }

        @Override
        public void setValue(String value) {
            super.setValueByRule(value);
        }
    }

    public static class Style extends Property {
        public Style(String name, String value, Node node) {
            super(name + "-style", value, node, false, false, Property::isLineStyle, Property::isNone);
        }
    }

    public static class Width extends Property {
        public Width(String name, String value, Node node) {
            super(name + "-width", value, node, false, false, Property::isLength);
        }
    }

    public static class Color extends Property {
        public Color(String name, String value, Node node) {
            super(name + "-color", value, node, false, false, Property::isColor);
        }
    }

    public static void main(String... args) {
        Border border = new Border("solid #000000 2px", null);
        border.setValue("none #FFFFFF 3px");
        border.positions.forEach(properties -> {
            properties.children.forEach(Dbg::p);
        });
    }
}
