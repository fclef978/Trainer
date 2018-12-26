package jp.ac.numazu_ct.d14122.gl.util;

import java.util.Objects;

/**
 * タプル
 * @param <AT> オブジェクトA
 * @param <BT> オブジェクトB
 */
public class Pair<AT, BT> {
    private AT a;
    private BT b;

    public Pair(AT a, BT b) {
        this.a = a;
        this.b = b;
    }

    public AT getA() {
        return a;
    }

    public BT getB() {
        return b;
    }

    public void setA(AT a) {
        this.a = a;
    }

    public void setB(BT b) {
        this.b = b;
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
