package nitnc.kotanilab.trainer.gl.node;

/**
 * Nodeに描画系の実装を少しした子ノード用のスーパークラスです。
 */
public abstract class Child extends Node {

    /**
     * コンストラクタです。
     */
    public Child() {
    }

    /**
     * 指定したスタイルで作成します。
     *
     * @param style スタイルシート
     */
    public Child(String style) {
        super(style);
    }

    @Override
    public void draw() {
        gl = parent.getDrawable().getGL().getGL2();
        drawingProcess();
    }

    /**
     * 描画プロセスです。
     * このメソッドの中でしかOpenGLの描画は行えません。
     */
    protected abstract void drawingProcess();

    @Override
    public double getWidth() {
        return style.get("width").getValueAsRltNumber();
    }

    @Override
    public double getHeight() {
        return style.get("height").getValueAsRltNumber();
    }
}
