package nitnc.kotanilab.trainer.math.point;

/**
 * Y軸が対数[dB]表現の点データです。
 * 元の値を保持しています。
 */
public class PointLogY extends Point {

    /**
     * 対数にする際の基準値
     */
    protected double baseLevel;

    /**
     * コンストラクタです。
     *
     * @param x         xの値
     * @param y         非対数のyの値
     * @param baseLevel 対数にする際の基準値
     */
    public PointLogY(double x, double y, double baseLevel) {
        super(x, dB(y, baseLevel));
        this.baseLevel = baseLevel;
    }

    public double getBaseLevel() {
        return baseLevel;
    }

    /**
     * デシベルを計算します。
     *
     * @param y         計算する値
     * @param baseLevel 対数にする際の基準値
     * @return デシベル
     */
    public static Double dB(double y, double baseLevel) {
        double tmp = 20.0 * Math.log10(y / baseLevel);
        return tmp == Double.NEGATIVE_INFINITY ? -Double.MAX_VALUE : tmp;
    }

}
