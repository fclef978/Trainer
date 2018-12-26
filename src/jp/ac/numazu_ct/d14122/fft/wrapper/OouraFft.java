package jp.ac.numazu_ct.d14122.fft.wrapper;

import jp.ac.numazu_ct.d14122.math.point.PointOfSpectrum;
import jp.ac.numazu_ct.d14122.math.series.Spectrum;
import jp.ac.numazu_ct.d14122.math.series.Wave;
import jp.ac.numazu_ct.d14122.fft.jnaNative.OouraFftLibrary;

/**
 * 大浦版FFTライブラリのラッパクラスです。
 * FFTインターフェースを実装しています。
 */
public class OouraFft implements Fft {

    private OouraFftLibrary instance = OouraFftLibrary.INSTANCE;

    private int length;
    private int[] work;
    private double[] tri;
    private int n;

    /**
     * 指定されたデータ長でFFTの準備を行います。
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
    public Complex[] cdft(Complex[] signal) {
        if (signal.length < length)
            throw new IllegalArgumentException("信号の長さが足りません " + signal.length + " 必要数 " + length);
        double[] input = new double[n];
        Complex[] output = new Complex[length];

        for (int i = 0; i < length; i++) {
            input[2 * i] = signal[i].re;
            input[2 * i + 1] = signal[i].im;
        }

        instance.cdft(length * 2, -1, input, work, tri);

        for (int i = 0; i < length; i++) {
            output[i].re = input[2 * i];
            output[i].im = input[2 * i + 1];
        }

        return output;
    }

    @Override
    public Spectrum rdft(Wave wave) {
        if (wave.size() < length) throw new IllegalArgumentException("信号の長さが足りません");
        double[] input = new double[n];
        Spectrum output = new Spectrum(wave, length);
        int spectrumLength = length / 2;

        for (int i = 0; i < length; i++) {
            input[2 * i] = wave.get(i).getY();
            input[2 * i + 1] = 0.0;
        }

        instance.cdft(length * 2, -1, input, work, tri);

        double maxFrequency = wave.getSamplingFrequency() / 2.0;
        for (int i = 1; i < spectrumLength + 1; i++) {
            output.add(
                    new PointOfSpectrum(
                            i * maxFrequency / (spectrumLength),
                            new Complex(input[2 * i] / length * 2, -input[2 * i + 1] / length * 2)
                    )
            );
        }

        return output;
    }

    @Override
    public int getLength() {
        return n;
    }

    /**
     * 2の累乗であるか確認します。
     * @param x 対象
     * @return 2の累乗だったらtrue
     */
    private static boolean isPowerOf2(int x) {
        return (x & (x - 1)) == 0;
    }
}
