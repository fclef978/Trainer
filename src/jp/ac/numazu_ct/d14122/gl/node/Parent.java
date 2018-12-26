package jp.ac.numazu_ct.d14122.gl.node;

import jp.ac.numazu_ct.d14122.gl.util.Position;
import jp.ac.numazu_ct.d14122.util.Dbg;

public abstract class Parent extends Node {
    public Parent() {
        if (getStyle().get("border") == null) {
            getStyle().put("border:none;");
        }
    }

    public Parent(String style) {
        super(style);
        if (getStyle().get("border") == null) {
            getStyle().put("border:none;");
        }
    }

    protected Children children = new Children(this);

    protected Position position = null;

    public Children getChildren() {
        return children;
    }

    @Override
    public void draw() {
        position = new Position(parent.getPosition(), getStyle());
    }

    @Override
    public double getWidth() {
        return style.get("width").getNumber();
        // return children.stream().mapToDouble(Node::getWidth).sum() * style.getNum("width");
    }

    @Override
    public double getHeight() {
        return style.get("height").getNumber();
        // return children.stream().mapToDouble(Node::getHeight).sum() * style.getNum("height");
    }

    public Position getPosition() {
        return position;
    }

    public static Parent dummy = new Parent() {
        @Override
        public Position getPosition() {
            throw new IllegalArgumentException("ダミーです");
        }
    };

}
