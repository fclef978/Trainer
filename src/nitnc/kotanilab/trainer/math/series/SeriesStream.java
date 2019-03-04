package nitnc.kotanilab.trainer.math.series;

import nitnc.kotanilab.trainer.math.point.AbstractPoint;
import nitnc.kotanilab.trainer.math.point.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

/**
 * 系列データを高速に処理するためのクラスです。
 * X,Y値ともにDoubleである必要があります。
 * SeriesがそうであるようにXについてソートされています。
 *
 * @param <Y> Y軸のクラス
 */
public class SeriesStream<Y extends Comparable<Y>> {

    private double[] xs;
    private Object[] ys;
    private int begin = 0; // 開始インデックス
    private int end;       // 終了インデックス
    private Series series;

    /**
     * Seriesから作成します。
     *
     * @param series StreamのもとになるSeries
     */
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

    /**
     * 指定されたSeriesStreamを別のオブジェクトに変換する関数を用いてこのSeriesStreamを別のオブジェクトに変換する終端操作です。
     * メソッドチェーンでつなぐことができます。
     *
     * @param function このSeriesStreamを別のオブジェクトに変換する関数
     * @param <T>      変換されるクラス
     * @return 変換されたオブジェクト
     */
    public <T> T to(Function<SeriesStream<Y>, T> function) {
        return function.apply(this);
    }

    /**
     * 指定したSeriesStreamの中央値を返します。合計値が0の場合はサポートしません。
     *
     * @param stream 中央値を求めたいSeriesStream
     * @return 中央周波数のPoint
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
     * 指定したSeriesStreamの最頻値を返します。
     *
     * @param stream 最頻値を求めたいSeriesStream
     * @return 最頻値のPoint
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

    /**
     * Xの最大値から指定した範囲外の点をすべて削除する中間操作です。
     *
     * @param range 残す範囲
     * @return このSeriesStream
     */
    public SeriesStream<Y> lastCutX(double range) {
        if (isEmpty()) return this;
        double lastX = xs[end];
        double limit = lastX - range;
        cutDownX(limit);
        return this;
    }

    /**
     * Xの最小値が0になるようにすべてのX値をシフトする中間操作です。
     *
     * @return このSeriesStream
     */
    public SeriesStream<Y> zeroMinX() {
        if (isEmpty()) return this;
        double bias = xs[begin];
        eachIndex(i -> {
            xs[i] -= bias;
            return true;
        });
        return this;
    }

    /**
     * Xの最大値が指定した値になるようにすべてのX値をシフトする中間操作です。
     *
     * @param value Xの最大値にしたい値
     * @return このSeriesStream
     */
    public SeriesStream<Y> shiftMaxX(double value) {
        if (isEmpty()) return this;
        double bias = value - xs[end];
        eachIndex(i -> {
            xs[i] += bias;
            return true;
        });
        return this;
    }

    /**
     * このSeriesStreamのデータ点数を返す終端操作です。
     *
     * @return このSeriesStreamのデータ点数
     */
    public int count() {
        return end - begin + 1;
    }

    /**
     * 指定された累積関数を用いてこのSeriesStreamのリダクションを行い結果を返す終端操作です。
     *
     * @param accumulator 累積関数
     * @return リダクションの結果
     */
    public Y reduce(BinaryOperator<Y> accumulator) {
        if (isEmpty()) return null;
        else if (count() < 2) return gy(0);
        Y ret = accumulator.apply(gy(begin), gy(begin + 1));
        for (int i = begin + 2; i <= end; i++) {
            ret = accumulator.apply(ret, gy(i));
        }
        return ret;
    }

    /**
     * このSeriesStreamが空かどうかを返す終端操作です。
     *
     * @return このSeriesStreamが空ならtrue、そうでないならfalse
     */
    public boolean isEmpty() {
        return begin > end;
    }

    /**
     * 指定した下限値以下のX値を持つ要素を全て削除する中間操作です。
     *
     * @param x X値の下限値
     * @return このSeriesStream
     */
    public SeriesStream<Y> cutDownX(double x) {
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

    /**
     * 指定した上限値以上のX値を持つ要素を全て削除する中間操作です。
     *
     * @param x X値の上限値
     * @return このSeriesStream
     */
    public SeriesStream<Y> cutUpX(double x) {
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

    /**
     * 指定したインデックス以上の要素を全て削除する中間操作です。
     *
     * @param i インデックス上限値
     * @return このSeriesStream
     */
    public SeriesStream<Y> cutUp(int i) {
        end = i;
        return this;
    }

    /**
     * 指定した範囲外のX値を持つ要素を全て削除する中間操作です。
     *
     * @param lower X値の下限値
     * @param upper X値の上限値
     * @return このSeriesStream
     */
    public SeriesStream<Y> cut(double lower, double upper) {
        return this.cutDownX(lower).cutUpX(upper);
    }

    /**
     * 指定したデータ点数になるまで指定したY値でデータを追加する中間操作です。
     * 追加されるデータのX値は、このSeriesStreamの最後の要素のX値から指定した刻み幅で一定で増えます。
     * 既に指定したデータ点数以上のデータ点数がある場合はなにもしません。
     *
     * @param number  データ点数
     * @param yValue  埋めるY値
     * @param xPeriod Xの刻み幅
     * @return このSeriesStream
     */
    public SeriesStream<Y> fill(int number, Y yValue, double xPeriod) {
        if (count() >= number) return this;
        expand(number);
        int size = count();
        double lastX;
        if (count() == 0) lastX = 0.0;
        else lastX = xs[end];
        repeat(number - size, i -> {
            xs[i + size] = lastX + xPeriod * (i + 1);
            ys[i + size] = yValue;
        });
        end = begin + number - 1;
        return this;
    }

    /**
     * 指定した述語に一致しないデータを削除する中間操作です。
     *
     * @param predicate 各要素を含めるべきか判定する目的で各要素に適用する述語
     * @return 新しいSeriesStream
     */
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

    /**
     * 指定した演算子を適用した結果で置換する中間操作です。
     *
     * @param xOperator Xの演算子
     * @param yOperator Yの演算子
     * @return このSeriesStream
     */
    public SeriesStream<Y> replace(UnaryOperator<Double> xOperator, UnaryOperator<Y> yOperator) {
        eachIndex(i -> {
            ys[i] = yOperator.apply(gy(i));
            xs[i] = xOperator.apply(xs[i]);
            return true;
        });
        return this;
    }

    /**
     * Y値を指定した演算子を適用した結果で置換する中間操作です。
     *
     * @param operator Yの演算子
     * @return このSeriesStream
     */
    public SeriesStream<Y> replaceY(UnaryOperator<Y> operator) {
        eachIndex(i -> {
            ys[i] = operator.apply(gy(i));
            return true;
        });
        return this;
    }

    /**
     * Y値を指定したデータのインデックスによる演算子を適用した結果で置換する中間操作です。
     *
     * @param coefficient データのインデックスとこのSeriesStreamのデータ点数を受け取り係数を返す関数
     * @param replacer    Y値と係数を受け取り置き換えるY値を作成する関数
     * @return このSeriesStream
     */
    public SeriesStream<Y> replaceYByIndex(ToDoubleBiFunction<Integer, Integer> coefficient, BiFunction<Y, Double, Y> replacer) {
        int size = count();
        eachIndex(i -> {
            ys[i] = replacer.apply(gy(i), coefficient.applyAsDouble(i, size));
            return true;
        });
        return this;
    }

    /**
     * 指定したX値とY値を受け取り新しいオブジェクトを返す関数を用いて新しいオブジェクトのListを返す終端操作です。
     *
     * @param combiner X値とY値を受け取り新しいオブジェクトを返す関数
     * @param <E>      X値とY値を結合した新しいオブジェクトのクラス
     * @return 新しいオブジェクトのList
     */
    public <E> List<E> combine(BiFunction<Double, ? super Y, ? extends E> combiner) {
        List<E> ret = new ArrayList<>(count());
        forEach((x, y) -> ret.add(combiner.apply(x, y)));
        return ret;
    }

    /**
     * 指定した関数を適用した結果から構成される新しいSeriesStreamを返す中間操作です。
     *
     * @param mapper 各要素に適用する非干渉でステートレスな関数
     * @param <Z>    新しいSeriesStreamの要素の型
     * @return 新しいSeriesStream
     */
    public <Z extends Comparable<Z>> SeriesStream<Z> mapY(Function<Y, Z> mapper) {
        SeriesStream<Z> ret = new SeriesStream<>(this);
        ret.eachIndex(i -> {
            ret.ys[i] = mapper.apply(this.gy(i));
            return true;
        });
        return ret;
    }

    public static BiFunction<Double, Double, BiFunction<Double, Double, Double>> differentiater
            = (x1, x2) -> (y1, y2) -> (y2 - y1) / (x2 - x1);

    public static BiFunction<Double, Double, BiFunction<Double, Double, Double>> differentiate() {
        return (x1, x2) -> (y1, y2) -> (y2 - y1) / (x2 - x1);
    }

    /**
     * カリー化された、連続する二つのXの値と連続する二つのYの値を受け取り新しいY値を返す関数を適用した結果から構成される新しいSeriesStreamを返す中間操作です。
     * 微分や積分に使用できます。
     *
     * @param function カリー化された、連続する二つのXの値と連続する二つのYの値を受け取り新しいY値を返す関数
     * @param <Z>      新しいSeriesStreamの要素の型
     * @return 新しいSeriesStream
     */
    public <Z extends Comparable<Z>> SeriesStream<Z> mapYByXY(BiFunction<Double, Double, BiFunction<Y, Y, Z>> function) {
        if (count() < 2) return null;
        SeriesStream<Z> ret = new SeriesStream<>(this);
        for (int i = begin; i <= end - 1; i++) {
            ret.ys[i] = function.apply(xs[i], xs[i + 1]).apply(gy(i), gy(i + 1));
        }
        ret.end -= 1;
        return ret;
    }

    /**
     * この系列データの各X,Yに対して指定されたアクションを、すべてのデータが処理されるか、アクションが例外をスローするまで実行します。
     *
     * @param action 各X,Yに対して実行されるアクション
     */
    public void forEach(BiConsumer<Double, ? super Y> action) {
        eachIndex(i -> {
            action.accept(xs[i], gy(i));
            return true;
        });
    }

    private static void repeat(int n, IntConsumer action) {
        for (int i = 0; i < n; i++) {
            action.accept(i);
        }
    }

    private static void repeat(int begin, int end, IntConsumer action) {
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
     * Y配列から値をキャストして取得
     * mapの残りカスでキャストミスするかも
     *
     * @param i インデックス
     * @return Y値
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
