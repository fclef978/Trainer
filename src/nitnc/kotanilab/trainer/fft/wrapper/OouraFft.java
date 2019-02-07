package nitnc.kotanilab.trainer.fft.wrapper;

import nitnc.kotanilab.trainer.main.ACF;
import nitnc.kotanilab.trainer.math.point.Point;
import nitnc.kotanilab.trainer.math.point.PointOfSpectrum;
import nitnc.kotanilab.trainer.math.series.Spectrum;
import nitnc.kotanilab.trainer.math.series.Wave;
import nitnc.kotanilab.trainer.fft.jnaNative.OouraFftLibrary;
import nitnc.kotanilab.trainer.util.CsvLogger;
import nitnc.kotanilab.trainer.util.Dbg;

import java.util.Arrays;
import java.util.List;

/**
 * 大浦版FFTライブラリのラッパクラスです。
 * FFTインターフェースを実装しています。
 */
public class OouraFft implements Fft {

    private final OouraFftLibrary instance = OouraFftLibrary.INSTANCE;

    private final int length;
    private final int[] work;
    private final double[] tri;
    private final int n;

    /**
     * 指定されたデータ長でFFTの準備を行います。
     *
     * @param length データ長です。
     */
    public OouraFft(int length) {
        if (!isPowerOf2(length)) throw new IllegalArgumentException("基数は2のべき乗である必要があります");
        this.length = length;
        n = length * 2;
        work = new int[2 + (int) Math.sqrt(length)];
        tri = new double[length / 2];
        work[0] = 0;

        // ライブラリの初期化
        instance.cdft(n, -1, new double[n], work, tri);
    }

    @Override
    public Complex[] dft(Complex[] signal) {
        if (signal.length < length)
            throw new IllegalArgumentException("信号の長さが足りません " + signal.length + " 必要数 " + length);
        double[] input = new double[n];
        Complex[] output = new Complex[length];

        for (int i = 0; i < length; i++) {
            input[2 * i] = signal[i].re;
            input[2 * i + 1] = signal[i].im;
        }

        instance.cdft(n, -1, input, work, tri);

        for (int i = 0; i < length; i++) {
            output[i] = new Complex(input[2 * i], input[2 * i + 1]);
        }

        return output;
    }

    @Override
    public Spectrum dft(Wave wave) {
        if (wave.size() < length)
            throw new IllegalArgumentException("信号の長さが足りません " + wave.size() + " 必要数 " + length);
        double[] input = new double[n];
        Spectrum output = new Spectrum(wave, length);
        int spectrumLength = length / 2;

        for (int i = 0; i < length; i++) {
            input[2 * i] = wave.get(i).getY();
            input[2 * i + 1] = 0.0;
        }

        instance.cdft(n, -1, input, work, tri);

        double maxFrequency = wave.getSamplingFrequency() / 2.0;
        for (int i = 0; i < length; i++) {
            output.add(
                    new PointOfSpectrum(
                            i * maxFrequency / (spectrumLength),
                            new Complex(input[2 * i] / length * 2, input[2 * i + 1] / length * 2)
                    )
            );
        }

        return output;
    }

    @Override
    public Complex[] idft(Complex[] signal) {
        if (signal.length != length)
            throw new IllegalArgumentException("信号の長さが不正です " + signal.length + " 必要数 " + length);
        double[] input = new double[n];
        Complex[] output = new Complex[length];

        for (int i = 0; i < length; i++) {
            input[2 * i] = signal[i].re;
            input[2 * i + 1] = signal[i].im;
        }

        instance.cdft(length * 2, 1, input, work, tri);

        for (int i = 0; i < length; i++) {
            output[i] = new Complex(input[2 * i] / 2, input[2 * i + 1] / 2);
        }

        return output;
    }

    @Override
    public int getLength() {
        return length;
    }

    /**
     * 2の累乗であるか確認します。
     *
     * @param x 数
     * @return 2の累乗だったらtrue
     */
    private static boolean isPowerOf2(int x) {
        return (x & (x - 1)) == 0;
    }

}
