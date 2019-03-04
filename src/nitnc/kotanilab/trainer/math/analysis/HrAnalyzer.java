package nitnc.kotanilab.trainer.math.analysis;

import nitnc.kotanilab.trainer.fft.wrapper.Fft;
import nitnc.kotanilab.trainer.fft.wrapper.OouraFft;
import nitnc.kotanilab.trainer.fx.controller.HrController;
import nitnc.kotanilab.trainer.gl.chart.Axis;
import nitnc.kotanilab.trainer.gl.chart.Chart;
import nitnc.kotanilab.trainer.gl.chart.plot.GideLine;
import nitnc.kotanilab.trainer.gl.chart.plot.LinePlot;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.util.PeriodicTask;
import nitnc.kotanilab.trainer.main.ACF;
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
    private PeriodicTask waveTask = new PeriodicTask(this::waveCallback, 30, TimeUnit.MILLISECONDS);
    private PeriodicTask hrTask = new PeriodicTask(this::hrCallback, 1000, TimeUnit.MILLISECONDS);

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
        Chart acfChart = createChart("ACF",
                new Axis("Value", 0.0, hrCalcLength / 2, hrCalcLength / 20),
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
        Wave hrWave = source.getWave(hrCalcLength);
        if (source.available(hrCalcLength)) {
            if (graphContextMap.get("HR").isVisible()) {
                double[] acf = ACF.wienerKhinchin(fft, hrWave.getYList());
                graphContextMap.get("ACF").ifVisible(context -> {
                    double[] acfX = new double[acf.length];
                    double[] acfY = new double[acf.length];
                    for (int i = 0; i < acf.length; i++) {
                        acfX[i] = context.getXAxis().scale(i);
                        acfY[i] = context.getYAxis().scale(acf[i] / acf[0]);
                    }
                    acfPlot.getLine().getVectorList().setAll(acfX, acfY);
                });
                graphContextMap.get("Diff").ifVisible(graph -> {
                    graph.setToVectorList(hrWave.stream().mapYByXY(SeriesStream.differentiate()).replaceY(y -> y / 10), diffPlot.getLine().getVectorList());
                });
                double hr = hrWave.getSamplingFrequency() / ACF.pickPeekIndex(acf) * 60.0; // 心拍数ではありえない範囲のものをカットすることで精度があがる
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
