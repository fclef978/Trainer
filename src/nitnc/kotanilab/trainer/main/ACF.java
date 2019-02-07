package nitnc.kotanilab.trainer.main;

import nitnc.kotanilab.trainer.fft.wrapper.Complex;
import nitnc.kotanilab.trainer.fft.wrapper.Fft;
import nitnc.kotanilab.trainer.fft.wrapper.OouraFft;
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
     * ウィーナー＝ヒンチンの定理に基づいて計算します。
     * データ点数は2の累乗でなければなりません
     *
     * @param fft フーリエ変換に用いるFFTクラス
     * @param x   変換する信号
     * @return 自己相関関数
     */
    public static double[] wienerKhinchin(Fft fft, List<Double> x) {
        int size = fft.getLength();
        Complex[] a = new Complex[size], b, c = new Complex[size], d;
        double[] f = new double[size / 2];
        for (int i = 0; i < size; i++) {
            a[i] = new Complex(x.get(i), 0);
        }
        b = fft.dft(a);
        for (int i = 0; i < size; i++) {
            c[i] = new Complex(b[i].getPower(), 0);
        }
        d = fft.idft(c);
        for (int i = 0; i < f.length; i++) {
            f[i] = d[i].getRe();
        }
        return f;
    }

    /**
     * データから最大の極大値のインデックスを返します。
     *
     * @param data 対象のデータ
     * @return 極大値のインデックス
     */
    public static int pickPeekIndex(double[] data) {
        List<Integer> indices = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        for (int i = 1; i < data.length - 1; i++) {
            if (data[i] > data[i - 1] && data[i] > data[i + 1]) {
                indices.add(i);
                values.add(data[i]);
            }
        }
        if (values.size() > 0) {
            double max = values.get(0);
            for (int i = 1; i < values.size(); i++) {
                double v = values.get(i);
                if (max < v) max = v;
            }
            int i = values.indexOf(max);
            return indices.get(i);
        } else {
            return 0;
        }
    }
}
