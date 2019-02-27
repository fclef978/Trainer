package nitnc.kotanilab.trainer.gl.util;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 * Created by Hirokazu SUZUKI on 2018/07/31.
 */

/**
 * 周期実行を簡単に行うためのクラスです。
 * TimerとTimerTaskのラッパクラスです。
 * Timerと違って、何度でも再実行できます。
 * 実行内容は、引数に繰り返し回数が渡されます。
 */
public class PeriodicTask {

    protected Consumer<Long> callback;
    protected Timer timer;
    protected TimerTask task;
    protected int period;
    private long count = 0;

    /**
     * 指定した周期で周期実行を準備します。
     * 実行内容は後で設定します。
     *
     * @param period 周期[ms]
     */
    public PeriodicTask(int period) {
        this.period = period;
    }

    /**
     * 指定した周期と実行内容で周期実行を準備します。
     *
     * @param callback 実行内容
     * @param period   周期[ms]
     */
    public PeriodicTask(Consumer<Long> callback, int period) {
        setCallback(callback);
        this.period = period;
    }

    /**
     * 実行内容を設定します。
     *
     * @param callback 実行内容
     */
    public void setCallback(Consumer<Long> callback) {
        this.callback = callback;
    }

    /**
     * 周期実行を開始します。
     */
    public void start() {
        if (timer == null) {
            timer = new Timer();
            setTask();
            timer.scheduleAtFixedRate(task, 0, period);
        }
    }

    /**
     * 周期実行を終了します。
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 周期実行のTimerTaskを設定します。
     * 複雑な内容のタスクを設定したいときはオーバーライドしてください。
     */
    protected void setTask() {
        /*
         * 周期実行タスク
         */
        class Task extends TimerTask {
            @Override
            public void run() {
                callback.accept(count++);
            }
        }
        task = new Task();
    }

    @Override
    protected void finalize() {
        stop();
    }

}
