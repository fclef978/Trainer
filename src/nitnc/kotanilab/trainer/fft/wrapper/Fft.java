package nitnc.kotanilab.trainer.fft.wrapper;

import nitnc.kotanilab.trainer.math.series.Spectrum;
import nitnc.kotanilab.trainer.math.series.Wave;

/**
 * FFTを計算するインターフェースです。
 */
public interface Fft {
    /**
     * 複素離散フーリエ変換です。
     *
     * @param signal 入力波形です。
     * @return 出力スペクトルです。
     */
    public Complex[] cdft(Complex[] signal);

    /**
     * 実離散フーリエ変換です。
     *
     * @param wave 入力波形です。
     * @return 出力スペクトルです。
     */
    public Spectrum rdft(Wave wave);

    /**
     * FFTの長さを返します。
     *
     * @return FFTの長さ
     */
    public int getLength();
}
