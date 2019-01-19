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
import nitnc.kotanilab.trainer.math.point.Point;
import nitnc.kotanilab.trainer.math.series.*;
import nitnc.kotanilab.trainer.util.CsvLogger;
import nitnc.kotanilab.trainer.util.Dbg;

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

    private ShiftedSeries<Point> heartRate = new ShiftedSeries<>(200.0, 30.0, Unit.sec(), Unit.arb("HR"), 60.0);

    public HrAnalyzer(Pane masterPane) {
        super(masterPane, "HR");
        LineGraph waveGraph = createWaveGraph(1, Unit.v(), -3, 3, "Wave");
        Chart waveChart = new Chart("Wave", waveGraph);
        graphContextMap.put("Wave", new GraphContext(waveGraph, waveChart, createWrapperPane(1), false));

        LineGraph hrGraph = createTimeSeriesGraph(60, new Axis("HR[bpm]", 50.0, 210.0, 20), "HR");
        Chart hrChart = new Chart("Heart Rate", hrGraph);
        graphContextMap.put("HR", new GraphContext(hrGraph, hrChart, createWrapperPane(1), false));

        panes.addAll(getGraphWrappers());
    }

    @Override
    public void start(double fs, int n) {
        source.setXMax(10.0);
        source.setDecimationNumber(16);
        source.addCallback(IirFilter.execute("bpf0.0001-0.01.txt"));
        graphContextMap.get("HR").setGraphSetter(graph -> {
            graph.putGideLine("Maximal", HrController.getMaxHR(age), Color.RED);
            graph.putGideLine("Optimal", HrController.getMaxHR(age) * HrController.OPT_MET, Color.GREEN);
        });
        graphContextMap.values().forEach(graphContext -> graphContext.confirm(masterPane));
        fft = new OouraFft(hrCalcLength);
        logger = new CsvLogger("HR.csv");
        updatePreviousTime();
    }

    @Override
    public void stop() {
        graphContextMap.get("HR").ifVisible(LineGraph::clearGideLine);
        getGraphs().forEach(this::clearVectorList);
        super.stop();
        heartRate.clear();
        logger.close();
    }

    @Override
    public void execute() {
        if (graphContextMap.get("Wave").isVisible()) {
            Wave tmpWave = source.getWave(2.0);
            LineGraph waveGraph = graphContextMap.get("Wave").getGraph();
            Wave wave1 = tmpWave.stream().lastCutX(waveGraph.getXAxis().getRange()).zeroX(waveGraph.getXAxis().getMax()).to(tmpWave::from);
            graphContextMap.get("Wave").update("Wave", wave1.getXList(), wave1.getYList()); // グラフ登録
        }

        if (source.available(hrCalcLength) && isPassedInterval(1.0)) {
            Wave diff = source.getWave(hrCalcLength);
            diff = diff.stream().to(diff::from);

            if (graphContextMap.get("HR").isVisible()) {
                double hr = diff.getSamplingFrequency() / ACF.wienerKhinchin(fft, diff.getYList()) * 60.0;
                Point hrPoint = new Point(diff.getStartTime(), hr);
                logger.print(hrPoint);
                heartRate.add(hrPoint);
                LineGraph hrGraph = graphContextMap.get("HR").getGraph();
                graphContextMap.get("HR").update("HR", heartRate.getXList(), heartRate.getYList());
                hrGraph.setGideLine("Maximal", HrController.getMaxHR(age));
                hrGraph.setGideLine("Optimal", HrController.getMaxHR(age) * HrController.OPT_MET);
                hrEvent.accept(hr);
            }

            updatePreviousTime();
        }
    }

    public void setHrEvent(DoubleConsumer hrEvent) {
        this.hrEvent = hrEvent;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
