package nitnc.kotanilab.trainer.math;

/**
 * Created by Hirokazu SUZUKI on 2018/07/30.
 */


import nitnc.kotanilab.trainer.math.point.PointOfWave;
import nitnc.kotanilab.trainer.util.Dbg;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.UnaryOperator;

/**
 * Created by Hirokazu SUZUKI on 2018/07/16.
 * 関数発生クラス
 */
public class FunctionGenerator {

    /**
     * 任意の周波数、振幅、切片で時間[s]を引数とするサイン関数オブジェクトを生成します。
     *
     * @param freq 周波数
     * @param amp  振幅
     * @param bias 切片
     * @return サイン波
     */
    public static UnaryOperator<Double> sin(double freq, double amp, double bias) {
        return t -> amp * Math.sin(2.0 * Math.PI * freq * t) + bias;
    }

    /**
     * 任意の周波数、振幅、切片で時間[s]を引数とするサイン関数オブジェクトを生成します。
     *
     * @param freq 周波数
     * @param amp  振幅
     * @param bias 切片
     * @return サイン波
     */
    public static UnaryOperator<Double> sin(double amp, double bias, double... freq) {
        return t -> {
            double ret = 0.0;
            for (double f : freq) {
                ret += amp * Math.sin(2.0 * Math.PI * f * t) + bias;
            }
            return ret;
        };
    }

    /**
     * 任意の周波数、振幅、切片で時間[s]を引数とする矩形波オブジェクトを生成します。
     *
     * @param freq 周波数
     * @param amp  振幅
     * @param bias 切片
     * @return 矩形波
     */
    public static UnaryOperator<Double> squa(double freq, double amp, double bias) {
        return t -> Math.sin(2.0 * Math.PI * freq * t) < 0.0 ? -amp + bias : amp + bias;
    }

    /**
     * 任意の周波数、振幅、切片で時間[s]を引数とする正弦波と余弦波の積オブジェクトを生成します。
     *
     * @param freq
     * @param amp
     * @param bias
     * @return
     */
    public static UnaryOperator<Double> cosSin(double freq, double amp, double bias) {
        return t -> amp * Math.sin(2.0 * Math.PI * freq * t) * Math.cos(2.0 * Math.PI * freq / 5.0 * t) + bias;
    }

    /**
     * 任意の振幅、切片で時間[s]を引数とするランダム波オブジェクトを生成します。
     *
     * @param amp  振幅
     * @param bias 切片
     * @return 矩形波
     */
    public static UnaryOperator<Double> rand(double amp, double bias) {
        return t -> amp * 2.0 * (Math.random() - 0.5) + bias;
    }

    /**
     * 任意の振幅、切片で時間[s]を引数とするホワイトノイズオブジェクトを生成します。
     *
     * @param amp  振幅
     * @param bias 切片
     * @return 矩形波
     */
    public static UnaryOperator<Double> white(double amp, double bias) {
        return t -> {
            double z = Math.sqrt(-2.0 * Math.log(Math.random())) * Math.sin(2.0 * Math.PI * Math.random());
            return bias + amp / 3.0 * z;
        };
    }

    /**
     * 任意の振幅、切片で時間[s]を引数とするホワイトノイズオブジェクトを生成します。
     *
     * @param amp  振幅
     * @param bias 切片
     * @return 矩形波
     */
    public static UnaryOperator<Double> white(double freq, int num, double amp, double bias) {
        double[] phase = new double[num];
        for (int i = 0; i < phase.length; i++) {
            phase[i] = 2 * Math.PI * Math.random();
        }
        return t -> {
            double y = 0;
            for (int i = 0; i < num; i++) {
                double f = freq * (double) i / (double) num;
                y += Math.sin(2.0 * Math.PI * f * t + phase[i]);
            }
            return amp * y / num + bias;
        };
    }

    /**
     * 任意の振幅、切片で時間[s]を引数とするホワイトノイズオブジェクトを生成します。
     *
     * @param amp  振幅
     * @param bias 切片
     * @return 矩形波
     */
    public static UnaryOperator<Double> white(int min, int max, double amp, double bias) {
        int n = 10 * (max - min);
        double[] phase = new double[n];
        for (int i = 0; i < phase.length; i++) {
            phase[i] = 2 * Math.PI * Math.random();
        }
        return t -> {
            double y = 0;
            int k = 0;
            for (int i = min; i < max; i++) {
                for (int j = 1; j < 10; j++) {
                    double f = j * Math.pow(10, i);
                    y += Math.sin(2.0 * Math.PI * f * t + phase[k]);
                    k++;
                }
            }
            return amp * y / n + bias;
        };
    }

    public static UnaryOperator<Double> csv(String filename) {
        UnaryOperator<Double> ret;
        try {
            FileInputStream fi = new FileInputStream(filename);
            InputStreamReader is = new InputStreamReader(fi);
            BufferedReader br = new BufferedReader(is);

            List<Double> values = new ArrayList<>();
            String line;
            double dxSum = 0.0;
            double xPrevious = -1.0;
            while ((line = br.readLine()) != null) {
                double x = Double.parseDouble(line.split(",")[0]);
                if (xPrevious >= 0) {
                    dxSum += x - xPrevious;
                }
                xPrevious = x;
                values.add(Double.parseDouble(line.split(",")[1]));
            }

            final double dxAvg = dxSum / (values.size() - 1);
            final double xMax = xPrevious;
            ret = t -> {
                int i = (int) (Math.round(t / dxAvg) % values.size());
                return values.get(i);
            };
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("正しいCSVではありません");
        }
        return ret;
    }

    private long startTime = -1;
    private UnaryOperator<Double> function;
    private boolean first = true;

    public FunctionGenerator(UnaryOperator<Double> function) {
        this.function = function;
    }

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        startTime = -1;
    }

    public boolean isOscillating() {
        return !(startTime == -1);
    }

    public PointOfWave generate() {
        if (first) {
            start();
            first = false;
        }
        double delta = (System.nanoTime() - startTime) / 1000000000.0;
        return new PointOfWave(delta, function.apply(delta));
    }
}
