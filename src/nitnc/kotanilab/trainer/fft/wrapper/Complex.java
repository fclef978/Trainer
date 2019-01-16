package nitnc.kotanilab.trainer.fft.wrapper;

import nitnc.kotanilab.trainer.math.point.ComplexPoint;

import java.util.function.ToDoubleFunction;

/**
 * 複素数を表すクラスです。
 */
public class Complex implements Cloneable, Comparable<Complex> {

    /**
     * 実部です。
     */
    protected double re;

    /**
     * 虚部です。
     */
    protected double im;

    /**
     * コンストラクタです。
     * @param re 実部です。
     * @param im 虚部です。
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Complex(double abs) {
        this.re = Math.sqrt(abs);
        this.im = Math.sqrt(abs);
    }

    /**
     * 実部虚部ともに0で作成します。
     */
    public Complex() {
        this(0,0);
    }

    @Override
    public int compareTo(Complex o) {
        double tmp = this.getAbs() - o.getAbs();
        if (tmp > 0) {
            return 1;
        } else if (tmp < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 絶対値を返します。
     * @return 絶対値です。
     */
    public double getAbs() {
        return Math.hypot(re, im);
    }

    public double getPower() {
        return Math.pow(getAbs(), 2.0);
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    @Override
    public Complex clone() {
        Complex other = null;

        try {
            other = new Complex(re, im);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return other;
    }

    @Override
    public String toString() {
        return re +
                "+" + im +
                'i';
    }
}
