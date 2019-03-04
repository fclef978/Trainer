package nitnc.kotanilab.trainer.math.analysis;

import nitnc.kotanilab.trainer.gl.chart.Axis;
import nitnc.kotanilab.trainer.gl.chart.LogAxis;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.math.Unit;

import java.util.Arrays;

/**
 * 筋電図の解析を行うAnalyzerです。
 */
public class EmgAnalyzer extends MmgAnalyzer {

    /**
     * コンストラクタです。
     *
     * @param masterPane グラフの親ペイン
     */
    public EmgAnalyzer(Pane masterPane) {
        super(masterPane, "EMG",
                createWaveChart("Wave",1, Unit.v(), 3),
                createSpectrumChart("Spectrum",0.01, 100, new Axis(Unit.db("Amplitude").toString(), -100, 0, 10)),
                createTimeSeriesChart("Frequency",60.0, new LogAxis("Frequency[Hz]", 1, 100.0)),
                createWaveChart("RMS",10, Unit.v(), 0, 5)
        );
        waveYMax = 3.0;
        waveYMin = -waveYMax;
        filters.addAll(Arrays.asList(
                IirFilter.load("bpf0.001-0.2.txt"),
                IirFilter.load("bef0.048-0.052.txt")
        ));
    }

}
