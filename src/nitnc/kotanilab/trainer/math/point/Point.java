package nitnc.kotanilab.trainer.math.point;

import nitnc.kotanilab.trainer.fft.wrapper.Complex;

import java.util.function.UnaryOperator;

/**
 * 二次元で実数の点データです。
 */
public class Point extends AbstractPoint<Double, Double>{
    public Point(double x, double y) {
        super(x, y);
    }

    public Point mapY(UnaryOperator<Double> function) {
        return new Point(x, function.apply(y));
    }

    public void shiftX(Double val) {
        this.x += val;
    }

    public void mulX(Double val) {
        this.x *= val;
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
