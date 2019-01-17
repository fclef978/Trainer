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

public class MmgMicAnalyzer extends MmgAnalyzer {

    public MmgMicAnalyzer(Pane masterPane) {
        super(masterPane, "MMG(mic.)",
                createWaveGraph(1, Unit.v(), 1, "Wave"),
                createSpectrumGraph(0.01, 100, new Axis(Unit.db("Amplitude").toString(), -100, 0, 10), "Spectrum"),
                createTimeSeriesGraph(60.0, new LogAxis("Frequency[Hz]", 1, 100.0, 0.1), "Median", "Peek"),
                createWaveGraph(10, Unit.v(), 0, 5, "RMS")
        );
        waveYMax = 0.5;
        waveYMin = -waveYMax;
        filters.addAll(Arrays.asList(
                IirFilter.execute("bpf0.001-0.2.txt"),
                IirFilter.execute("bef0.048-0.052.txt")
        ));
    }

}
