package nitnc.kotanilab.trainer.math.point;

/**
 * Y軸が対数[dB]表現の点データです。
 * 元の値を保持しています。
 */
public class PointLogY extends Point {

    protected double baseLevel;

    public PointLogY(double x, double y, double baseLevel) {
        super(x, dB(y, baseLevel));
        this.baseLevel = baseLevel;
    }

    public double getAntiLogY() {
        return super.getY();
    }

    public Point getAntiLogPoint() {
        return new Point(x, getY());
    }

    public double getBaseLevel() {
        return baseLevel;
    }

    public static Double dB(double y, double baseLevel) {
        double tmp =  20.0 * Math.log10(y / baseLevel);
        return tmp == Double.NEGATIVE_INFINITY ? -Double.MAX_VALUE : tmp;
    }

}
