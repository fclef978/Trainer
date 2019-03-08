package nitnc.kotanilab.trainer.fft.wrapper;

import nitnc.kotanilab.trainer.math.series.Spectrum;
import nitnc.kotanilab.trainer.math.series.Wave;

/**
 * FFTを計算するインターフェースです。
 */
public interface Fft {
    /**
     * 複素離散フーリエ変換です。
     * Complex型配列を用います。
     *
     * @param signal 入力波形です。
     * @return 出力スペクトルです。
     */
    Complex[] dft(Complex[] signal);

    /**
     * 実離散フーリエ変換です。
     * 入力にWave、出力にSpectrumを用います。
     * 入力のWaveは複素数の実部として使用され、虚部は0として変換します。
     *
     * @param wave 入力波形です。
     * @return 出力スペクトルです。
     */
    Spectrum dft(Wave wave);

    /**
     * 逆離散フーリエ変換です。
     * Complex型配列を用います。
     *
     * @param signal 入力スペクトラムです。
     * @return 出力波形です。
     */
    Complex[] idft(Complex[] signal);

    /**
     * 逆離散フーリエ変換です。
     * 入力にSpectrum、出力にWaveを用います。
     * 出力のWaveは絶対値が入ります。
     *
     * @param signal 入力スペクトラムです。
     * @return 出力波形です。
     */
    Wave idft(Spectrum signal);

    /**
     * FFTの長さを返します。
     *
     * @return FFTの長さ
     */
    int getLength();
}
