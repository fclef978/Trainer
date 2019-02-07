package nitnc.kotanilab.trainer.math.point;

import nitnc.kotanilab.trainer.fft.wrapper.Complex;

/**
 * Spectrumの点を表すクラスです。
 */
public class PointOfSpectrum extends ComplexPoint {

    /**
     * コンストラクタです。
     * @param x 周波数[Hz]
     * @param y 複素数の振幅
     */
    public PointOfSpectrum(double x, Complex y) {
        super(x, y);
    }

    public PointOfSpectrum clone() {
        PointOfSpectrum other = null;

        try {
            other = new  PointOfSpectrum(x, y);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return other;
    }
}
