package nitnc.kotanilab.trainer.math.series;

import nitnc.kotanilab.trainer.math.point.AbstractPoint;
import nitnc.kotanilab.trainer.math.point.Point;
import nitnc.kotanilab.trainer.util.Dbg;

import java.io.ObjectStreamField;
import java.util.ArrayList;
import java.util.List;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * 系列データを高速に処理するためのクラスです。
 *
 * @param <Y>
 */
public class SeriesStream<Y extends Comparable<Y>> {

    private double[] xs;
    private Object[] ys;
    private int begin = 0; // 開始インデックス
    private int end;       // 終了インデックス
    private Series series;

    public SeriesStream(Series<?, Y, ? extends AbstractPoint<?, ? extends Y>> series) {
        this.series = new SeriesImpl<Double, Y, AbstractPoint<Double, Y>>(series.getYMax(), series.getYMin(), series.getXUnit(), series.getYUnit());
        int len = series.size();
        end = len - 1;
        xs = new double[len];
        ys = new Object[len];
        repeat(len, i -> {
            Object tmp = series.get(i).getX();
            if (tmp instanceof Double) xs[i] = (Double) tmp;
            else throw new IllegalArgumentException("未実装なのでX軸がDoubleでないSeriesはStreamにできません。");
            ys[i] = series.get(i).getY();
        });
    }

    private SeriesStream(SeriesStream stream) {
        this.series = stream.series;
        this.xs = stream.xs;
        this.ys = stream.ys;
        this.begin = stream.begin;
        this.end = stream.end;
    }

    public <T> T to(Function<SeriesStream<Y>, T> function) {
        return function.apply(this);
    }

    /**
     * 中央値を返します。合計値が0の場合はサポートしません。
     *
     * @return 中央周波数のPointオブジェクト
     */
    public static Point getMedian(SeriesStream<Double> stream) {
        BiFunction<Double, Double, Point> function = Point::new;
        if (stream.count() < 2) return function.apply(stream.xs[0], stream.gy(0));
        double half = stream.reduce((a, b) -> a + b) / 2.0;

        int halfPos = stream.count() / 2 + stream.begin;
        if (half == 0.0) return function.apply(stream.xs[halfPos], stream.gy(halfPos));

        double tmp = stream.gy(stream.begin);
        for (int i = stream.begin + 1; i <= stream.end; i++) {
            tmp += stream.gy(i);
            if (tmp > half) {
                return function.apply(stream.xs[i], stream.gy(i));
            }
        }

        throw new IllegalArgumentException("不正なデータです。");
    }

    /**
     * 振幅最大の周波数を返します。
     *
     * @return ピークのPointオブジェクト
     */
    public static Point getPeek(SeriesStream<Double> stream) {
        if (stream.isEmpty()) return null;

        int maxPos = stream.begin;
        for (int i = stream.begin + 1; i <= stream.end; i++) {
            double current = stream.gy(i);
            if (current > stream.gy(maxPos)) maxPos = i;
        }

        return new Point(stream.xs[maxPos], stream.gy(maxPos));
    }

    public SeriesStream<Y> lastCutX(double range) {
        if(isEmpty()) return this;
        double lastX = xs[end];
        double limit = lastX - range;
        cutDown(limit);
        return this;
    }

    public SeriesStream<Y> zeroX() {
        if(isEmpty()) return this;
        double bias = xs[begin];
        eachIndex(i -> {
            xs[i] -= bias;
            return true;
        });
        return this;
    }

    public SeriesStream<Y> zeroX(double last) {
        if(isEmpty()) return this;
        double bias = last - xs[end];
        eachIndex(i -> {
            xs[i] += bias;
            return true;
        });
        return this;
    }

    public int count() {
        return end - begin + 1;
    }

    public Y reduce(BinaryOperator<Y> accumulator) {
        if (isEmpty()) return null;
        else if (count() < 2) return gy(0);
        Y ret = accumulator.apply(gy(begin), gy(begin + 1));
        for (int i = begin + 2; i <= end; i++) {
            ret = accumulator.apply(ret, gy(i));
        }
        return ret;
    }

    public boolean isEmpty() {
        return begin > end;
    }

    public SeriesStream<Y> cutDown(double x) {
        eachIndex(i -> {
            if (isEmpty()) {
                return false;
            } else if (xs[i] <= x) {
                begin = i + 1;
                return true;
            } else {
                return false;
            }
        });
        return this;
    }

    public SeriesStream<Y> cutUp(double x) {
        eachIndexInv(i -> {
            if (isEmpty()) {
                return false;
            } else if (xs[i] >= x) {
                end = i - 1;
                return true;
            } else {
                return false;
            }
        });
        return this;
    }

    public SeriesStream<Y> cutUp(int i) {
        end = i;
        return this;
    }

    public SeriesStream<Y> cut(double l, double b) {
        return this.cutDown(l).cutUp(b);
    }

    public SeriesStream<Y> fill(int number, Y value, double period) {
        if (count() >= number) return this;
        expand(number);
        int size = count();
        double lastX;
        if (count() == 0) lastX = 0.0;
        else lastX = xs[end];
        repeat(number - size, i -> {
            xs[i + size] = lastX + period * (i + 1);
            ys[i + size] = value;
        });
        end = begin + number - 1;
        return this;
    }

    public SeriesStream<Y> filter(Predicate<? super Y> predicate) {
        SeriesStream<Y> ret = new SeriesStream<>(this);
        int[] j = {0};
        this.eachIndex(i -> {
            if (predicate.test(this.gy(i))) {
                ret.ys[j[0]] = this.gy(i);
                ret.end = j[0];
                j[0]++;
            }
            return true;
        });
        return ret;
    }

    public SeriesStream<Y> replaceY(UnaryOperator<Y> operator) {
        eachIndex(i -> {
            ys[i] = operator.apply(gy(i));
            return true;
        });
        return this;
    }

    public SeriesStream<Y> replaceYByIndex(ToDoubleBiFunction<Integer, Integer> coefficient, BiFunction<Y, Double, Y> replacer) {
        int size = count();
        eachIndex(i -> {
            ys[i] = replacer.apply(gy(i), coefficient.applyAsDouble(i, size));
            return true;
        });
        return this;
    }

    public <E> List<E> toPointList(BiFunction<Double, ? super Y, ? extends E> pointGenerator) {
        List<E> ret = new ArrayList<>(count());
        each((x, y) -> {
            ret.add(pointGenerator.apply(x, y));
        });
        return ret;
    }

    public <S extends Series, E extends AbstractPoint> S toSeries(BiFunction<Double, ? super Y, ? extends E> pointGenerator, Function<List<E>, S> seriesConstructor) {
        return seriesConstructor.apply(toPointList(pointGenerator));
    }

    /**
     * @param mapper
     * @param <Z>
     * @return
     */
    public <Z extends Comparable<Z>> SeriesStream<Z> map(Function<Y, Z> mapper) {
        SeriesStream<Z> ret = new SeriesStream<>(this);
        ret.eachIndex(i -> {
            ret.ys[i] = mapper.apply(this.gy(i));
            return true;
        });
        return ret;
    }

    public static BiFunction<Double, Double, BiFunction<Double, Double, Double>> differentiate() {
        return (x1, x2) -> (y1, y2) -> (y2 - y1) / (x2 - x1);
    }

    public <Z extends Comparable<Z>> SeriesStream<Z> biMapXY(BiFunction<Double, Double, BiFunction<Y, Y, Z>> function) {
        if (count() < 2) return null;
        SeriesStream<Z> ret = new SeriesStream<>(this);
        for (int i = begin; i <= end - 1; i++) {
            ret.ys[i] = function.apply(xs[i], xs[i+1]).apply(gy(i), gy(i+1));
        }
        ret.end--;
        return ret;
    }

    public void each(BiConsumer<Double, ? super Y> action) {
        eachIndex(i -> {
            action.accept(xs[i], gy(i));
            return true;
        });
    }

    public static void print(Object x, Object y) {
        System.out.println("X=" + x + ",Y=" + y);
    }

    public static void repeat(int n, IntConsumer action) {
        for (int i = 0; i < n; i++) {
            action.accept(i);
        }
    }

    public static void repeat(int begin, int end, IntConsumer action) {
        for (int i = begin; i <= end; i++) {
            action.accept(i);
        }
    }

    private void eachIndex(IntPredicate action) {
        if (isEmpty()) return;
        for (int i = begin; i <= end; i++) {
            action.test(i);
        }
    }

    private void eachIndexInv(IntPredicate action) {
        if (isEmpty()) return;
        for (int i = end; i >= begin; i--) {
            action.test(i);
        }
    }

    /**
     * mapの残りカスでキャストミスするかも
     *
     * @param i
     * @return
     */
    @SuppressWarnings("unchecked")
    private Y gy(int i) {
        return (Y) ys[i];
    }

    private void expand(int n) {
        if (count() >= n) return;

        arrayCopy(xs, ys, n);

        // 順番間違えちゃだめ
        end = count() - 1;
        begin = 0;
    }

    private void arrayCopy(double[] xs, Object[] ys, int n) {
        double[] tmpX = new double[n];
        Object[] tmpY = new Object[n];
        if (!isEmpty()) {
            System.arraycopy(xs, begin, tmpX, 0, count());
            System.arraycopy(ys, begin, tmpY, 0, count());
        }
        this.xs = tmpX;
        this.ys = tmpY;
    }
}
