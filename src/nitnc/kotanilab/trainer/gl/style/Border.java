package nitnc.kotanilab.trainer.gl.style;

import nitnc.kotanilab.trainer.gl.node.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * オブジェクトの枠線であるボーダーを表すプロパティです。
 */
public class Border extends Properties {
    private List<Properties> positions = new ArrayList<>(4);
    private List<Properties> types = new ArrayList<>(4);

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


        ByType styles = new ByType("style", "", node);
        ByType colors = new ByType("color", "", node);
        ByType widths = new ByType("width", "", node);
        types.addAll(Arrays.asList(styles, colors, widths));
        for (String positionStr : positionArray) {
            Style style = new Style(name + "-" + positionStr, "", node);
            Width width = new Width(name + "-" + positionStr, "", node);
            Color color = new Color(name + "-" + positionStr, "", node);

            ByPosition byPosition = new ByPosition(positionStr, "", node);
            byPosition.getChildren().addAll(Arrays.asList(style, width, color));
            positions.add(byPosition);
            styles.getChildren().add(style);
            colors.getChildren().add(color);
            widths.getChildren().add(width);
        }

        children.addAll(positions);
        children.addAll(types);

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
            types.forEach(properties -> {
                properties.setValue(s);
            });
        }
    }

    /**
     * ボーダーの位置によって分けられたPropertiesです。
     */
    public static class ByPosition extends Properties {
        public ByPosition(String position, String value, Node node) {
            super("border-" + position, node, false);
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
            super.setValueToChildren(value);
        }
    }

    /**
     * ボーダーのプロパティの種類によって分けられたPropertiesです。
     */
    public static class ByType extends Properties {
        public ByType(String type, String value, Node node) {
            super("border-" + type, node, false);
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
            super.setValueToChildren(value);
        }
    }

    /**
     * ボーダースタイルです。
     */
    public static class Style extends Property {
        public Style(String name, String value, Node node) {
            super(name + "-style", value, node, false, false, Property::isLineStyle, Property::isNone);
        }
    }

    /**
     * ボーダーの太さです。
     */
    public static class Width extends Property {
        public Width(String name, String value, Node node) {
            super(name + "-width", value, node, false, false, Property::isLength);
        }
    }

    /**
     * ボーダーの色です。
     */
    public static class Color extends Property {
        public Color(String name, String value, Node node) {
            super(name + "-color", value, node, false, false, Property::isColor);
        }
    }
}
