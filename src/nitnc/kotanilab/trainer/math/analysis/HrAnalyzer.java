package nitnc.kotanilab.trainer.math.analysis;

import nitnc.kotanilab.trainer.fft.wrapper.Fft;
import nitnc.kotanilab.trainer.fft.wrapper.OouraFft;
import nitnc.kotanilab.trainer.fx.controller.HrController;
import nitnc.kotanilab.trainer.gl.chart.Axis;
import nitnc.kotanilab.trainer.gl.chart.Chart;
import nitnc.kotanilab.trainer.gl.chart.plot.GideLine;
import nitnc.kotanilab.trainer.gl.chart.plot.LinePlot;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.main.ACF;
import nitnc.kotanilab.trainer.math.Unit;
import nitnc.kotanilab.trainer.math.point.Point;
import nitnc.kotanilab.trainer.math.series.*;
import nitnc.kotanilab.trainer.util.CsvLogger;

import java.awt.*;
import java.util.Arrays;
import java.util.function.DoubleConsumer;

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

    private ShiftedSeries<Point> heartRate = new ShiftedSeries<>(200.0, 30.0, Unit.sec(), Unit.arb("HR"), 60.0);

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
        Chart diffChart = createWaveChart("Differential", 6, Unit.v(), -3, 3);
        diffChart.getPlots().add(diffPlot);
        putNewGraphContext("Diff", diffChart);

        panes.addAll(getGraphWrappers());
        getCharts().forEach(Chart::updateLegend);
    }

    @Override
    public void start(double fs, int n) {
        source.setXMax(10.0);
        source.setDecimationNumber(16);
        source.addCallback(IirFilter.execute("bpf0.0001-0.01.txt"));
        graphContextMap.get("Wave").setAxisSetter((x,y) -> {
            x.setMax(hrCalcLength / source.getSamplingFrequency());
            x.setSize(hrCalcLength / source.getSamplingFrequency() / 8.0);
        });
        graphContextMap.values().forEach(graphContext -> graphContext.confirm(masterPane));
        fft = new OouraFft(hrCalcLength);
        logger = new CsvLogger("HR.csv");
        updatePreviousTime();
    }

    @Override
    public void stop() {
        super.stop();
        heartRate.clear();
        logger.close();
    }

    @Override
    public void execute() {
        Wave hrWave = source.getWave(hrCalcLength);
        if (graphContextMap.get("Wave").isVisible()) {
            Wave tmpWave = source.getWave(hrCalcLength);
            Wave wave1 = tmpWave.stream().lastCutX(wavePlot.getXAxis().getRange()).zeroX(wavePlot.getXAxis().getMax()).to(tmpWave::from);
            graphContextMap.get("Wave").ifVisible(context -> wavePlot.getLine().getVectorList().set(wave1.getXList(), wave1.getYList()));
        }
        if (source.available(hrCalcLength)) {
            if (isPassedInterval(1.0)) {
                if (graphContextMap.get("HR").isVisible()) {
                    double[] acf = ACF.wienerKhinchin(fft, hrWave.getYList());
                    graphContextMap.get("ACF").ifVisible(graph -> {
                        double[] acfX = new double[acf.length];
                        double[] acfY = new double[acf.length];
                        for (int i = 0; i < acf.length; i++) {
                            acfX[i] = i;
                            acfY[i] = acf[i] / acf[0];
                        }
                        acfPlot.getLine().getVectorList().set(acfX, acfY);
                    });
                    graphContextMap.get("Diff").ifVisible(graph -> {
                        Wave diffWave = hrWave.stream().biMapXY(SeriesStream.differentiate()).replaceY(y -> y / 10).to(hrWave::from);
                        diffPlot.getLine().getVectorList().set(diffWave.getXList(), diffWave.getYList());
                    });
                    double hr = hrWave.getSamplingFrequency() / ACF.pickPeekIndex(acf) * 60.0;
                    Point hrPoint = new Point(hrWave.getStartTime(), hr);
                    logger.print(hrPoint);
                    heartRate.add(hrPoint);
                    hrPlot.getLine().getVectorList().set(heartRate.getXList(), heartRate.getYList());
                    maxHRGide.setPosition(HrController.getMaxHR(age));
                    targetHRGide.setPosition(HrController.getMaxHR(age) * HrController.OPT_MET); // 最大心拍数に運動強度をかけたもの
                    hrEvent.accept(hr);
                }

                updatePreviousTime();
            }
        }
    }

    public void setHrEvent(DoubleConsumer hrEvent) {
        this.hrEvent = hrEvent;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
