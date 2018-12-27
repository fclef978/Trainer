package nitnc.kotanilab.trainer.math.analysis;

import nitnc.kotanilab.trainer.gl.chart.Axis;
import nitnc.kotanilab.trainer.gl.chart.Chart;
import nitnc.kotanilab.trainer.gl.chart.LineGraph;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.math.point.Point;
import nitnc.kotanilab.trainer.math.series.*;
import nitnc.kotanilab.trainer.util.Dbg;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public class HrAnalyzer extends Analyzer {
    private LineGraph waveGraph;
    private Chart waveChart;
    private Pane wavePane;
    private LineGraph diffGraph;
    private Chart diffChart;
    private Pane diffPane;
    private LineGraph hrGraph;
    private Chart hrChart;
    private Pane hrPane;
    private boolean waveVisible = false;
    private boolean hrVisible = false;
    private boolean debugVisible = false;
    private double thresholdLower = 0.0;
    private double thresholdHigher = 0.0;
    private DoubleConsumer hrEvent = value -> {};
    private int age = 20;
    private double optMET = 0.7;

    private ShiftedSeries<Point> heartRate = new ShiftedSeries<>(200.0, 30.0, Unit.sec(), Unit.arb("HR"), 60.0);

    public HrAnalyzer(Pane masterPane) {
        super(masterPane, "HR");
        waveGraph = createWaveGraph(1, Unit.arb("Value"), 0, 5, "Wave");
        waveChart = new Chart("Wave", waveGraph);
        wavePane = createWrapperPane(1);
        wavePane.getChildren().add(waveChart);

        diffGraph = createWaveGraph(6, Unit.arb("Variable"), 3, "Wave", "Differential", "State");
        diffChart = new Chart("Debug", diffGraph);
        diffPane = createWrapperPane(1);
        diffPane.getChildren().add(diffChart);

        hrGraph = createTimeSeriesGraph(60, new Axis("HR", 50.0, 210.0, 20), "HR");
        hrChart = new Chart("Heart Rate", hrGraph);
        hrPane = createWrapperPane(1);
        hrPane.getChildren().add(hrChart);

        panes.addAll(Arrays.asList(wavePane, diffPane, hrPane));
    }

    @Override
    public void start(double fs, int n, double waveXMax, double waveYMax) {
        super.start(fs, n, waveXMax, waveYMax);
        source.setXMax(6.0);
        if (waveVisible) {
            masterPane.getChildren().addAll(wavePane);
        }
        if (debugVisible) {
            masterPane.getChildren().add(diffPane);
            diffGraph.putGideLine("Low", thresholdLower, Color.BLUE);
            diffGraph.putGideLine("High", thresholdHigher, Color.RED);
        }
        if (hrVisible) {
            masterPane.getChildren().add(hrPane);
            hrGraph.putGideLine("Maximal", getMaxHR(age), Color.RED);
            hrGraph.putGideLine("Optimal", getMaxHR(age) * optMET, Color.GREEN);
        }
        updatePreviousTime();
    }

    @Override
    public void stop() {
        if (hrVisible) {
            hrGraph.clearGideLine();
        }
        if (debugVisible) {
            diffGraph.clearGideLine();
        }
        clearVectorList(waveGraph);
        clearVectorList(diffGraph);
        clearVectorList(hrGraph);
        super.stop();
        heartRate.clear();
    }

    @Override
    public void execute() {
        if (waveVisible) {
            Wave tmpWave = source.getWave(2.0);
            Wave wave1 = tmpWave.stream().lastCutX(waveGraph.getXAxis().getRange()).zeroX(waveGraph.getXAxis().getMax()).to(tmpWave::from);
            waveGraph.getVectorList("Wave").set(wave1.getXList(), wave1.getYList()); // グラフ登録
        }

        if (source.available(6.0) && isPassedInterval(1.0)) {
            Wave diff = source.getWave(6.0);
            diff = diff.stream().to(diff::from);
            if (debugVisible) {
                diffGraph.getVectorList("Wave").set(diff.getXList(), diff.getYList());
            }
            StateMachine stateMachine = new StateMachine(thresholdLower, thresholdHigher);
            diff = diff.stream().biMapXY(SeriesStream.differentiate()).replaceY(y -> y / 10.0).to(diff::from);
            if (debugVisible) {
                diffGraph.getVectorList("Differential").set(diff.getXList(), diff.getYList());
            }
            diff.forEach(stateMachine::run);
            if (debugVisible) {
                diffGraph.getVectorList("State").set(stateMachine.wave.getXList(), stateMachine.wave.getYList());
                diffGraph.setGideLine("Low", thresholdLower);
                diffGraph.setGideLine("High", thresholdHigher);
            }

            if (hrVisible) {
                double hr = 60.0 / stateMachine.getDelta();
                heartRate.add(new Point(diff.getStartTime(), hr));
                hrGraph.getVectorList("HR").set(heartRate.getXList(), heartRate.getYList());
                hrGraph.setGideLine("Maximal", getMaxHR(age));
                hrGraph.setGideLine("Optimal", getMaxHR(age) * optMET);
                hrEvent.accept(hr);
            }

            updatePreviousTime();
        }
    }

    @Override
    public Analyzer setVisible(boolean... visible) {
        waveVisible = visible[0];
        hrVisible = visible[1];
        debugVisible = visible[2];
        return this;
    }

    private static double getMaxHR(int age) {
        return 220 - age;
    }

    public void setThresholdLower(double thresholdLower) {
        this.thresholdLower = thresholdLower;
    }

    public void setThresholdHigher(double thresholdHigher) {
        this.thresholdHigher = thresholdHigher;
    }

    public void setHrEvent(DoubleConsumer hrEvent) {
        this.hrEvent = hrEvent;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private class StateMachine {
        private double thresholdLower;
        private double thresholdHigher;
        private String state = "Idle";
        private double previousTime = -1;
        private double tmpTime = 0.0;
        private double delta = 0.0;
        private int count = 0;
        private RealSeries<Point> wave;

        public StateMachine(double thresholdLower, double thresholdHigher) {
            this.thresholdLower = thresholdLower;
            this.thresholdHigher = thresholdHigher;
            wave = new RealSeries<>(thresholdHigher, thresholdLower);
        }

        public void run(Point point) {
            switch (state) {
                case "Idle":
                    if (point.getY() > thresholdHigher) {
                        state = "PulseLow";
                        tmpTime = point.getX();
                    }
                    wave.add(new Point(point.getX(), 0));
                    break;
                case "PulseLow":
                    double currentTime = point.getX();
                    if (point.getY() < thresholdLower) {
                        state = "Idle";
                        tmpTime = point.getX();
                        if (previousTime >= 0) {
                            delta += tmpTime - previousTime;
                            count++;
                        }
                        previousTime = tmpTime;
                        wave.add(new Point(point.getX(), thresholdLower));
                    } else if (currentTime - tmpTime > 0.3 && point.getY() > thresholdHigher) {
                        tmpTime = point.getX();
                        wave.add(new Point(point.getX(), thresholdHigher));
                    } else {
                        wave.add(new Point(point.getX(), thresholdHigher));
                    }
                    break;
            }
        }

        public String getState() {
            return state;
        }

        public double getDelta() {
            return count == 0 ? -1 : (delta / count);
        }
    }
}
