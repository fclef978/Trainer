package nitnc.kotanilab.trainer.math.series;

import nitnc.kotanilab.trainer.math.Unit;
import nitnc.kotanilab.trainer.math.point.Point;

import java.util.Collection;

/**
 * 一番最初の要素のxの値が0になるようにずらして値を管理する系列データです。
 * TODO かなり重い
 * @param <E>
 */
public class ShiftedSeries<E extends Point> extends RealSeries<E> {

    private double xMax;
    private double base = 0; /// 前回分
    private final Object lock = new Object();

    public ShiftedSeries(Double yMax, Double yMin, Unit xUnit, Unit yUnit, double xMax) {
        super(yMax, yMin, xUnit, yUnit);
        this.xMax = xMax;
    }

    public ShiftedSeries(Double yMax, Double yMin, double xMax) {
        super(yMax, yMin);
        this.xMax = xMax;
    }

    public ShiftedSeries(Collection<? extends E> c, Double yMax, Double yMin, Unit xUnit, Unit yUnit, double xMax) {
        super(c, yMax, yMin, xUnit, yUnit);
        this.xMax = xMax;
    }

    public ShiftedSeries(int n, Double yMax, Double yMin, Unit xUnit, Unit yUnit, double xMax) {
        super(n, yMax, yMin, xUnit, yUnit);
        this.xMax = xMax;
    }

    protected E copy(E e) {
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

    public void setAll(RealSeries<? extends E> series) {
        if (series.size() == 0) return;
        synchronized (lock) {
            this.clear();
        }
        series.forEach(point -> this.add(this.copy(point)));
        double tmp = this.get(this.size() - 1).getX();
        this.forEach(point -> {
            point.shiftX(-tmp);
            point.inverseSignX();
        });
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

    public double getxMax() {
        return xMax;
    }
}
