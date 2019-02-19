package nitnc.kotanilab.trainer.gl.chart.plot;

import nitnc.kotanilab.trainer.gl.chart.Axis;
import nitnc.kotanilab.trainer.gl.pane.StackPane;
import nitnc.kotanilab.trainer.gl.shape.Line;
import nitnc.kotanilab.trainer.gl.shape.Text;
import nitnc.kotanilab.trainer.gl.util.Vector;

import java.util.Set;

public abstract class Plot extends StackPane {
    protected Axis xAxis;
    protected Axis yAxis;

    public void setxAxises(Axis xAxis, Axis yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public abstract Set<String> getKeys();

    public Line getLegendLine(String key, Vector start, Vector end) {
        return null;
    }
}
