package nitnc.kotanilab.trainer.math.series;

import nitnc.kotanilab.trainer.fft.wrapper.Complex;
import nitnc.kotanilab.trainer.math.Unit;
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

    public Spectrum from(SeriesStream<? extends Complex> stream) {
        Spectrum wave = new Spectrum(yMax, yMin, xUnit, yUnit, stream.count(), samplingFrequency, startTime);
        stream.each((x, y) -> wave.add(new ComplexPoint(x, y)));
        return wave;
    }

    public Signal<Double, Point> getPowerSpectrum() {
        List<Point> tmp = stream().cutUp(this.size() / 2 + 1).combine((x, y) -> new Point(x, y.getPower()));
        return new Signal<>(tmp, yMax.getAbs(), yMin.getAbs(), xUnit, yUnit, samplingFrequency, startTime);
    }

}
