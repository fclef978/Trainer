package nitnc.kotanilab.trainer.gl.util;

import nitnc.kotanilab.trainer.util.Dbg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Vectorのリスト
 * スレッドセーフです（予定）。
 */
public class VectorList {
    private List<Vector> list;
    private UnaryOperator<Double> xConverter;
    private UnaryOperator<Double> yConverter;
    final private Object lock = new Object();
    private int sign = 1;

    /**
     * 空のベクトルリストを作成します。
     *
     * @param xConverter xの値をベクトル値に変換する処理
     * @param yConverter yの値をベクトル値に変換する処理
     */
    public VectorList(UnaryOperator<Double> xConverter, UnaryOperator<Double> yConverter) {
        list = Collections.synchronizedList(new ArrayList<>());
        this.xConverter = xConverter;
        this.yConverter = yConverter;
    }

    public void add(Vector vector) {
        synchronized (lock) {
            if (vector.getY() > 1.0) vector.setY(1.0);
            else if (vector.getY() < -1.0) vector.setY(-1.0);
            if (vector.getX() <= 1.0 && vector.getX() >= -1.0)
                list.add(vector);
        }
    }

    public void add(double x, double y) {
        this.add(new Vector(sign * xConverter.apply(x), yConverter.apply(y)));
    }

    /**
     * TODO かなり効率が悪い
     *
     * @param xc xのコレクション
     * @param yc yのコレクション
     */
    public void set(List<? extends Double> xc, Collection<? extends Double> yc) {
        synchronized (lock) {
            list.clear();
            Double[] x = xc.toArray(new Double[0]);
            Double[] y = yc.toArray(new Double[0]);
            for (int i = 0; i < x.length; i++) {
                this.add(x[i], i < y.length ? y[i] : 0);
            }
        }
    }

    public void remove(int index) {
        synchronized (lock) {
            if (index < 0) index -= list.size();
            list.remove(index);
        }
    }

    public void clear() {
        synchronized (lock) {
            list.clear();
        }
    }

    public void forEach(Consumer<? super Vector> action) {
        synchronized (lock) {
            list.forEach(action);
        }
    }

    public Collection<Vector> getCollection() {
        synchronized (lock) {
            return list;
        }
    }

    public void reverse(boolean dir) {
        if (dir) {
            sign = -1;
        } else {
            sign = 1;
        }
    }
}
