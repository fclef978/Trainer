package nitnc.kotanilab.trainer.math;

import nitnc.kotanilab.trainer.gl.util.PeriodicTask;
import nitnc.kotanilab.trainer.math.point.PointOfWave;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.function.ToDoubleFunction;
import java.util.function.UnaryOperator;

/**
 * オシレータ
 */
public class Oscillator extends PeriodicTask{
    protected int period;
    protected Consumer<PointOfWave> callback;
    protected UnaryOperator<Double> function;

    public Oscillator(double fs, UnaryOperator<Double> function, Consumer<PointOfWave> callback) {
        super((int) (1000.0 / fs));
        this.function = function;
        this.callback = callback;
    }

    @Override
    protected void setTask() {
        task = new OscTask(function, callback);
    }

    /**
     * 周期実行タスク
     */
    class OscTask extends TimerTask {

        private FunctionGenerator fg;
        private boolean first = true;
        private Consumer<PointOfWave> callback;

        public OscTask(UnaryOperator<Double> function, Consumer<PointOfWave> callback) {
            this.fg = new FunctionGenerator(function);
            this.callback = callback;
        }

        @Override
        public void run() {
            if (first) {
                fg.start();
                first = false;
            }
            callback.accept(fg.generate());
        }
    }

    public int getPeriod() {
        return period;
    }
}
