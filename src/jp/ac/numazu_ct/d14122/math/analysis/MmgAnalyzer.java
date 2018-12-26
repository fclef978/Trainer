package jp.ac.numazu_ct.d14122.math.analysis;

import jp.ac.numazu_ct.d14122.fft.wrapper.Fft;
import jp.ac.numazu_ct.d14122.fft.wrapper.OouraFft;
import jp.ac.numazu_ct.d14122.gl.chart.Axis;
import jp.ac.numazu_ct.d14122.gl.chart.Chart;
import jp.ac.numazu_ct.d14122.gl.chart.LineGraph;
import jp.ac.numazu_ct.d14122.gl.chart.LogAxis;
import jp.ac.numazu_ct.d14122.gl.pane.Pane;
import jp.ac.numazu_ct.d14122.gl.pane.StackPane;
import jp.ac.numazu_ct.d14122.math.point.Point;
import jp.ac.numazu_ct.d14122.math.point.PointLogY;
import jp.ac.numazu_ct.d14122.math.series.*;
import jp.ac.numazu_ct.d14122.util.Dbg;
import jp.ac.numazu_ct.d14122.util.Utl;

import java.util.Arrays;

public class MmgAnalyzer extends Analyzer {

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

    public MmgAnalyzer(Pane masterPane) {
        super(masterPane, "MMG");

        freqGraph = createTimeSeriesGraph(60.0, new LogAxis("Frequency[Hz]", 1, 100.0, 0.1), "Median", "Peek");
        freqChart = new Chart("Median and Peek Frequency", freqGraph);
        freqPane = createWrapperPane(1);
        freqPane.getChildren().add(freqChart);


        spectrumGraph = createSpctrumGraph(1, 100, new Axis(Unit.db("Amplitude").toString(), -100, 0, 10), "Spectrum");
        spectrumChart = new Chart("Spectrum", spectrumGraph);
        spectrumPane = createWrapperPane(1);
        spectrumPane.getChildren().add(spectrumChart);


        waveGraph = createWaveGraph(1, new Unit("Acceleration", "m/s/s"), 5, "Wave");
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
        if (waveVisible) {
            waveGraph.getXAxis().setMax(waveXMax);
            waveGraph.getYAxis().setMax(waveYMax);
            waveGraph.getYAxis().setMin(-waveYMax);
            waveChart.setGraph();
            masterPane.getChildren().addAll(wavePane);
        }
        if (spectrumVisible) {
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
            Wave wave = tmpWave.stream().lastCutX(waveGraph.getXAxis().getRange()).zeroX(waveGraph.getXAxis().getMax()).replaceY(x -> (x - 2.5) / 1.0).to(tmpWave::from);
            waveGraph.getVectorList("Wave").set(wave.getXList(), wave.getYList()); // グラフ登録
        }
        if (source.available(number) && isPassedInterval(1.0)) {
            Wave tmpWave = source.getWave(number);
            Wave wave = tmpWave.stream().replaceY(x -> (x - 2.5) / 1.0).to(tmpWave::from);
            // fft処理
            tmpWave = wave.stream().fill(fft.getLength(), 0.0, 1 / wave.getSamplingFrequency()).replaceYByIndex(Wave::hanning, (a, b) -> a * b).to(wave::from);
            Signal<Double, Point> tmpSpectrum = fft.rdft(tmpWave).getPowerSpectrum();
            Signal<Double, Point> spectrum = tmpSpectrum.stream().cutDown(1).toSeries(Point::new, tmpSpectrum::from);
            // スペクトラムのdB化
            Signal<Double, PointLogY> powerSpectrum = spectrum.stream().toSeries((x, y) -> new PointLogY(x, y, spectrum.getYMax()), spectrum::toLogY);
            if (spectrumVisible) {
                spectrumGraph.getVectorList("Spectrum").set(powerSpectrum.getXList(), powerSpectrum.getYList());
            }

            median.add(new Point(wave.getStartTime(), spectrum.stream().to(SeriesStream::getMedian).getX()));
            peek.add(new Point(wave.getStartTime(), spectrum.stream().to(SeriesStream::getPeek).getX()));

            if (freqVisible) {
                freqGraph.getVectorList("Median").set(median.getXList(), median.getYList());
                freqGraph.getVectorList("Peek").set(peek.getXList(), peek.getYList());
            }
            updatePreviousTime();
        }
    }

}
