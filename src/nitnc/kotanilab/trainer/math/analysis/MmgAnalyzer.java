package nitnc.kotanilab.trainer.math.analysis;

import nitnc.kotanilab.trainer.fft.wrapper.Fft;
import nitnc.kotanilab.trainer.fft.wrapper.OouraFft;
import nitnc.kotanilab.trainer.gl.chart.*;
import nitnc.kotanilab.trainer.gl.chart.plot.GideLine;
import nitnc.kotanilab.trainer.gl.chart.plot.LinePlot;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.gl.util.PeriodicTask;
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
import java.util.concurrent.TimeUnit;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleUnaryOperator;

/**
 * 筋音図の解析を行うAnalyzerです。
 * 継承することで筋電図への転用が可能です。
 */
public class MmgAnalyzer extends Analyzer {

    /**
     * 中央周波数のSeries
     */
    protected RealSeries<Point> mdfSeries;
    /**
     * 最大周波数のSeries
     */
    protected RealSeries<Point> pfSeries;
    /**
     * RMSのSeries
     */
    protected RealSeries<Point> rmsSeries;
    /**
     * FFTオブジェクト
     */
    protected Fft fft;
    /**
     * FFTのサンプル数
     */
    protected int number;
    /**
     * 波形グラフのX最大値
     */
    protected double waveXMax;
    /**
     * 波形グラフのY最大値
     */
    protected double waveYMax;
    /**
     * 波形グラフのY最小値
     */
    protected double waveYMin;
    private CsvLogger logger;
    /**
     * デジタルフィルタのList
     */
    protected List<DoubleUnaryOperator> filters = new ArrayList<>();
    /**
     * 解析した中央周波数を受け取るコールバック
     */
    protected DoubleConsumer mdfCallback = v -> {
    };
    /**
     * 波形のPlot
     */
    protected LinePlot wavePlot;
    /**
     * スペクトラムのPlot
     */
    protected LinePlot spectrumPlot;
    /**
     * 中央周波数のPlot
     */
    protected LinePlot mdfPlot;
    /**
     * 最大周波数のPlot
     */
    protected LinePlot pfPlot;
    /**
     * RMSのPlot
     */
    protected LinePlot rmsPlot;
    /**
     * 中央周波数のGideLine
     */
    protected GideLine mdfGide = new GideLine("MF", 0.0, Color.RED, 2.0, true);
    private PeriodicTask waveTask = new PeriodicTask(this::waveAnalyze, 30, TimeUnit.MILLISECONDS);
    private PeriodicTask freqTask = new PeriodicTask(this::freqAnalyze, 1000, TimeUnit.MILLISECONDS);

    /**
     * コンストラクタです。
     *
     * @param masterPane グラフの親ペイン
     */
    public MmgAnalyzer(Pane masterPane) {
        this(masterPane, "MMG",
                createWaveChart("Wave", 1, new Unit("Acceleration", "m/s/s"), 5),
                createSpectrumChart("Spectrum", 0.01, 100, new Axis(Unit.db("Amplitude").toString(), -200, 0, 25)),
                createTimeSeriesChart("MDF and PF", 60.0, new LogAxis("Frequency[Hz]", 1, 100.0)),
                createWaveChart("RMS", 10, Unit.v(), 0, 5)
        );
        filters.addAll(Arrays.asList(
                x -> (x - 2.5) / 1.0,
                IirFilter.load("bpf0.001-0.2.txt"),
                IirFilter.load("bef0.048-0.052.txt")
        ));
        spectrumPlot.getLine().setThick(2.0);
    }

    /**
     * サブクラス向けのコンストラクです。
     *
     * @param masterPane    グラフの親ペイン
     * @param name          Analyzerの名前
     * @param waveChart     波形のChart
     * @param spectrumChart スペクトラムのChart
     * @param freqChart     周波数解析のChart
     * @param rmsChart      RMSのChart
     */
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
        source.addAllCallback(filters);
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

        mdfSeries = new RealSeries<>(fs / 2, 0.0, Unit.sec(), Unit.hz());
        pfSeries = new RealSeries<>(fs / 2, 0.0, Unit.sec(), Unit.hz());
        rmsSeries = new RealSeries<>(5.0, 0.0, Unit.sec(), Unit.v());

        number = n;
        fft = new OouraFft(n);
        logger = new CsvLogger(title + ".csv");
        waveTask.start();
        freqTask.start();
    }

    @Override
    public void stop() {
        super.stop();
        waveTask.stop();
        freqTask.stop();
        mdfSeries.clear();
        pfSeries.clear();
        Arrays.asList(wavePlot, spectrumPlot, mdfPlot, pfPlot, rmsPlot).forEach(plot -> plot.getLine().getVectorList().clear());
        logger.close();
    }

    /**
     * 波形表示のメソッド
     */
    private void waveAnalyze() {
        graphContextMap.get("Wave").ifVisible(context -> {
            Wave wave = source.getWave(1.0);
            context.setToVectorListRealTime(wave.stream(), wavePlot.getLine().getVectorList());
        });
    }

    /**
     * 周波数解析のメソッド
     */
    private void freqAnalyze() {
        if (source.available(number)) {
            Wave tmpWave = source.getWave(number);
            Wave wave = tmpWave.stream().to(tmpWave::from);
            tmpWave = wave.stream().fill(fft.getLength(), 0.0, 1 / wave.getSamplingFrequency()).replaceYByIndex(Wave::hamming, (a, b) -> a * b).to(wave::from);
            Signal<Double, Point> spectrum = fft.dft(tmpWave).getPowerSpectrum();
            graphContextMap.get("Spectrum").ifVisible(context ->
                    context.setToVectorList(spectrum.stream().replaceY(y ->
                            PointLogY.dB(y, spectrum.getYMax())), spectrumPlot.getLine().getVectorList()));

            Point medianPoint = new Point(wave.getStartTime(), spectrum.stream().to(SeriesStream::getMedian).getX());
            logger.print(medianPoint);
            mdfCallback.accept(medianPoint.getY());
            mdfSeries.add(medianPoint);
            pfSeries.add(new Point(wave.getStartTime(), spectrum.stream().to(SeriesStream::getPeek).getX()));

            mdfGide.setPosition(medianPoint.getY());

            graphContextMap.get("Frequency").ifVisible(context -> {
                context.setToVectorListRealTime(mdfSeries.stream(), mdfPlot.getLine().getVectorList());
                context.setToVectorListRealTime(pfSeries.stream(), pfPlot.getLine().getVectorList());
            });

            Wave rmsWave = source.getWave(3.0);
            double sqAve = rmsWave.stream().replaceY(y -> y * y).reduce((a, b) -> a + b) / rmsWave.size();
            double rms = Math.sqrt(sqAve);
            rmsSeries.add(new Point(rmsWave.getStartTime(), rms));
            graphContextMap.get("RMS").ifVisible(context ->
                    context.setToVectorListRealTime(rmsSeries.stream(), rmsPlot.getLine().getVectorList())
            );
        }
    }

    /**
     * 中央周波数が求まった際に実行されるコールバックを設定します。
     *
     * @param mdfCallback 解析した中央周波数を受け取るコールバック
     */
    public void setMdfCallback(DoubleConsumer mdfCallback) {
        this.mdfCallback = mdfCallback;
    }
}
