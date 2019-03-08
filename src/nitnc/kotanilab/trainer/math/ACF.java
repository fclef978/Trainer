package nitnc.kotanilab.trainer.math;

import nitnc.kotanilab.trainer.fft.wrapper.Complex;
import nitnc.kotanilab.trainer.fft.wrapper.Fft;
import nitnc.kotanilab.trainer.fft.wrapper.OouraFft;
import nitnc.kotanilab.trainer.math.point.AbstractPoint;
import nitnc.kotanilab.trainer.math.series.Series;
import nitnc.kotanilab.trainer.math.series.SeriesImpl;
import nitnc.kotanilab.trainer.math.series.Spectrum;
import nitnc.kotanilab.trainer.math.series.Wave;
import nitnc.kotanilab.trainer.util.Dbg;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自己相関関数に関するstaticメソッドだけで構成されるクラスです。
 */
public class ACF {

    /**
     * ウィーナー＝ヒンチンの定理に基づいて自己相関関数を計算します。
     * データ点数は2の累乗でなければなりません
     *
     * @param fft フーリエ変換に用いるFFTクラス
     * @param x   変換する信号
     * @return 自己相関関数
     */
    public static Wave wienerKhinchin(Fft fft, Wave x) {
        int size = fft.getLength();
        // Complex[] a = new Complex[size], b, c = new Complex[size], d;
        Spectrum a;
        Spectrum b;
        Wave ret;
        double[] c = new double[size];
        double[] f = new double[size / 2];
        for (int i = 0; i < size; i++) {
            // a[i] = new Complex(x.get(i), 0);
        }
        a = fft.dft(x);
        b = a.stream().replaceY(y -> new Complex(y.getAbs(), 0)).to(a::regenerate);
        ret = fft.idft(b);
        return ret;
    }
}
