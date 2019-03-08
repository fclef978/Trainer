package nitnc.kotanilab.trainer.math.series;

import nitnc.kotanilab.trainer.math.Unit;
import nitnc.kotanilab.trainer.math.point.Point;

import java.util.List;

/**
 * 波形クラス
 */
public class Wave extends Signal<Double, Point> implements Regenerable<Wave, Double> {

    /**
     * コンストラクタ
     *
     * @param samplingFrequency サンプリング周波数
     * @param max               最大値
     * @param min               最小値
     * @param yUnit             y軸単位
     * @param startTime         サンプリング開始時刻
     */
    public Wave(double max, double min, Unit yUnit, double samplingFrequency, double startTime) {
        super(max, min, Unit.sec(), yUnit, samplingFrequency, startTime);
    }

    /**
     * コンストラクタ
     *
     * @param max               最大値
     * @param min               最小値
     * @param samplingFrequency サンプリング周波数
     * @param startTime         サンプリング開始時刻
     */
    public Wave(double max, double min, double samplingFrequency, double startTime) {
        this(max, min, Unit.arb("amplitude"), samplingFrequency, startTime);
    }

    /**
     * コンストラクタ
     *
     * @param initCapacity      初期化サイズ
     * @param samplingFrequency サンプリング周波数
     * @param max               最大値
     * @param min               最小値
     * @param yUnit             y軸単位
     * @param startTime         サンプリング開始時刻
     */
    public Wave(int initCapacity, double max, double min, Unit yUnit, double samplingFrequency, double startTime) {
        super(initCapacity, max, min, Unit.sec(), yUnit, samplingFrequency, startTime);
    }

    /**
     * 指定したSpectrumの物理的情報やサンプリング情報を持つ空のWaveを作成します。
     * @param spectrum 情報を持つSpectrum
     * @param size 波形のデータの長さ
     */
    public Wave(Spectrum spectrum, int size) {
        this(size, spectrum.getYMax().getAbs(), spectrum.getYMin().getAbs(), spectrum.getXUnit(), spectrum.getSamplingFrequency(), spectrum.getStartTime());
    }

    public Wave from(SeriesStream<Double> stream) {
        Wave wave = new Wave(stream.count(), yMax, yMin, yUnit, samplingFrequency, startTime);
        stream.forEach((x, y) -> wave.add(new Point(x, y)));
        return wave;
    }

    @Override
    public Wave regenerate(SeriesStream<Double> stream) {
        Wave wave = new Wave(stream.count(), yMax, yMin, yUnit, samplingFrequency, startTime);
        stream.forEach((x, y) -> wave.add(new Point(x, y)));
        return wave;
    }

    @Override
    public Signal<Double, Point> from(List<Point> list) {
        return null;
    }

    /**
     * ハニング窓です。
     *
     * @param n      データのインデックス
     * @param length データの総数
     * @return 係数
     */
    public static double hanning(int n, int length) {
        return 0.50 - 0.50 * Math.cos(2 * Math.PI * n / length);
    }

    /**
     * ハミング窓です。
     *
     * @param n      データのインデックス
     * @param length データの総数
     * @return 係数
     */
    public static double hamming(int n, int length) {
        return 0.54 - 0.46 * Math.cos(2 * Math.PI * n / length);
    }

}
