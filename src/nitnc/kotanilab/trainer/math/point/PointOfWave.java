package nitnc.kotanilab.trainer.math.point;

import java.util.function.ToDoubleFunction;

/**
 * Waveの点を表すクラスです。
 */
public class PointOfWave extends Point {
    /**
     * コンストラクタです。
     *
     * @param x 時間[s]
     * @param y 振幅
     */
    public PointOfWave(double x, double y) {
        super(x, y);
    }

    @Override
    public PointOfWave clone() {
        PointOfWave other = null;

        try {
            other = new PointOfWave(x, y);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return other;
    }
}
