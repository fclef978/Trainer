package jp.ac.numazu_ct.d14122.math;

import jp.ac.numazu_ct.d14122.math.point.PointOfWave;
import jp.ac.numazu_ct.d14122.math.series.Unit;
import jp.ac.numazu_ct.d14122.math.series.Wave;
import jp.ac.numazu_ct.d14122.util.Dbg;

import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 波形を保持するバッファです。
 * スレッドセーフです(たぶん)
 * Created by Hirokazu SUZUKI on 2018/07/31.
 */
public class WaveBuffer {

    private double xMax = 10;

    protected Unit yUnit;
    private double samplingFrequency;
    private double yMax;
    private double yMin;
    private BlockingDeque<Double> queue = new LinkedBlockingDeque<>();
    private long totalCount = 0;
    private boolean isStart = false;

    /**
     * コンストラクタです。
     *
     * @param samplingFrequency サンプリング周波数
     * @param yMax              最大値
     * @param yUnit             y軸単位
     */
    public WaveBuffer(double yMax, double yMin, Unit yUnit, double samplingFrequency) {
        this.samplingFrequency = samplingFrequency;
        this.yMax = yMax;
        this.yMin = yMin;
        this.yUnit = yUnit;
    }

    /**
     * コンストラクタです。
     *
     * @param samplingFrequency サンプリング周波数
     * @param max               最大値
     */
    public WaveBuffer(double max, double min, double samplingFrequency) {
        this(max, min, Unit.arb("amplitude"), samplingFrequency);
    }

    public Wave getWave(double sec) {
        return getWave((int) Math.round(sec * samplingFrequency));
    }

    /**
     * バッファから任意の長さでWaveをとりだします。
     *
     * @return 取り出したWave
     */
    public Wave getWave(int number) {

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
        int tmp = size < number ? 0 : size - number;
        double startTime = (size + totalCount) / samplingFrequency; // 数える用
        Wave wave = new Wave(number, yMax, yMin, yUnit, samplingFrequency, startTime);
        Iterator<Double> iterator = queue.iterator();

        int count = 0;
        while (iterator.hasNext()) {
            if (count < tmp)
                iterator.next();
            else
                wave.add(new PointOfWave((count - tmp) / samplingFrequency, iterator.next()));
            count++;
        }

        /*
        int count = size - 1;
        for (int i = 0; i < size; i++) {
            wave.add(null);
        }
        while (iterator.hasNext()) {
            if (count == -1) {
                break;
            }
            wave.set(count, new PointOfWave(count-- / samplingFrequency, iterator.next()));
        }
        */

        return wave;
    }

    /**
     * キューにデータを挿入します。
     *
     * @param val 挿入する波データ
     * @throws InterruptedException もしかしたらスレッド衝突するかもしれない
     */
    public void put(Double val) throws InterruptedException {
        if (isStart) queue.put(val);
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

    public void start() {
        isStart = true;
    }

    public void stop() {
        isStart = false;
        totalCount = 0;
        queue.clear();
    }

    public double getSamplingFrequency() {
        return samplingFrequency;
    }

    /**
     * キューに貯めておく最大秒数をセットします。
     *
     * @param xMax キューに貯めておく最大秒数
     */
    public void setXMax(double xMax) {
        this.xMax = xMax;
    }
}
