package nitnc.kotanilab.trainer.gl.util;

import nitnc.kotanilab.trainer.util.Dbg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Vectorのリストです。
 * できる限りスレッドセーフになるようになっています。
 */
public class VectorList {
    final private List<Vector> list;

    /**
     * 空で作成します。
     */
    public VectorList() {
        //list = Collections.synchronizedList(new ArrayList<>());
        list = new ArrayList<>();
    }

    /**
     * Vectorを追加します。
     * 追加されるVectorはY値が-1.0～1.0に制限されます。
     *
     * @param vector 追加するVector
     */
    public void add(Vector vector) {
        synchronized (list) {
            if (vector.getY() > 1.0) vector.setY(1.0);
            else if (vector.getY() < -1.0) vector.setY(-1.0);
            if (vector.getX() <= 1.0 && vector.getX() >= -1.0)
                list.add(vector);
        }
    }

    /**
     * X値とY値からVectorを作成して追加します。
     * 追加されるVectorはY値が-1.0～1.0に制限されます。
     *
     * @param x 追加するVectorのX値
     * @param y 追加するVectorのY値
     */
    public void add(double x, double y) {
        this.add(new Vector(x, y));
    }

    /**
     * このVectorListに含まれるVectorを全て削除し、指定したVectorのリストの中のVectorを全てこのVectorListに追加します。
     *
     * @param vectors このVectorListに追加するVectorを含むVectorList
     */
    public void setAll(List<Vector> vectors) {
        synchronized (list) {
            list.clear();
            vectors.forEach(this::add);
        }
    }

    /**
     * このVectorListに含まれるVectorを全て削除し、指定したX値とY値の配列からVectorを作成し全てこのVectorListに追加します。
     * Y配列の長さはX配列の長さ以上でなければいけません。
     *
     * @param x X値の配列
     * @param y Y値の配列
     */
    public void setAll(double[] x, double[] y) {
        synchronized (list) {
            list.clear();
            for (int i = 0; i < x.length; i++) {
                this.add(x[i], i < y.length ? y[i] : 0);
            }
        }
    }

    /**
     * 指定した位置のVectorを削除します。
     *
     * @param index 削除するVectorのインデックス
     */
    public void remove(int index) {
        synchronized (list) {
            if (index < 0) index -= list.size();
            list.remove(index);
        }
    }

    /**
     * このVectorListに含まれるVectorを全て削除します。
     */
    public void clear() {
        synchronized (list) {
            list.clear();
        }
    }

    /**
     * このVectorListの各Vectorに対して指定されたアクションを、すべての要素が処理されるか、アクションが例外をスローするまで実行します。
     *
     * @param action 各Vectorに対して実行されるアクション
     */
    public void forEach(Consumer<? super Vector> action) {
        synchronized (list) {
            for (int i = 0; i < list.size(); i++) {
                action.accept(list.get(i));
            }
        }
    }
}
