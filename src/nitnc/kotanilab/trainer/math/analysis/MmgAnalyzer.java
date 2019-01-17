package nitnc.kotanilab.trainer.math.analysis;

import nitnc.kotanilab.trainer.fft.wrapper.Fft;
import nitnc.kotanilab.trainer.fft.wrapper.OouraFft;
import nitnc.kotanilab.trainer.gl.chart.*;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.math.point.Point;
import nitnc.kotanilab.trainer.math.point.PointLogY;
import nitnc.kotanilab.trainer.math.series.*;
import nitnc.kotanilab.trainer.util.Utl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

public class MmgAnalyzer extends Analyzer {

    protected RealSeries<Point> median;
    protected RealSeries<Point> peek;
    protected RealSeries<Point> rms;
    protected Fft fft;
    protected int number;
    protected double waveXMax;
    protected double waveYMax;
    protected double waveYMin;
    List<DoubleUnaryOperator> filters = new ArrayList<>();

    public MmgAnalyzer(Pane masterPane) {
        this(masterPane, "MMG",
                createWaveGraph(1, new Unit("Acceleration", "m/s/s"), 5, "Wave"),
                createSpectrumGraph(0.01, 100, new Axis(Unit.db("Amplitude").toString(), -100, 0, 10), "Spectrum"),
                createTimeSeriesGraph(60.0, new LogAxis("Frequency[Hz]", 1, 100.0, 0.1), "Median", "Peek"),
                createWaveGraph(10, Unit.v(), 0, 5, "RMS")
        );
        filters.addAll(Arrays.asList(
                x -> (x - 2.5) / 1.0,
                IirFilter.execute("bpf0.001-0.2.txt"),
                IirFilter.execute("bef0.048-0.052.txt")
        ));
    }

    protected MmgAnalyzer(Pane masterPane, String name, LineGraph waveGraph, LineGraph spectrumGraph, LineGraph freqGraph, LineGraph rmsGraph) {
        super(masterPane, name);

        Chart waveChart = new Chart("Wave", waveGraph);
        graphContextMap.put("Wave", new GraphContext(waveGraph, waveChart, createWrapperPane(1), false));

        Chart spectrumChart = new Chart("Spectrum", spectrumGraph);
        graphContextMap.put("Spectrum", new GraphContext(spectrumGraph, spectrumChart, createWrapperPane(1), false));

        Chart freqChart = new Chart("Median and Peek Frequency", freqGraph);
        graphContextMap.put("Frequency", new GraphContext(freqGraph, freqChart, createWrapperPane(1), false));

        Chart rmsChart = new Chart("RMS", rmsGraph);
        graphContextMap.put("RMS", new GraphContext(rmsGraph, rmsChart, createWrapperPane(1), false));

        panes.addAll(getGraphWrappers());

        waveXMax = 1.0;
        waveYMax = 3.0;
        waveYMin = -waveYMax;
    }

    @Override
    public void start(double fs, int n) {
        source.setXMax(fs * n);
        source.addCallback(filters);
        graphContextMap.get("Wave").setGraphSetter(graph -> {
            graph.getXAxis().setMax(waveXMax);
            graph.getYAxis().setMax(waveYMax);
            graph.getYAxis().setMin(waveYMin);
        });
        graphContextMap.get("Spectrum").setGraphSetter(graph -> {
            graph.getXAxis().setMin(Math.pow(10.0, Utl.ceil(Math.log10(fs / n))));
            graph.getXAxis().setMax(fs / 2.0);
        });
        graphContextMap.get("Frequency").setGraphSetter(graph -> {
            graph.getYAxis().setMax(fs / 2.0);
        });
        graphContextMap.values().forEach(graphContext -> graphContext.confirm(masterPane));

        median = new ShiftedSeries<>(fs / 2, 0.0, Unit.sec(), Unit.hz(), 60);
        peek = new ShiftedSeries<>(fs / 2, 0.0, Unit.sec(), Unit.hz(), 60);
        rms = new ShiftedSeries<>(5.0, 0.0, Unit.sec(), Unit.v(), 10);

        number = n;
        fft = new OouraFft(n);
        updatePreviousTime();
    }

    @Override
    public void stop() {
        getGraphs().forEach(this::clearVectorList);
        super.stop();
        median.clear();
        peek.clear();
    }

    @Override
    public void execute() {
        if (graphContextMap.get("Wave").isVisible()) {
            Wave tmpWave = source.getWave(1.0);
            LineGraph waveGraph = graphContextMap.get("Wave").getGraph();
            Wave wave = tmpWave.stream().lastCutX(waveGraph.getXAxis().getRange()).zeroX(waveGraph.getXAxis().getMax())
                    .to(tmpWave::from);
            graphContextMap.get("Wave").update("Wave", wave.getXList(), wave.getYList());
        }
        if (source.available(number) && isPassedInterval(1.0)) {
            Wave tmpWave = source.getWave(number);
            Wave wave = tmpWave.stream()
                    .to(tmpWave::from);
            // fft処理
            tmpWave = wave.stream().fill(fft.getLength(), 0.0, 1 / wave.getSamplingFrequency())/*.replaceYByIndex(Wave::hanning, (a, b) -> a * b)*/.to(wave::from);
            Signal<Double, Point> tmpSpectrum = fft.dft(tmpWave).getPowerSpectrum();
            Signal<Double, Point> spectrum = tmpSpectrum.stream().toSeries(Point::new, tmpSpectrum::from);
            // スペクトラムのdB化
            Signal<Double, PointLogY> powerSpectrum = spectrum.stream().toSeries((x, y) -> new PointLogY(x, y, spectrum.getYMax()), spectrum::toLogY);
            graphContextMap.get("Spectrum").update("Spectrum", powerSpectrum.getXList(), powerSpectrum.getYList());

            median.add(new Point(wave.getStartTime(), spectrum.stream().to(SeriesStream::getMedian).getX()));
            peek.add(new Point(wave.getStartTime(), spectrum.stream().to(SeriesStream::getPeek).getX()));

            graphContextMap.get("Frequency").update("Median", median.getXList(), median.getYList());
            graphContextMap.get("Frequency").update("Peek", peek.getXList(), peek.getYList());

            Wave rmsWave = source.getWave(3.0);
            double sqAve = rmsWave.stream().replaceY(y -> y * y).reduce((a, b) -> a + b) / rmsWave.size();
            double rms = Math.sqrt(sqAve);
            this.rms.add(new Point(rmsWave.getStartTime(), rms));
            graphContextMap.get("RMS").update("RMS", this.rms.getXList(), this.rms.getYList());


            updatePreviousTime();
        }
    }

}
