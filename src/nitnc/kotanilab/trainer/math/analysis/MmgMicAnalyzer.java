package nitnc.kotanilab.trainer.math.analysis;

import nitnc.kotanilab.trainer.gl.chart.Axis;
import nitnc.kotanilab.trainer.gl.chart.LogAxis;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.math.Unit;

import java.util.Arrays;

public class MmgMicAnalyzer extends MmgAnalyzer {

    public MmgMicAnalyzer(Pane masterPane) {
        super(masterPane, "MMG(mic.)",
                createWaveGraph(0.1, Unit.v(), 2, "Wave"),
                createSpectrumGraph(0.01, 100, new Axis(Unit.db("Amplitude").toString(), -100, 0, 10), "Spectrum"),
                createTimeSeriesGraph(60.0, new LogAxis("Frequency[Hz]", 1, 100.0), "Median", "Peek"),
                createWaveGraph(10, Unit.v(), 0, 5, "RMS")
        );
        waveYMax = 2;
        waveYMin = -waveYMax;
        waveXMax = 0.1;
        filters.addAll(Arrays.asList(/*
                IirFilter.execute("bpf0.001-0.2.txt"),
                IirFilter.execute("bef0.048-0.052.txt")*/
                IirFilter.execute("lpf0.1.txt")
        ));
    }

}
