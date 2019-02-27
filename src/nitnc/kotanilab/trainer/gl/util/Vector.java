package nitnc.kotanilab.trainer.gl.util;

/**
 * OpenGLの描画で座標を表す位置ベクトルのクラスです。
 */
public class Vector {
    private double x;
    private double y;

    /**
     * 指定した値で作成します。
     *
     * @param x X(横軸)の値
     * @param y Y(縦軸)の値
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * 指定した値がtrueならXの値の符号を反転します。
     *
     * @param inverse Xの値の符号を反転するかどうか
     * @return このVector
     */
    public Vector inverseX(boolean inverse) {
        x *= inverse ? -1 : 1;
        return this;
    }

    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
