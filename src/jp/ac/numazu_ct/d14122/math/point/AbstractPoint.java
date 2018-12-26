package jp.ac.numazu_ct.d14122.math.point;

import java.util.function.ToDoubleFunction;
import java.util.function.UnaryOperator;

/**
 * 二次元点データのための抽象クラスです。
 * このクラスはイミュータブルでなければなりません。
 */
public class AbstractPoint<X, Y> implements Cloneable {

    protected X x;
    protected Y y;

    public AbstractPoint(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }

    public void setX(X x) {
        this.x = x;
    }

    public void setY(Y y) {
        this.y = y;
    }

}
