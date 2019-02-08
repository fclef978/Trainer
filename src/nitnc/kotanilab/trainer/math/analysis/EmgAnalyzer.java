package nitnc.kotanilab.trainer.math.analysis;

import nitnc.kotanilab.trainer.gl.chart.Axis;
import nitnc.kotanilab.trainer.gl.chart.LogAxis;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.math.Unit;

import java.util.Arrays;

public class EmgAnalyzer extends MmgAnalyzer {

    public EmgAnalyzer(Pane masterPane) {
        super(masterPane, "EMG",
                createWaveGraph(1, Unit.v(), 3, "Wave"),
                createSpectrumGraph(0.01, 100, new Axis(Unit.db("Amplitude").toString(), -100, 0, 10), "Spectrum"),
                createTimeSeriesGraph(60.0, new LogAxis("Frequency[Hz]", 1, 100.0), "Median", "Peek"),
                createWaveGraph(10, Unit.v(), 0, 5, "RMS")
        );
        waveYMax = 3.0;
        waveYMin = -waveYMax;
        filters.addAll(Arrays.asList(
                IirFilter.execute("bpf0.001-0.2.txt"),
                IirFilter.execute("bef0.048-0.052.txt")
        ));
    }

}
