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

    public Children getChildren() {
        return children;
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public double getWidth() {
        return style.get("width").getValueAsRatio();
        // return children.stream().mapToDouble(Node::getWidth).sum() * style.getNum("width");
    }

    @Override
    public double getHeight() {
        return style.get("height").getValueAsRatio();
        // return children.stream().mapToDouble(Node::getHeight).sum() * style.getNum("height");
    }


    /**
     * ルートノードに接続されていないときにParentに接続されるダミーのParentです。
     * 各種操作時に専用の例外を発生させます。
     */
    public static Parent dummy = new Parent() {

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
