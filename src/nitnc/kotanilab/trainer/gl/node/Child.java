package nitnc.kotanilab.trainer.gl.node;

public abstract class Child extends Node {
    public Child() {
    }

    public Child(String style) {
        super(style);
    }

    @Override
    public void draw() {
        gl = parent.getDrawable().getGL().getGL2();
        drawingProcess();
    }
    /**
     * 描画プロセス
     */
    protected abstract void drawingProcess();

    @Override
    public double getWidth() {
        return style.get("width").getNumber();
    }

    @Override
    public double getHeight() {
        return style.get("height").getNumber();
    }
}
