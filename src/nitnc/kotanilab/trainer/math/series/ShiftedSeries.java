package nitnc.kotanilab.trainer.math.series;

import nitnc.kotanilab.trainer.math.Unit;
import nitnc.kotanilab.trainer.math.point.Point;
import nitnc.kotanilab.trainer.util.Dbg;

import java.util.Collection;

/**
 * 一番最初の要素のX値が0になるようにすべての要素のX値をずらして管理する系列データです。
 * 要素数が多くなると動作が重くなるため一定の範囲外のX値を持つ要素を自動で削除します。
 *
 * @param <E> 点データのクラス
 */
public class ShiftedSeries<E extends Point> extends RealSeries<E> {

    private double xMax;
    private double base = 0; /// 前回分
    private final Object lock = new Object();

    /**
     * 空で作成します。
     *
     * @param yMax  y最大値
     * @param yMin  y最小値
     * @param xUnit x単位
     * @param yUnit y単位
     * @param xMax  保持するX値の最大値
     */
    public ShiftedSeries(Double yMax, Double yMin, Unit xUnit, Unit yUnit, double xMax) {
        super(yMax, yMin, xUnit, yUnit);
        this.xMax = xMax;
    }

    /**
     * 空で作成します。物理的情報も空になります。
     *
     * @param yMax y最大値
     * @param yMin y最小値
     * @param xMax 保持するX値の最大値
     */
    public ShiftedSeries(Double yMax, Double yMin, double xMax) {
        super(yMax, yMin);
        this.xMax = xMax;
    }

    /**
     * 浅いコピーですでにある系列データから新しい系列データを作ります。
     *
     * @param c     元になるコレクション
     * @param yMax  y最大値
     * @param yMin  y最小値
     * @param xUnit x単位
     * @param yUnit y単位
     * @param xMax  保持するX値の最大値
     */
    public ShiftedSeries(Collection<? extends E> c, Double yMax, Double yMin, Unit xUnit, Unit yUnit, double xMax) {
        super(c, yMax, yMin, xUnit, yUnit);
        this.xMax = xMax;
    }

    /**
     * 指定した長さで領域を確保した新しい系列データを作ります。
     *
     * @param n     長さ
     * @param yMax  y最大値
     * @param yMin  y最小値
     * @param xUnit x単位
     * @param yUnit y単位
     * @param xMax  保持するX値の最大値
     */
    public ShiftedSeries(int n, Double yMax, Double yMin, Unit xUnit, Unit yUnit, double xMax) {
        super(n, yMax, yMin, xUnit, yUnit);
        this.xMax = xMax;
    }

    private E copy(E e) {
        E point;
        try {
            point = (E) e.clone();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        return point;
    }

    @Override
    public boolean add(E e) {
        E point = copy(e);
        point.shiftX(-base); // 新規追加分から前回シフト量を引く
        final double tmp = point.getX(); // 減算した残りを記憶
        forEach(p -> p.shiftX(tmp)); // すでにある分からtmpを引く
        point.setX(0.0); // 新規追加分を0にする
        this.addFirst(point); // 先頭に追加(クソ重い処理)
        // 容量を超えていれば削減する
        int last = size() - 1;
        if (list.get(last).getX() > xMax) list.remove(last);
        base += tmp; // 次回分
        return true;
    }

    @Override
    public E get(int index) {
        synchronized (lock) {
            return super.get(index);
        }
    }

    @Override
    public void clear() {
        super.clear();
        base = 0;
    }

    public double getXMax() {
        return xMax;
    }
}
