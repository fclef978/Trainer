package nitnc.kotanilab.trainer.math.analysis;

import nitnc.kotanilab.trainer.fft.wrapper.Fft;
import nitnc.kotanilab.trainer.fft.wrapper.OouraFft;
import nitnc.kotanilab.trainer.fx.controller.HrController;
import nitnc.kotanilab.trainer.gl.chart.Axis;
import nitnc.kotanilab.trainer.gl.chart.Chart;
import nitnc.kotanilab.trainer.gl.chart.plot.GideLine;
import nitnc.kotanilab.trainer.gl.chart.plot.LinePlot;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.util.Dbg;
import nitnc.kotanilab.trainer.util.PeriodicTask;
import nitnc.kotanilab.trainer.math.ACF;
import nitnc.kotanilab.trainer.math.Unit;
import nitnc.kotanilab.trainer.math.point.Point;
import nitnc.kotanilab.trainer.math.series.*;
import nitnc.kotanilab.trainer.util.CsvLogger;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.DoubleConsumer;

/**
 * 心拍数解析を行うAnalyzerです。
 */
public class HrAnalyzer extends Analyzer {
    private Fft fft;
    private int hrCalcLength = 512;
    private DoubleConsumer hrEvent = value -> {
    };
    private int age = 20;
    private CsvLogger logger;
    private LinePlot wavePlot;
    private LinePlot hrPlot;
    private LinePlot acfPlot;
    private LinePlot diffPlot;
    private GideLine maxHRGide = new GideLine("Maximal", HrController.getMaxHR(age), Color.RED, 1.0, false);
    private GideLine targetHRGide = new GideLine("Target", HrController.getMaxHR(age) * HrController.OPT_MET, Color.GREEN.darker(), 1.0, false);

    private RealSeries<Point> heartRate = new RealSeries<>(200.0, 30.0, Unit.sec(), Unit.arb("HR"));
    private PeriodicTask waveTask = new PeriodicTask(()->{
        try {
            this.waveCallback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }, 30, TimeUnit.MILLISECONDS);

    private PeriodicTask hrTask = new PeriodicTask(()->{
        try {
            this.hrCallback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }, 1000, TimeUnit.MILLISECONDS);

    /**
     * コンストラクタです。
     *
     * @param masterPane グラフの親ペイン
     */
    public HrAnalyzer(Pane masterPane) {
        super(masterPane, "HR");

        wavePlot = new LinePlot("Wave");
        Chart waveChart = createWaveChart("Wave", 1, Unit.v(), -2, 2);
        waveChart.getPlots().add(wavePlot);
        putNewGraphContext(waveChart);

        hrPlot = new LinePlot("HR");
        Chart hrChart = createTimeSeriesChart("Heart Rate", 60, new Axis("HR[bpm]", 50.0, 210.0, 20));
        hrChart.getPlots().addAll(Arrays.asList(hrPlot, maxHRGide, targetHRGide));
        putNewGraphContext("HR", hrChart);

        acfPlot = new LinePlot("ACF");
        double acfTime = hrCalcLength / 2.0 / 1000. * 16.;
        Chart acfChart = createChart("ACF",
                new Axis("Value", 0.0, acfTime, Math.round(acfTime / 5)),
                new Axis("Value", -1.0, 1.0, 0.5)
        );
        acfChart.getPlots().add(acfPlot);
        putNewGraphContext(acfChart);

        diffPlot = new LinePlot("Diff");
        Chart diffChart = createWaveChart("Differential", 6, Unit.v(), -5, 5);
        diffChart.getPlots().add(diffPlot);
        putNewGraphContext("Diff", diffChart);

        getCharts().forEach(Chart::updateLegend);
    }

    @Override
    public void start(double fs, int n) {
        source.setXMax(10.0);
        source.setDecimationNumber(16);
        source.addCallback(IirFilter.load("bpf0.0001-0.01.txt"));
        graphContextMap.get("Wave").setAxisSetter((x, y) -> {
            x.setMax(hrCalcLength / source.getSamplingFrequency());
            x.setSize(hrCalcLength / source.getSamplingFrequency() / 8.0);
        });
        graphContextMap.values().forEach(graphContext -> graphContext.confirm(masterPane));
        fft = new OouraFft(hrCalcLength);
        logger = new CsvLogger("HR.csv");
        waveTask.start();
        hrTask.start();
    }

    @Override
    public void stop() {
        super.stop();
        waveTask.stop();
        hrTask.stop();
        heartRate.clear();
        Arrays.asList(wavePlot, hrPlot, acfPlot, diffPlot).forEach(plot -> plot.getLine().getVectorList().clear());
        logger.close();
    }

    /**
     * 波形表示のメソッド
     */
    private void waveCallback() {
        graphContextMap.get("Wave").ifVisible(context -> {
            Wave wave = source.getWave(hrCalcLength);
            context.setToVectorListRealTime(wave.stream(), wavePlot.getLine().getVectorList());
        });
    }

    /**
     * 心拍数解析のメソッド
     */
    private void hrCallback() {
        if (source.available(hrCalcLength)) {
            if (graphContextMap.get("HR").isVisible()) {
                Wave hrWave = source.getWave(hrCalcLength);
                Wave acf = ACF.wienerKhinchin(fft, hrWave);
                double acfMax = acf.get(0).getY();
                Wave acfStream = acf.stream().cutAfter(acf.size() / 2).replaceY(y -> y / acfMax).to(acf::from);
                graphContextMap.get("ACF").ifVisible(context -> {
                    context.setToVectorList(acfStream.stream(), acfPlot.getLine().getVectorList());
                    /*
                    double[] acfX = new double[acf.length];
                    double[] acfY = new double[acf.length];
                    for (int i = 0; i < acf.length; i++) {
                        acfX[i] = context.getXAxis().scale(i);
                        acfY[i] = context.getYAxis().scale(acf[i] / acf[0]);
                    }
                    acfPlot.getLine().getVectorList().setAll(acfX, acfY);
                    */
                });
                graphContextMap.get("Diff").ifVisible(graph -> {
                    graph.setToVectorList(hrWave.stream().mapYByXY(SeriesStream.differentiater).replaceY(y -> y / 10), diffPlot.getLine().getVectorList());
                });
                Point period = acfStream.stream().cutAboveX(2.).filterBySurroundY(SeriesStream.peekPicker).reduce(Math::max, Point::new);
                Dbg.p(period);
                double hr = 60.0 / period.getX();
                Point hrPoint = new Point(hrWave.getStartTime(), hr);
                logger.print(hrPoint);
                heartRate.add(hrPoint);
                graphContextMap.get("HR").setToVectorListRealTime(heartRate.stream(), hrPlot.getLine().getVectorList());
                maxHRGide.setPosition(HrController.getMaxHR(age));
                targetHRGide.setPosition(HrController.getMaxHR(age) * HrController.OPT_MET); // 最大心拍数に運動強度をかけたもの
                hrEvent.accept(hr);
            }

        }
    }

    /**
     * 心拍数が求まった際に実行されるコールバックを設定します。
     *
     * @param hrEvent 心拍数が求まった際に実行されるコールバック
     */
    public void setHrEvent(DoubleConsumer hrEvent) {
        this.hrEvent = hrEvent;
    }

    /**
     * 最大心拍数計算用の年齢を設定します。
     *
     * @param age ユーザの年齢
     */
    public void setAge(int age) {
        this.age = age;
    }

}
