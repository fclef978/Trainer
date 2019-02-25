package nitnc.kotanilab.trainer.gl.node;

import nitnc.kotanilab.trainer.gl.util.Position;

/**
 * 親ノードのスーパークラスです。
 * 複数の子を持つことができます。
 */
public abstract class Parent extends Node {
    /**
     * コンストラクタです。
     */
    public Parent() {
        if (getStyle().get("border") == null) {
            getStyle().put("border:none;");
        }
    }

    /**
     * 指定したスタイルで作成します。
     *
     * @param style スタイルシート
     */
    public Parent(String style) {
        super(style);
        if (getStyle().get("border") == null) {
            getStyle().put("border:none;");
        }
    }

    /**
     * 子ノードのコレクション
     */
    protected Children children = new Children(this);

    /**
     * 子が描画するときに参照してくる位置や縮尺などのデータを持ったオブジェクト
     */
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
        return style.get("width").getValueAsRltNumber();
        // return children.stream().mapToDouble(Node::getWidth).sum() * style.getNum("width");
    }

    @Override
    public double getHeight() {
        return style.get("height").getValueAsRltNumber();
        // return children.stream().mapToDouble(Node::getHeight).sum() * style.getNum("height");
    }

    public Position getPosition() {
        return position;
    }

    /**
     * ルートノードに接続されていないときにParentに接続されるダミーのParentです。
     * 各種操作時に専用の例外を発生させます。
     */
    public static Parent dummy = new Parent() {
        @Override
        public Position getPosition() {
            throw new IllegalArgumentException("ダミーです");
        }

        @Override
        public Object getLock() {
            throw new IllegalArgumentException("ダミーです");
        }

        @Override
        public Window getRoot() {
            throw new IllegalArgumentException("ダミーです");
        }
    };

}
