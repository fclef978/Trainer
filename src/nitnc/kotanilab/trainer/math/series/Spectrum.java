package nitnc.kotanilab.trainer.math.series;

import nitnc.kotanilab.trainer.fft.wrapper.Complex;
import nitnc.kotanilab.trainer.math.point.ComplexPoint;
import nitnc.kotanilab.trainer.math.point.Point;

import java.util.List;

/**
 * スペクトラムです。
 */
public class Spectrum extends Signal<Complex,ComplexPoint> {

    public Spectrum(Wave wave, int samplingNumber) {
        super(samplingNumber, new Complex(wave.getYMax()), new Complex(), Unit.hz(), wave.getYUnit(), wave.getSamplingFrequency(), wave.getStartTime());
    }

    public Spectrum(Complex yMax, Complex yMin, Unit xUnit, Unit yUnit, int samplingNumber, double samplingFrequency, double startTime) {
        super(samplingNumber, yMax, yMin, xUnit, yUnit, samplingFrequency, startTime);
    }

    public Spectrum from(List<ComplexPoint> list) {
        Spectrum ret = new Spectrum(yMax, yMin, xUnit, yUnit, this.size(), samplingFrequency, startTime);
        ret.list = list;
        return ret;
    }

    public Signal<Double, Point> getPowerSpectrum() {
        List<Point> tmp = stream().toPointList((x, y) -> new Point(x, y.getAbs()));
        return new Signal<>(tmp, yMax.getAbs(), yMin.getAbs(), xUnit, yUnit, samplingFrequency, startTime);
    }

}
