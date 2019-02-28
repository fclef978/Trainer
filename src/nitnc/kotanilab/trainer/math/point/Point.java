package nitnc.kotanilab.trainer.math.point;

import nitnc.kotanilab.trainer.fft.wrapper.Complex;

import java.util.function.UnaryOperator;

/**
 * 二次元で実数の点データのクラスです。
 */
public class Point extends AbstractPoint<Double, Double> {
    /**
     * コンストラクタです。
     *
     * @param x xの値
     * @param y yの値
     */
    public Point(double x, double y) {
        super(x, y);
    }

    /**
     * 指定した数だけxをずらします。
     * x += val
     *
     * @param val ずらす量
     */
    public void shiftX(Double val) {
        this.x += val;
    }

    /**
     * xの符号を反転します。
     */
    public void inverseSignX() {
        this.x *= -1;
    }

    @Override
    public String toString() {
        return "x=" + x +
                ", y=" + y;
    }

    @Override
    public Point clone() {
        Point other = null;

        try {
            other = new Point(x, y);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return other;
    }
}
