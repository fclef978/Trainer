package nitnc.kotanilab.trainer.math.series;

import nitnc.kotanilab.trainer.math.Unit;
import nitnc.kotanilab.trainer.math.point.Point;
import nitnc.kotanilab.trainer.math.point.PointLogY;

import java.util.Collection;
import java.util.List;

/**
 * XY軸がともに実数の系列データクラスです。
 * @param <E> 点データの型
 */
public class RealSeries<E extends Point> extends SeriesImpl<Double, Double, E> {

    /**
     * 空の系列データを作ります。
     * @param yMax y最大値
     * @param yMin y最小値
     * @param xUnit x単位
     * @param yUnit y単位
     */
    public RealSeries(Double yMax, Double yMin, Unit xUnit, Unit yUnit) {
        super(yMax, yMin, xUnit, yUnit);
    }

    /**
     * 空の系列データを作ります。物理的情報も空になります。
     * @param yMax y最大値
     * @param yMin y最小値
     */
    public RealSeries(Double yMax, Double yMin) {
        super(yMax, yMin);
    }

    /**
     * 浅いコピーですでにある系列データから新しい系列データを作ります。
     * @param c 元になるコレクション
     * @param yMax y最大値
     * @param yMin y最小値
     * @param xUnit x単位
     * @param yUnit y単位
     */
    public RealSeries(Collection<? extends E> c, Double yMax, Double yMin, Unit xUnit, Unit yUnit) {
        super(c, yMax, yMin, xUnit, yUnit);
    }

    /**
     * 指定した長さで領域を確保した新しい系列データを作ります。
     * @param n 長さ
     * @param yMax y最大値
     * @param yMin y最小値
     * @param xUnit x単位
     * @param yUnit y単位
     */
    public RealSeries(int n, Double yMax, Double yMin, Unit xUnit, Unit yUnit) {
        super(n, yMax, yMin, xUnit, yUnit);
    }

    public RealSeries<E> from(List<E> list) {
        RealSeries<E> ret = new RealSeries<>(yMax, yMin, xUnit, yUnit);
        ret.list = list;
        return ret;
    }

    public RealSeries<PointLogY> getLogSeries() {
        List<PointLogY> tmp = stream().combine((x, y) -> new PointLogY(x, y, yMax));
        return new RealSeries<>(tmp, 0.0, Double.NEGATIVE_INFINITY, xUnit, Unit.db(yUnit.getName()));
    }

    /**
     * 中央値を返します。
     * @return 中央周波数のPointオブジェクト
     */
    public Point getMeanPoint() {
        double sum = stream().reduce((a, b) -> a + b);

        if (sum == 0.0) return list.get(list.size() / 2).clone();

        double tmp = 0.0;
        Point ret = null;

        for (Point point : list) {
            tmp += point.getY();
            if (tmp > sum / 2.0) {
                ret = point.clone();
                break;
            }
        }

        if (ret == null) throw new IllegalArgumentException("不正なスペクトラムです sum=" + sum);
        return ret;
    }

    /**
     * 振幅最大の周波数を返します。
     * @return ピークのPointオブジェクト
     */
    public Point getPeekPoint() {
        if (size() == 0) return null;

        Point max = get(0);
        for (int i = 1; i < size(); i++) {
            Point current = get(i);
            if (current.getY() > max.getY()) max = current;
        }

        return max;
    }
}
