package nitnc.kotanilab.trainer.main;

import nitnc.kotanilab.trainer.fft.wrapper.Complex;
import nitnc.kotanilab.trainer.fft.wrapper.Fft;
import nitnc.kotanilab.trainer.fft.wrapper.OouraFft;
import nitnc.kotanilab.trainer.util.Dbg;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ACF {

    public static int wienerKhinchin(Fft fft, List<Double> x) {
        int size = fft.getLength();
        Complex[] a = new Complex[size], b, c = new Complex[size], d;
        Double[] f = new Double[size / 2];
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
        return pickPeek(Arrays.asList(f));
    }

    public static List<Double> acf(List<Double> a) {
        List<Double> b = new ArrayList<>(a.size());

        for (int i = 0; i < a.size(); i++) {
            double tmp = 0;
            for (int j = 0; j < a.size(); j++) {
                tmp += a.get(j) * a.get((j + i) % a.size());
            }
            b.add(tmp);
        }

        return b;

        /*
        try {
            FileWriter filewriter = new FileWriter("data.csv");
            for (int i = 0; i < b.size(); i++) {
                filewriter.append(String.valueOf(a.get(i))).append(",").append(String.valueOf(b.get(i))).append("\n");
            }
            filewriter.close();

            System.out.println("テキストファイルの作成に成功しました");
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

    public static int pickPeek(List<Double> a) {
        List<Integer> index = new ArrayList<>();
        List<Double> value = new ArrayList<>();
        for (int i = 1; i < a.size() - 1; i++) {
            if (a.get(i) > a.get(i - 1) && a.get(i) > a.get(i + 1)) {
                index.add(i);
                value.add(a.get(i));
            }
        }
        if (value.size() > 0) {
            double max = value.get(0);
            for (int i = 1; i < value.size(); i++) {
                double v = value.get(i);
                if (max < v) max = v;
            }
            int i = value.indexOf(max);
            return index.get(i);
        } else {
            return 0;
        }
    }
}
