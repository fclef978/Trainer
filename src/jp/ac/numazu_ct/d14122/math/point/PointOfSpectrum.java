package jp.ac.numazu_ct.d14122.math.point;

import jp.ac.numazu_ct.d14122.fft.wrapper.Complex;

public class PointOfSpectrum extends ComplexPoint {

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
