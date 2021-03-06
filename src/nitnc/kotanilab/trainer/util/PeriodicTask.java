package nitnc.kotanilab.trainer.util;

import nitnc.kotanilab.trainer.util.Dbg;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 周期実行を簡単に行うためのクラスです。
 * TimerとTimerTaskのラッパクラスです。
 * Timerと違って、何度でも再実行できます。
 * 実行内容は、引数に繰り返し回数が渡されます。
 */
public class PeriodicTask {

    private static ScheduledExecutorService service;

    protected Runnable callback;
    protected Timer timer;
    protected TimerTask task;
    protected long period;
    protected TimeUnit timeUnit;
    protected ScheduledFuture<?> future;

    /**
     * 指定した周期で周期実行を準備します。
     * 実行内容は後で設定します。
     *
     * @param period   周期 単位はtimeUnit依存
     * @param timeUnit periodのTimeUnit
     */
    public PeriodicTask(long period, TimeUnit timeUnit) {
        this(() -> {
        }, period, timeUnit);
    }

    /**
     * 指定した周期と実行内容で周期実行を準備します。
     *
     * @param callback 実行内容
     * @param period   周期 単位はtimeUnit依存
     * @param timeUnit periodのTimeUnit
     */
    public PeriodicTask(Runnable callback, long period, TimeUnit timeUnit) {
        if (service == null) service = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        setCallback(callback);
        this.period = period;
        this.timeUnit = timeUnit;
    }

    /**
     * 実行内容を設定します。
     *
     * @param callback 実行内容
     */
    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    /**
     * 周期実行を開始します。
     */
    public void start() {
        future = service.scheduleAtFixedRate(callback, 0, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 周期実行を終了します。
     */
    public void stop() {
        future.cancel(false);
    }

    @Override
    protected void finalize() {
        stop();
    }

}
