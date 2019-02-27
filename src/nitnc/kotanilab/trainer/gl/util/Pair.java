package nitnc.kotanilab.trainer.gl.util;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * オブジェクト二つのタプルを表すクラスです。
 * 未使用
 *
 * @param <A> クラスA
 * @param <B> クラスB
 */
public class Pair<A, B> {
    private A a;
    private B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public void setA(A a) {
        this.a = a;
    }

    public void setB(B b) {
        this.b = b;
    }

    /**
     * AとBを引数にとる関数を受け取り、その結果を返します。
     *
     * @param function AとBを引数にとる関数
     * @param <Y>      関数の返り値の型
     * @return 関数の型
     */
    public <Y> Y func(BiFunction<? super A, ? super B, Y> function) {
        return function.apply(a, b);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(a, pair.a) &&
                Objects.equals(b, pair.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
