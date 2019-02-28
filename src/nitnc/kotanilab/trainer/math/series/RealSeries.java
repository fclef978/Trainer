package nitnc.kotanilab.trainer.math.series;

import nitnc.kotanilab.trainer.math.Unit;
import nitnc.kotanilab.trainer.math.point.Point;
import nitnc.kotanilab.trainer.math.point.PointLogY;

import java.util.Collection;
import java.util.List;

/**
 * XY軸がともに実数の系列データクラスです。
 *
 * @param <E> 点データの型
 */
public class RealSeries<E extends Point> extends SeriesImpl<Double, Double, E> {

    /**
     * 空の系列データを作ります。
     *
     * @param yMax  y最大値
     * @param yMin  y最小値
     * @param xUnit x単位
     * @param yUnit y単位
     */
    public RealSeries(Double yMax, Double yMin, Unit xUnit, Unit yUnit) {
        super(yMax, yMin, xUnit, yUnit);
    }

    /**
     * 空の系列データを作ります。物理的情報も空になります。
     *
     * @param yMax y最大値
     * @param yMin y最小値
     */
    public RealSeries(Double yMax, Double yMin) {
        super(yMax, yMin);
    }

    /**
     * 浅いコピーですでにある系列データから新しい系列データを作ります。
     *
     * @param c     元になるコレクション
     * @param yMax  y最大値
     * @param yMin  y最小値
     * @param xUnit x単位
     * @param yUnit y単位
     */
    public RealSeries(Collection<? extends E> c, Double yMax, Double yMin, Unit xUnit, Unit yUnit) {
        super(c, yMax, yMin, xUnit, yUnit);
    }

    /**
     * 指定した長さで領域を確保した新しい系列データを作ります。
     *
     * @param n     長さ
     * @param yMax  y最大値
     * @param yMin  y最小値
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

    /**
     * yMaxを基準としてY値を対数(dB)にとした新しいRealSeriesを作成します。
     * フィールドはこのRealSeriesのものが引き継がれます。
     *
     * @return yMaxを基準としてY値を対数(dB)にとした新しいRealSeries
     */
    public RealSeries<PointLogY> getLogSeries() {
        List<PointLogY> tmp = stream().combine((x, y) -> new PointLogY(x, y, yMax));
        return new RealSeries<>(tmp, 0.0, Double.NEGATIVE_INFINITY, xUnit, Unit.db(yUnit.getName()));
    }
}
