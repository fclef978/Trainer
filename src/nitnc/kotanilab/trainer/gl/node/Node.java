package nitnc.kotanilab.trainer.gl.node;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import nitnc.kotanilab.trainer.gl.util.Position;
import nitnc.kotanilab.trainer.gl.style.Style;
import nitnc.kotanilab.trainer.gl.util.Vector;

import java.awt.*;

/**
 * 木構造のノードのスーパークラスです。
 * 親を持ち子は持ちません。
 * OpenGLの描画に使用するユーティリティメソッドをいくつかとスタイルシートを持ちます。
 */
public abstract class Node {

    protected Parent parent = Parent.dummy;
    protected Style style = new Style(this);
    protected GL2 gl = null;

    /**
     * コンストラクタです。
     */
    public Node() {
    }

    /**
     * 指定したスタイルで作成します。
     *
     * @param style スタイルシート
     */
    public Node(String style) {
        this.getStyle().put(style);
    }

    /**
     * 描画
     */
    public abstract void draw();

    /**
     * スタイルシートを返します。
     *
     * @return スタイルシート
     */
    public Style getStyle() {
        return style;
    }

    /**
     * 親ノードを返します。
     *
     * @return 親ノード
     */
    public Node getParent() {
        return parent;
    }

    /**
     * 描画中かどうかのロックを取得します。
     *
     * @return 描画中かどうかのロック
     */
    public Object getLock() {
        return getRoot().getLock();
    }

    /**
     * ルートノード(Windowクラス)を返します。
     *
     * @return ルートノード
     */
    public Window getRoot() {
        if (parent == null) throw new IllegalStateException("親を持ちません。");
        return parent.getRoot();
    }

    /**
     * 親ノードをセットします。
     *
     * @param parent 親ノード
     */
    public void setParent(Parent parent) {
        this.parent = parent;
    }

    /**
     * 親ノードから離します。
     */
    public void removeParent() {
        this.parent = Parent.dummy;
    }

    /**
     * ルートノードからOpenGLの描画に使用するGLAutoDrawableを取得します。
     *
     * @return OpenGLの描画に使用するGLAutoDrawable
     */
    protected GLAutoDrawable getDrawable() {
        return getRoot().getDrawable();
    }

    /**
     * OpenGLの線の太さをセットします。
     *
     * @param thickness 線の太さ
     */
    protected void setThickness(double thickness) {
        gl.glLineWidth((float) thickness);
    }

    /**
     * OpenGLの描画モードをセットします。
     *
     * @param mode 描画モード
     */
    protected void setMode(int mode) {
        gl.glBegin(mode);
    }

    /**
     * OpenGLの描画色をセットします。
     *
     * @param color 色
     */
    protected void setColor(Color color) {
        gl.glColor3d(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0);
    }

    /**
     * 2次元でVectorオブジェクトを用いて頂点をセットします。
     *
     * @param vector 頂点座標
     */
    protected void setVertex(Vector vector) {
        Vector absVector = calcAbsVector(vector);
        gl.glVertex2d(absVector.getX(), absVector.getY());
    }

    /**
     * 2次元でx座標とy座標を用いて頂点をセットします。
     *
     * @param x x座標
     * @param y y座標
     */
    protected void setVertex(double x, double y) {
        Vector absVector = calcAbsVector(new Vector(x, y));
        gl.glVertex2d(absVector.getX(), absVector.getY());
    }

    /**
     * OpenGLの描画を終了します。
     */
    protected void end() {
        gl.glEnd();
    }

    /**
     * 原点が中央の座標を、端を0、もう片方の端を指定値としたさいの座標に変換します。
     *
     * @param pos 原点が中央の座標
     * @param max 端の指定値
     * @return 変換した座標
     */
    protected static double calcAbsPosition(double pos, double max) {
        return ((pos + 1.0) * max / 2.0);
    }

    /**
     * 相対座標(現在の親に対する座標)から絶対座標(ルートノードに対する座標、すなわち描画に使用する座標)を計算します。
     *
     * @param rltVector 相対座標
     * @return 絶対座標
     */
    protected Vector calcAbsVector(Vector rltVector) {
        Position position = parent.getPosition();
        return new Vector(rltVector.getX() * position.getXScale() + position.getXOffset(),
                rltVector.getY() * position.getYScale() + position.getYOffset());
    }

    /**
     * 座標と最大ピクセル数(ウィンドウ幅・高さ)からその座標でのピクセル数を計算します。
     *
     * @param pos 座標
     * @param max 最大ピクセル数
     * @return その座標でのピクセル数
     */
    protected static int calcPx(double pos, double max) {
        return (int) Math.round(((pos + 1.0) * max / 2.0));
    }

    /**
     * 現在の画面幅をピクセル数で取得します。
     *
     * @return 画面幅
     */
    public int getWindowWidth() {
        return this.getDrawable().getSurfaceWidth();
    }

    /**
     * 現在の画面高をピクセル数で取得します。
     *
     * @return 画面高
     */
    public int getWindowHeight() {
        return this.getDrawable().getSurfaceHeight();
    }

    /**
     * スタイルシートのこのオブジェクトの幅を返します。
     *
     * @return オブジェクトの幅
     */
    public abstract double getWidth();

    /**
     * スタイルシートのこのオブジェクトの高さを返します。
     *
     * @return オブジェクトの高さ
     */
    public abstract double getHeight();
}

