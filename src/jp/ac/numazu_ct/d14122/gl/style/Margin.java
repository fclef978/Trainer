package jp.ac.numazu_ct.d14122.gl.style;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.ac.numazu_ct.d14122.gl.node.Node;
import jp.ac.numazu_ct.d14122.util.Dbg;

public class Margin extends PropertyXY {

    public Margin(String value, Node node) {
        super("margin", value, node, false, Property::isLength);
    }

    public static void main(String... args) {
        Margin margin = new Margin("50% 70%", null);
        margin.setValue("20% 12px");
        Dbg.p(margin.getValue());
    }
}




