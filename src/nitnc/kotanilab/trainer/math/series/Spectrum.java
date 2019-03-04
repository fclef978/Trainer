package nitnc.kotanilab.trainer.math.series;

import nitnc.kotanilab.trainer.fft.wrapper.Complex;
import nitnc.kotanilab.trainer.math.Unit;
import nitnc.kotanilab.trainer.math.point.ComplexPoint;
import nitnc.kotanilab.trainer.math.point.Point;

import java.util.List;

/**
 * スペクトラムです。
 */
public class Spectrum extends Signal<Complex, ComplexPoint> {

    /**
     * 指定したWaveの物理的情報やサンプリング情報を持つ空のSpectrumを作成します。
     *
     * @param wave           情報を持つWave
     * @param samplingNumber DFTした際のサンプリング数
     */
    public Spectrum(Wave wave, int samplingNumber) {
        super(samplingNumber, new Complex(wave.getYMax()), new Complex(), Unit.hz(), wave.getYUnit(), wave.getSamplingFrequency(), wave.getStartTime());
    }

    /**
     * 空のSpectrumを作成します。
     *
     * @param yMax              yの最大値
     * @param yMin              yの最小値
     * @param xUnit             xの単位
     * @param yUnit             yの単位
     * @param samplingNumber    DFTした際のサンプリング数
     * @param samplingFrequency 　サンプリング周波数
     * @param startTime         サンプリング開始時刻
     */
    public Spectrum(Complex yMax, Complex yMin, Unit xUnit, Unit yUnit, int samplingNumber, double samplingFrequency, double startTime) {
        super(samplingNumber, yMax, yMin, xUnit, yUnit, samplingFrequency, startTime);
    }

    /**
     * このSpectrumのパワースペクトラムを返します。
     *
     * @return このSpectrumのパワースペクトラム
     */
    public Signal<Double, Point> getPowerSpectrum() {
        List<Point> tmp = stream().cutUp(this.size() / 2 + 1).combine((x, y) -> new Point(x, y.getPower()));
        return new Signal<>(tmp, yMax.getAbs(), yMin.getAbs(), xUnit, yUnit, samplingFrequency, startTime);
    }

}
