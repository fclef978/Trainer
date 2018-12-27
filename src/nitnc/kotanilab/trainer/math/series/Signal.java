package nitnc.kotanilab.trainer.math.series;

import nitnc.kotanilab.trainer.math.point.AbstractPoint;
import nitnc.kotanilab.trainer.math.point.Point;
import nitnc.kotanilab.trainer.math.point.PointLogY;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

/**
 * サンプリングデータです。X軸が必ず実数なほか、サンプリング情報が含まれます。
 * @param <Y>
 * @param <E>
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

    public Signal(Y yMax, Y yMin, Unit xUnit, Unit yUnit, double samplingFrequency, double startTime) {
        super(yMax, yMin, xUnit, yUnit);
        this.samplingFrequency = samplingFrequency;
        this.startTime = startTime;
    }

    public Signal(Y yMax, Y yMin, double samplingFrequency, double startTime) {
        super(yMax, yMin);
        this.samplingFrequency = samplingFrequency;
        this.startTime = startTime;
    }

    public Signal(Collection<? extends E> c, Y yMax, Y yMin, Unit xUnit, Unit yUnit, double samplingFrequency, double startTime) {
        super(c, yMax, yMin, xUnit, yUnit);
        this.samplingFrequency = samplingFrequency;
        this.startTime = startTime;
    }

    public Signal(int n, Y yMax, Y yMin, Unit xUnit, Unit yUnit, double samplingFrequency, double startTime) {
        super(n, yMax, yMin, xUnit, yUnit);
        this.samplingFrequency = samplingFrequency;
        this.startTime = startTime;
    }

    public Signal<Y, E> from(List<E> list) {
        Signal<Y, E> ret = new Signal<>(yMax, yMin, xUnit, yUnit, samplingFrequency, startTime);
        ret.list = list;
        return ret;
    }

    public Signal<Y, E> from(SeriesStream<? extends Y> stream, BiFunction<Double, ? super Y, ? extends E> function) {
        Signal<Y, E> ret = new Signal<>(stream.count(), yMax, yMin, xUnit, yUnit, samplingFrequency, startTime);
        stream.each((x, y) -> ret.add(function.apply(x, y)));
        return ret;
    }

    public Signal<Double, PointLogY> toLogY(List<PointLogY> list) {
        return new Signal<>(list, 0.0, Double.NEGATIVE_INFINITY, xUnit, Unit.db(yUnit.getName()), samplingFrequency, startTime);
    }

    public double getSamplingFrequency() {
        return samplingFrequency;
    }

    public double getStartTime() {
        return startTime;
    }

}
