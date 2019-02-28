package nitnc.kotanilab.trainer.math.point;

import nitnc.kotanilab.trainer.fft.wrapper.Complex;

import java.util.function.ToDoubleFunction;

/**
 * x軸が実数、y軸が複素数になる二次元点データクラスです。
 */
public class ComplexPoint extends AbstractPoint<Double, Complex> {

    /**
     * コンストラクタです。
     *
     * @param x xの値
     * @param y yの複素数の値
     */
    public ComplexPoint(Double x, Complex y) {
        super(x, y);
    }

    /**
     * yの絶対値を返します。
     *
     * @return yの絶対値
     */
    public double getAbsY() {
        return this.y.getAbs();
    }

    /**
     * yが絶対値のPointを返します。
     *
     * @return yが絶対値のPoint
     */
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
