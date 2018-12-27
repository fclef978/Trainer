package nitnc.kotanilab.trainer.math.point;

import java.util.function.ToDoubleFunction;

/**
 * Created by Hirokazu SUZUKI on 2018/07/16.
 * 点
 */
public class PointOfWave extends Point {
    public PointOfWave(double x, double y) {
        super(x, y);
    }

    @Override
    public PointOfWave clone(){
        PointOfWave other = null;

        try {
            other = new PointOfWave(x, y);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return other;
    }
}
