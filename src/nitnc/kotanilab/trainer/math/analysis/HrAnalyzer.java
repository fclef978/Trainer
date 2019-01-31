package nitnc.kotanilab.trainer.math.analysis;

import nitnc.kotanilab.trainer.fft.wrapper.Fft;
import nitnc.kotanilab.trainer.fft.wrapper.OouraFft;
import nitnc.kotanilab.trainer.fx.controller.HrController;
import nitnc.kotanilab.trainer.gl.chart.Axis;
import nitnc.kotanilab.trainer.gl.chart.Chart;
import nitnc.kotanilab.trainer.gl.chart.GraphContext;
import nitnc.kotanilab.trainer.gl.chart.LineGraph;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.main.ACF;
import nitnc.kotanilab.trainer.math.Unit;
import nitnc.kotanilab.trainer.math.point.Point;
import nitnc.kotanilab.trainer.math.series.*;
import nitnc.kotanilab.trainer.util.CsvLogger;

import java.awt.*;
import java.util.function.DoubleConsumer;

public class HrAnalyzer extends Analyzer {
    private Fft fft;
    private int hrCalcLength = 512;
    private DoubleConsumer hrEvent = value -> {
    };
    private int age = 20;
    private CsvLogger logger;

    private ShiftedSeries<Point> heartRate = new ShiftedSeries<>(200.0, 30.0, Unit.sec(), Unit.arb("HR"), 60.0);

    public HrAnalyzer(Pane masterPane) {
        super(masterPane, "HR");
        LineGraph waveGraph = createWaveGraph(1, Unit.v(), -2, 2, "Wave");
        Chart waveChart = new Chart("Wave", waveGraph);
        graphContextMap.put("Wave", new GraphContext(waveGraph, waveChart, createWrapperPane(1), false));

        LineGraph hrGraph = createTimeSeriesGraph(60, new Axis("HR[bpm]", 50.0, 210.0, 20), "HR");
        Chart hrChart = new Chart("Heart Rate", hrGraph);
        graphContextMap.put("HR", new GraphContext(hrGraph, hrChart, createWrapperPane(1), false));

        LineGraph acfGraph = createGraph(
                new Axis("Value", 0.0, hrCalcLength / 2, hrCalcLength / 20),
                new Axis("Value", -1.0, 1.0, 0.5),
                "ACF"
        );
        addGraphContext("ACF", acfGraph);

        LineGraph diffGraph = createWaveGraph(6, Unit.v(), -3, 3, "Diff");
        addGraphContext("Diff", diffGraph);

        panes.addAll(getGraphWrappers());
    }

    @Override
    public void start(double fs, int n) {
        source.setXMax(10.0);
        source.setDecimationNumber(16);
        source.addCallback(IirFilter.execute("bpf0.0001-0.01.txt"));
        graphContextMap.get("Wave").setGraphSetter(graph -> {
            graph.getXAxis().setMax(hrCalcLength / source.getSamplingFrequency());
            graph.getXAxis().setSize(hrCalcLength / source.getSamplingFrequency() / 8.0);
        });
        graphContextMap.get("HR").setGraphSetter(graph -> {
            graph.putGideLine("Maximal", HrController.getMaxHR(age), Color.RED);
            graph.putGideLine("Target", HrController.getMaxHR(age) * HrController.OPT_MET, Color.GREEN.darker());
        });
        graphContextMap.values().forEach(graphContext -> graphContext.confirm(masterPane));
        fft = new OouraFft(hrCalcLength);
        logger = new CsvLogger("HR.csv");
        updatePreviousTime();
    }

    @Override
    public void stop() {
        graphContextMap.get("HR").ifVisible(graph->graph.getGraph().clearGideLine());
        getGraphs().forEach(this::clearVectorList);
        super.stop();
        heartRate.clear();
        logger.close();
    }

    @Override
    public void execute() {

        Wave hrWave = source.getWave(hrCalcLength);
        if (graphContextMap.get("Wave").isVisible()) {
            Wave tmpWave = source.getWave(hrCalcLength);
            LineGraph waveGraph = graphContextMap.get("Wave").getGraph();
            Wave wave1 = tmpWave.stream().lastCutX(waveGraph.getXAxis().getRange()).zeroX(waveGraph.getXAxis().getMax()).to(tmpWave::from);
            graphContextMap.get("Wave").update("Wave", wave1.getXList(), wave1.getYList()); // グラフ登録
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
                        graph.update("ACF", acfX, acfY);
                    });
                    graphContextMap.get("Diff").ifVisible(graph -> {
                        Wave diffWave = hrWave.stream().biMapXY(SeriesStream.differentiate()).replaceY(y -> y / 10).to(hrWave::from);
                        graph.getGraph().getVectorList("Diff").set(diffWave.getXList(), diffWave.getYList());
                        graph.update("Diff", diffWave.getXList(), diffWave.getYList());
                    });
                    double hr = hrWave.getSamplingFrequency() / ACF.pickPeekIndex(acf) * 60.0;
                    Point hrPoint = new Point(hrWave.getStartTime(), hr);
                    logger.print(hrPoint);
                    heartRate.add(hrPoint);
                    LineGraph hrGraph = graphContextMap.get("HR").getGraph();
                    graphContextMap.get("HR").update("HR", heartRate.getXList(), heartRate.getYList());
                    hrGraph.setGideLine("Maximal", HrController.getMaxHR(age));
                    hrGraph.setGideLine("Target", HrController.getMaxHR(age) * HrController.OPT_MET);
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
