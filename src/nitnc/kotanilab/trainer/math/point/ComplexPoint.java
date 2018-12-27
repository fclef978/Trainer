package nitnc.kotanilab.trainer.math.point;

import nitnc.kotanilab.trainer.fft.wrapper.Complex;

import java.util.function.ToDoubleFunction;

/**
 * Y軸が複素数になる二次元点データクラスです。
 */
public class ComplexPoint extends AbstractPoint<Double, Complex> {

    public ComplexPoint(Double x, Complex y) {
        super(x, y);
    }

    public double getAbsY() {
        return this.y.getAbs();
    }

    public Point getAbsPoint() {
        return new Point(x, getAbsY());
    }

    @Override
    public ComplexPoint clone() {
        ComplexPoint other = null;

        try {
            other = new ComplexPoint(x, y.clone());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return other;
    }
}
