package nitnc.kotanilab.trainer.math.series;

import nitnc.kotanilab.trainer.math.Unit;
import nitnc.kotanilab.trainer.math.point.AbstractPoint;
import nitnc.kotanilab.trainer.math.point.PointLogY;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

/**
 * サンプリングデータです。X軸が必ず実数なほか、サンプリング情報が含まれます。
 *
 * @param <Y> Y軸のクラス
 * @param <E> ポイントのクラス
 */
public class Signal<Y extends Comparable<Y>, E extends AbstractPoint<Double, ? extends Y>> extends SeriesImpl<Double, Y, E> {

    /**
     * サンプリング周波数
     */
    protected double samplingFrequency;
    /**
     * サンプリング開始時間(相対時間)
     */
    protected double startTime;

    /**
     * 初期容量10で空のシグナルを作成します。
     *
     * @param yMax              yの最大値
     * @param yMin              yの最小値
     * @param xUnit             xの単位
     * @param yUnit             yの単位
     * @param samplingFrequency サンプリング周波数
     * @param startTime         サンプリング開始時刻
     */
    public Signal(Y yMax, Y yMin, Unit xUnit, Unit yUnit, double samplingFrequency, double startTime) {
        super(yMax, yMin, xUnit, yUnit);
        this.samplingFrequency = samplingFrequency;
        this.startTime = startTime;
    }

    /**
     * 指定したコレクションの要素が全て含まれるシグナルを、要素がイテレータによって返される順序によって作成します。
     *
     * @param c                 要素がこのシグナルに配置されるコレクション
     * @param yMax              yの最大値
     * @param yMin              yの最小値
     * @param xUnit             xの単位
     * @param yUnit             yの単位
     * @param samplingFrequency サンプリング周波数
     * @param startTime         サンプリング開始時刻
     */
    public Signal(Collection<? extends E> c, Y yMax, Y yMin, Unit xUnit, Unit yUnit, double samplingFrequency, double startTime) {
        super(c, yMax, yMin, xUnit, yUnit);
        this.samplingFrequency = samplingFrequency;
        this.startTime = startTime;
    }

    /**
     * 指定された初期容量でシグナルを作成します。
     *
     * @param n                 シグナルの初期容量
     * @param yMax              yの最大値
     * @param yMin              yの最小値
     * @param xUnit             xの単位
     * @param yUnit             yの単位
     * @param samplingFrequency サンプリング周波数
     * @param startTime         サンプリング開始時刻
     */
    public Signal(int n, Y yMax, Y yMin, Unit xUnit, Unit yUnit, double samplingFrequency, double startTime) {
        super(n, yMax, yMin, xUnit, yUnit);
        this.samplingFrequency = samplingFrequency;
        this.startTime = startTime;
    }

    /**
     * データとして指定したリストを持つ自身と同じ設定のシグナルを作成します。
     *
     * @param list シグナルのデータを持つリスト
     * @return 作成したシグナル
     */
    public Signal<Y, E> from(List<E> list) {
        Signal<Y, E> ret = new Signal<>(yMax, yMin, xUnit, yUnit, samplingFrequency, startTime);
        ret.list = list;
        return ret;
    }

    /**
     * データとして指定した対数のリストを持つ自身と同じ設定のシグナルを作成します。
     *
     * @param list シグナルのデータを持つ対数のリスト
     * @return 作成したシグナル
     */
    public Signal<Double, PointLogY> fromLogY(List<PointLogY> list) {
        return new Signal<>(list, 0.0, Double.NEGATIVE_INFINITY, xUnit, Unit.db(yUnit.getName()), samplingFrequency, startTime);
    }

    public double getSamplingFrequency() {
        return samplingFrequency;
    }

    public double getStartTime() {
        return startTime;
    }

}
