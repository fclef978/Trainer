package nitnc.kotanilab.trainer.math;


import nitnc.kotanilab.trainer.math.point.PointOfWave;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * 任意の関数から実際の時間をX値として値を生成するクラスです。
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
     * 任意の複数の周波数が重畳された、振幅、切片で時間[s]を引数とするサイン関数オブジェクトを生成します。
     *
     * @param amp  振幅
     * @param bias 切片
     * @param freq 周波数
     * @return サイン波
     */
    public static UnaryOperator<Double> sin(double amp, double bias, double... freq) {
        return t -> {
            double ret = 0.0;
            for (int i = 0; i < freq.length; i++) {
                ret += amp * Math.sin(2.0 * Math.PI * freq[i] * t);
            }
            return ret + bias;
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
     * @return ランダム波
     */
    public static UnaryOperator<Double> rand(double amp, double bias) {
        return t -> amp * 2.0 * (Math.random() - 0.5) + bias;
    }

    /**
     * 任意の振幅、切片で時間[s]を引数とするホワイトノイズオブジェクトを生成します。
     *
     * @param amp  振幅
     * @param bias 切片
     * @return ホワイトノイズ
     */
    public static UnaryOperator<Double> white(double amp, double bias) {
        return t -> {
            double z = Math.sqrt(-2.0 * Math.log(Math.random())) * Math.sin(2.0 * Math.PI * Math.random());
            return bias + amp / 3.0 * z;
        };
    }

    /**
     * 指定されたCSVファイルを読み込んでそのデータをもとにする関数を返します。
     * CSVの形式は、
     * 時間[s],値\n
     * で、値以外の行は含まれてはいけません。
     * 渡されたX値に、より近いX値を持つデータ点のY値が返ります。補完は行われません。
     * そのため、CSVデータのサンプル間隔よりも短いX値の間隔で結果の関数を呼び出さないでください。
     * @param filename ファイル名
     *                 絶対パスまたは実行場所からの相対パス
     * @return 指定されたCSVファイルをもとにする関数
     */
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

    /**
     * コンストラクタです。
     * @param function Xを受け取りYを返す関数
     */
    public FunctionGenerator(UnaryOperator<Double> function) {
        this.function = function;
    }

    private void start() {
        startTime = System.nanoTime();
    }

    /**
     * 関数に渡されるXを次のgenerate()呼び出し時に0にリセットします。
     */
    public void reset() {
        first = true;
    }

    /**
     * 点データの形で値を生成します。
     * 呼び出された時の時間から基準時間を引いた値が関数に渡り、実際の時間に関数の値が準拠します。
     * 初回呼び出し時やreset()が呼ばれた直後は基準時間がそのときの時間になり、関数に渡る値が0になります。
     * @return 点データ
     */
    public PointOfWave generate() {
        if (first) {
            start();
            first = false;
        }
        double delta = (System.nanoTime() - startTime) / 1000000000.0;
        return new PointOfWave(delta, function.apply(delta));
    }
}
