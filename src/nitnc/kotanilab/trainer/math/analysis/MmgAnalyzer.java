package nitnc.kotanilab.trainer.math.analysis;

import nitnc.kotanilab.trainer.fft.wrapper.Fft;
import nitnc.kotanilab.trainer.fft.wrapper.OouraFft;
import nitnc.kotanilab.trainer.gl.chart.*;
import nitnc.kotanilab.trainer.gl.chart.plot.GideLine;
import nitnc.kotanilab.trainer.gl.chart.plot.LinePlot;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.math.Unit;
import nitnc.kotanilab.trainer.math.point.Point;
import nitnc.kotanilab.trainer.math.point.PointLogY;
import nitnc.kotanilab.trainer.math.series.*;
import nitnc.kotanilab.trainer.util.CsvLogger;
import nitnc.kotanilab.trainer.util.Utl;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleUnaryOperator;

public class MmgAnalyzer extends Analyzer {

    protected RealSeries<Point> mdfSeries;
    protected RealSeries<Point> pfSeries;
    protected RealSeries<Point> rmsSeries;
    protected Fft fft;
    protected int number;
    protected double waveXMax;
    protected double waveYMax;
    protected double waveYMin;
    private CsvLogger logger;
    List<DoubleUnaryOperator> filters = new ArrayList<>();
    protected DoubleConsumer mfCallback = v -> {
    };
    protected LinePlot wavePlot;
    protected LinePlot spectrumPlot;
    protected LinePlot mdfPlot;
    protected LinePlot pfPlot;
    protected LinePlot rmsPlot;
    protected GideLine mdfGide = new GideLine("MF", 0.0, Color.RED, 2.0, true);

    public MmgAnalyzer(Pane masterPane) {
        this(masterPane, "MMG",
                createWaveChart("Wave", 1, new Unit("Acceleration", "m/s/s"), 5),
                createSpectrumChart("Spectrum", 0.01, 100, new Axis(Unit.db("Amplitude").toString(), -200, 0, 25)),
                createTimeSeriesChart("MDF and PF", 60.0, new LogAxis("Frequency[Hz]", 1, 100.0)),
                createWaveChart("RMS", 10, Unit.v(), 0, 5)
        );
        filters.addAll(Arrays.asList(
                x -> (x - 2.5) / 1.0,
                IirFilter.execute("bpf0.001-0.2.txt"),
                IirFilter.execute("bef0.048-0.052.txt")
        ));
        spectrumPlot.getLine().setThick(2.0);
    }

    protected MmgAnalyzer(Pane masterPane, String name, Chart waveChart, Chart spectrumChart, Chart freqChart, Chart rmsChart) {
        super(masterPane, name);

        wavePlot = new LinePlot("Wave");
        waveChart.getPlots().add(wavePlot);
        putNewGraphContext(waveChart);

        spectrumPlot = new LinePlot("Spectrum");
        spectrumChart.getPlots().addAll(Arrays.asList(spectrumPlot, mdfGide));
        putNewGraphContext(spectrumChart);

        mdfPlot = new LinePlot("MDF");
        pfPlot = new LinePlot("PF");
        freqChart.getPlots().addAll(Arrays.asList(mdfPlot, pfPlot));
        putNewGraphContext("Frequency", freqChart);

        rmsPlot = new LinePlot("RMS");
        rmsChart.getPlots().add(rmsPlot);
        putNewGraphContext(rmsChart);

        getCharts().forEach(Chart::updateLegend);

        waveXMax = 0.1;
        waveYMax = 3.0;
        waveYMin = -waveYMax;
    }

    @Override
    public void start(double fs, int n) {
        source.setXMax(fs * n);
        source.addCallback(filters);
        graphContextMap.get("Wave").setAxisSetter((x, y) -> {
            x.setMax(waveXMax);
            x.setSize(waveXMax / 10);
            y.setMax(waveYMax);
            y.setMin(waveYMin);
        });
        graphContextMap.get("Spectrum").setAxisSetter((x, y) -> {
            x.setMin(Math.pow(10.0, Utl.ceil(Math.log10(fs / n))));
            x.setMax(fs / 2.0);
        });
        graphContextMap.get("Frequency").setAxisSetter((x, y) -> {
            y.setMax(fs / 2.0);
        });
        graphContextMap.values().forEach(graphContext -> graphContext.confirm(masterPane));

        mdfSeries = new ShiftedSeries<>(fs / 2, 0.0, Unit.sec(), Unit.hz(), 60);
        pfSeries = new ShiftedSeries<>(fs / 2, 0.0, Unit.sec(), Unit.hz(), 60);
        rmsSeries = new ShiftedSeries<>(5.0, 0.0, Unit.sec(), Unit.v(), 10);

        number = n;
        fft = new OouraFft(n);
        updatePreviousTime();
        logger = new CsvLogger(title + ".csv");
    }

    @Override
    public void stop() {
        super.stop();
        mdfSeries.clear();
        pfSeries.clear();
        Arrays.asList(wavePlot, spectrumPlot, mdfPlot, pfPlot, rmsPlot).forEach(plot ->plot.getLine().getVectorList().clear());
        logger.close();
    }

    @Override
    public void execute() {
        if (graphContextMap.get("Wave").isVisible()) {
            Wave wave = source.getWave(1.0);
            graphContextMap.get("Wave").ifVisible(context ->
                    context.setToVectorList(
                            wave.stream().lastCutX(wavePlot.getXAxis().getRange()).zeroX(wavePlot.getXAxis().getMax()),
                            wavePlot.getLine().getVectorList()
                    )
            );
        }
        if (source.available(number) && isPassedInterval(1.0)) {
            Wave tmpWave = source.getWave(number);
            Wave wave = tmpWave.stream().to(tmpWave::from);
            // fft処理
            tmpWave = wave.stream().fill(fft.getLength(), 0.0, 1 / wave.getSamplingFrequency()).replaceYByIndex(Wave::hamming, (a, b) -> a * b).to(wave::from);
            Signal<Double, Point> tmpSpectrum = fft.dft(tmpWave).getPowerSpectrum();
            Signal<Double, Point> spectrum = tmpSpectrum.stream().toSeries(Point::new, tmpSpectrum::from);
            // スペクトラムのdB化
            // Signal<Double, PointLogY> powerSpectrum = spectrum.stream().toSeries((x, y) -> new PointLogY(x, y, spectrum.getYMax()), spectrum::fromLogY);
            graphContextMap.get("Spectrum").ifVisible(context ->
                    context.setToVectorList(spectrum.stream().replaceY(y ->
                            PointLogY.dB(y, spectrum.getYMax())), spectrumPlot.getLine().getVectorList()));

            Point medianPoint = new Point(wave.getStartTime(), spectrum.stream().to(SeriesStream::getMedian).getX());
            logger.print(medianPoint);
            mfCallback.accept(medianPoint.getY());
            mdfSeries.add(medianPoint);
            pfSeries.add(new Point(wave.getStartTime(), spectrum.stream().to(SeriesStream::getPeek).getX()));

            mdfGide.setPosition(medianPoint.getY());

            graphContextMap.get("Frequency").ifVisible(context -> {
                context.setToVectorList(mdfSeries.stream(), mdfPlot.getLine().getVectorList());
                context.setToVectorList(pfSeries.stream(), pfPlot.getLine().getVectorList());
            });

            Wave rmsWave = source.getWave(3.0);
            double sqAve = rmsWave.stream().replaceY(y -> y * y).reduce((a, b) -> a + b) / rmsWave.size();
            double rms = Math.sqrt(sqAve);
            rmsSeries.add(new Point(rmsWave.getStartTime(), rms));
            graphContextMap.get("RMS").ifVisible(context ->
                    context.setToVectorList(rmsSeries.stream(), rmsPlot.getLine().getVectorList()));


            updatePreviousTime();
        }
    }

    public void setMfCallback(DoubleConsumer mfCallback) {
        this.mfCallback = mfCallback;
    }
}
