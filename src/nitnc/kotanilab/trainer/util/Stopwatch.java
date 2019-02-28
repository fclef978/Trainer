package nitnc.kotanilab.trainer.util;

public class Stopwatch {

    public double previousX = 0.0;

    public boolean isPassedInterval(double interval) {
        boolean ret =  getTime() - previousX > interval;
        if (ret) start();
        return ret;
    }

    public void start() {
        previousX = getTime();
    }

    public double take() {
        return getTime() - previousX;
    }

    /**
     * 現在の秒数をミリ秒単位で返します。
     * @return 現在の秒数[ms]
     */
    private static double getTime() {
        return System.currentTimeMillis() / 1000.0;
    }
}
