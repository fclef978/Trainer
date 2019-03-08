package nitnc.kotanilab.trainer.math;

import nitnc.kotanilab.trainer.math.point.PointOfWave;
import nitnc.kotanilab.trainer.math.series.Wave;

import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.DoubleUnaryOperator;

/**
 * 波形を保持するバッファです。
 * データの前処理やデシメーションに対応します。
 * スレッドセーフです(たぶん)
 */
public class WaveBuffer {

    private double xMax = 11;

    private Unit yUnit;
    private int decimationNumber = 1; // 間引き量
    private double samplingFrequency;
    private double yMax;
    private double yMin;
    private BlockingDeque<Double> queue = new LinkedBlockingDeque<>();
    private long totalCount = 0;
    private boolean isStart = false;
    private List<DoubleUnaryOperator> callbacks = new ArrayList<>();

    /**
     * コンストラクタです。
     *
     * @param yMax              最大値
     * @param yMin               最小値
     * @param yUnit             y軸単位
     * @param samplingFrequency サンプリング周波数
     */
    public WaveBuffer(double yMax, double yMin, Unit yUnit, double samplingFrequency) {
        this.samplingFrequency = samplingFrequency;
        this.yMax = yMax;
        this.yMin = yMin;
        this.yUnit = yUnit;
    }

    /**
     * Y軸単位を任意単位で作成します。
     *
     * @param max               最大値
     * @param min               最小値
     * @param samplingFrequency サンプリング周波数
     */
    public WaveBuffer(double max, double min, double samplingFrequency) {
        this(max, min, Unit.arb("amplitude"), samplingFrequency);
    }

    /**
     * バッファから指定した秒数分取り出します。
     * 指定した秒数に足りない場合は少ないまま返ります。
     *
     * @param sec 取り出したい秒数
     * @return 取り出したWave
     */
    public Wave getWave(double sec) {
        return getWave((int) Math.round(sec * samplingFrequency));
    }

    /**
     * バッファから任意の最大データ長でWaveをとりだします。
     * バッファに指定した長さ以上のデータがある場合はバッファの最後の値からmasNumber分のデータを持つWaveが返ります。
     * 指定した長さに足りない場合は少ないまま返ります。そのため、available()を使って最小長がいくつになるか確認してください。
     *
     * @param maxNumber 取り出す最大長
     * @return 取り出したWave
     */
    public Wave getWave(int maxNumber) {
        // キューの長さを制限する
        while (queue.size() > Math.ceil(xMax * samplingFrequency)) {
            try {
                queue.takeFirst();
                totalCount++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // イテレータを生成する。このイテレータは生成時点のキューの内容を元に作られる。
        // このとき、サイズは可能な限りイテレータ生成時点に近いタイミングでとらなければならない。
        int size = queue.size();
        int tmp = size < maxNumber ? 0 : size - maxNumber;
        double startTime = (size + totalCount) / samplingFrequency; // 数える用
        Wave wave = new Wave(maxNumber, yMax, yMin, yUnit, samplingFrequency, startTime);
        Iterator<Double> iterator = queue.iterator();

        int count = 0;
        while (iterator.hasNext()) {
            if (count < tmp)
                iterator.next();
            else
                wave.add(new PointOfWave((count - tmp) / samplingFrequency, iterator.next()));
            count++;
        }

        return wave;
    }

    /**
     * put()が呼び出される際に追加される値に対して適用される演算子を追加します。
     * 演算子の実行順序は演算子の追加順です。
     *
     * @param callback put()が呼び出される際に追加される値に対して適用される演算子
     */
    public void addCallback(DoubleUnaryOperator callback) {
        callbacks.add(callback);
    }

    /**
     * 指定されたListに含まれるput()が呼び出される際に追加される値に対して適用される演算子を全て追加します。
     * Listの順番どおりに追加されます。
     * 演算子の実行順序は演算子の追加順です。
     *
     * @param callbacks put()が呼び出される際に追加される値に対して適用される演算子を含むList
     */
    public void addAllCallback(List<DoubleUnaryOperator> callbacks) {
        this.callbacks.addAll(callbacks);
    }

    private int decimationCount = 0;

    /**
     * キューにデータを挿入します。
     *
     * @param val 挿入する波データ
     * @throws InterruptedException もしかしたらスレッド衝突するかもしれない
     */
    public void put(Double val) throws InterruptedException {
        if (!isStart) return;
        for (DoubleUnaryOperator callback : callbacks) {
            val = callback.applyAsDouble(val);
        }
        if (decimationCount % decimationNumber == 0) {
            queue.put(val);
        }
        decimationCount++;
    }

    /**
     * キューに何秒分のデータが入っているかを返します。
     *
     * @return キューに入っているデータの時間
     */
    public double available() {
        return queue.size() / samplingFrequency;
    }

    /**
     * キューに指定した時間以上のデータが入っているかを返します。
     *
     * @param sec 時間
     * @return キューにsec秒以上のデータが入っているかどうか
     */
    public boolean available(double sec) {
        return available() >= sec;
    }

    /**
     * キューに指定した長さ以上のデータが入っているかを返します。
     *
     * @param length 長さ
     * @return キューにlength以上のデータが入っているかどうか
     */
    public boolean available(int length) {
        return queue.size() >= length;
    }

    /**
     * バッファリングを開始します。
     * put()やgetWave()を呼ぶ前に呼び出してください。
     */
    public void start() {
        isStart = true;
        decimationCount = 0;
    }

    /**
     * バッファリングを終了します。
     * 再利用する際は必ずサンプリング終了後に呼び出してください。
     */
    public void stop() {
        isStart = false;
        totalCount = 0;
        queue.clear();
    }

    /**
     * データの間引き(デシメーション)点数を設定します。デフォルトは1(データ点数1/1、すなわち間引きなし)です。
     * 間引きをかける場合は間引き後のナイキスト周波数以下の周波数成分を事前にカットする必要があるため、コールバックに対応したローパスフィルタを追加してください。
     *
     * @param decimationNumber 間引き点数
     */
    public void setDecimationNumber(int decimationNumber) {
        this.decimationNumber = decimationNumber;
        this.samplingFrequency /= decimationNumber;
    }

    public double getSamplingFrequency() {
        return samplingFrequency;
    }

    /**
     * キューに貯めておく最大秒数をセットします。
     * デフォルトは10秒です。
     *
     * @param xMax キューに貯めておく最大秒数
     */
    public void setXMax(double xMax) {
        this.xMax = xMax;
    }
}
