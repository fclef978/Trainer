package nitnc.kotanilab.trainer.math.point;

import java.util.function.ToDoubleFunction;
import java.util.function.UnaryOperator;

/**
 * 二次原点データのためのスーパークラスです。
 * @param <X> X軸のクラス
 * @param <Y> Y軸のクラス
 */
public abstract class AbstractPoint<X, Y> implements Cloneable {

    /**
     * xの値です。
     */
    protected X x;
    /**
     * yの値です。
     */
    protected Y y;

    /**
     * コンストラクタです。
     * @param x xの値
     * @param y yの値
     */
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
