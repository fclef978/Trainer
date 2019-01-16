package nitnc.kotanilab.trainer.math.analysis;

import nitnc.kotanilab.trainer.fft.wrapper.Fft;
import nitnc.kotanilab.trainer.fft.wrapper.OouraFft;
import nitnc.kotanilab.trainer.gl.chart.Axis;
import nitnc.kotanilab.trainer.gl.chart.Chart;
import nitnc.kotanilab.trainer.gl.chart.LineGraph;
import nitnc.kotanilab.trainer.gl.chart.LogAxis;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.math.point.Point;
import nitnc.kotanilab.trainer.math.point.PointLogY;
import nitnc.kotanilab.trainer.math.series.*;
import nitnc.kotanilab.trainer.util.Utl;

import java.util.Arrays;

public class MmgMicAnalyzer extends Analyzer {

    private LineGraph freqGraph;
    private Chart freqChart;
    private Pane wavePane;
    private LineGraph spectrumGraph;
    private Chart spectrumChart;
    private Pane spectrumPane;
    private LineGraph waveGraph;
    private Chart waveChart;
    private Pane freqPane;
    private RealSeries<Point> median;
    private RealSeries<Point> peek;
    private Fft fft;
    private int number;
    private boolean waveVisible = false;
    private boolean spectrumVisible = false;
    private boolean freqVisible = false;

    public MmgMicAnalyzer(Pane masterPane) {
        super(masterPane, "MMG(mic.)");

        freqGraph = createTimeSeriesGraph(60.0, new LogAxis("Frequency[Hz]", 1, 100.0, 0.1), "Median", "Peek");
        freqChart = new Chart("Median and Peek Frequency", freqGraph);
        freqPane = createWrapperPane(1);
        freqPane.getChildren().add(freqChart);


        spectrumGraph = createSpectrumGraph(0.01, 100, new Axis(Unit.db("Amplitude").toString(), -100, 0, 10), "Spectrum");
        spectrumChart = new Chart("Spectrum", spectrumGraph);
        spectrumPane = createWrapperPane(1);
        spectrumPane.getChildren().add(spectrumChart);


        waveGraph = createWaveGraph(1, Unit.v(), 1, "Wave");
        waveChart = new Chart("Wave", waveGraph);
        wavePane = createWrapperPane(1);
        wavePane.getChildren().add(waveChart);

        panes.addAll(Arrays.asList(wavePane, spectrumPane, freqPane));
    }

    @Override
    public Analyzer setVisible(boolean... visible) {
        waveVisible = visible[0];
        spectrumVisible = visible[1];
        freqVisible = visible[2];
        return this;
    }

    @Override
    public void start(double fs, int n, double waveXMax, double waveYMax) {
        super.start(fs, n, waveXMax, waveYMax);
        source.setXMax(fs * n);
        source.addCallback(
                x -> (x - 2.5) / 1.0,
                IirFilter.execute("bpf0.001-0.2.txt"),
                IirFilter.execute("bef0.048-0.052.txt")
        );
        if (waveVisible) {
            waveGraph.getXAxis().setMax(waveXMax);
            waveGraph.getYAxis().setMax(0.5);
            waveGraph.getYAxis().setMin(-0.5);
            waveChart.setGraph();
            masterPane.getChildren().addAll(wavePane);
        }
        if (spectrumVisible) {
            spectrumGraph.getXAxis().setMin(Math.pow(10.0, Utl.ceil(Math.log10(fs / n))));
            spectrumGraph.getXAxis().setMax(fs / 2.0);
            spectrumChart.setGraph();
            masterPane.getChildren().addAll(spectrumPane);
        }
        if (freqVisible) {
            freqGraph.getYAxis().setMax(fs / 2.0);
            freqChart.setGraph();
            masterPane.getChildren().addAll(freqPane);
        }

        median = new ShiftedSeries<>(fs / 2, 0.0, Unit.sec(), Unit.hz(), 60);
        peek = new ShiftedSeries<>(fs / 2, 0.0, Unit.sec(), Unit.hz(), 60);

        number = n;
        fft = new OouraFft(n);
        updatePreviousTime();
    }

    @Override
    public void stop() {
        clearVectorList(waveGraph);
        clearVectorList(spectrumGraph);
        clearVectorList(freqGraph);
        super.stop();
        median.clear();
        peek.clear();
    }

    @Override
    public void execute() {
        if (waveVisible) {
            Wave tmpWave = source.getWave(1.0);
            Wave wave = tmpWave.stream().lastCutX(waveGraph.getXAxis().getRange()).zeroX(waveGraph.getXAxis().getMax())
                    .to(tmpWave::from);
            waveGraph.getVectorList("Wave").set(wave.getXList(), wave.getYList()); // グラフ登録
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
            Signal<Double, PointLogY> powerSpectrum = spectrum.stream().toSeries((x, y) -> new PointLogY(x, y, 1.0), spectrum::toLogY);
            if (spectrumVisible) {
                spectrumGraph.getVectorList("Spectrum").set(powerSpectrum.getXList(), powerSpectrum.getYList());
            }

            median.add(new Point(wave.getStartTime(), spectrum.stream().to(SeriesStream::getMedian).getX()));
            peek.add(new Point(wave.getStartTime(), spectrum.stream().to(SeriesStream::getPeek).getX()));

            if (freqVisible) {
                freqGraph.getVectorList("Median").set(median.getXList(), median.getYList());
                freqGraph.getVectorList("Peek").set(peek.getXList(), peek.getYList());
            }

            Wave rmsWave = source.getWave(1.0);
            double sqAve = rmsWave.stream().replaceY(y -> y * y).reduce((a, b) -> a + b) / rmsWave.size();
            double rms = Math.sqrt(sqAve);
            // Dbg.p("RMS=", rms);

            updatePreviousTime();
        }
    }

}
