package nitnc.kotanilab.trainer.gl.chart.plot;

import nitnc.kotanilab.trainer.gl.chart.Axis;
import nitnc.kotanilab.trainer.gl.node.Node;
import nitnc.kotanilab.trainer.gl.shape.Line;
import nitnc.kotanilab.trainer.gl.shape.Text;
import nitnc.kotanilab.trainer.gl.util.Vector;
import nitnc.kotanilab.trainer.util.Dbg;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GideLine extends Plot {
    private Line gideLine;
    private Text label;
    private boolean vertical;
    private double position;

    public GideLine(String name, double position, Color color, double width, boolean vertical) {
        super(name);
        this.position = position;
        this.gideLine = new Line(0.0, vertical, color, width);
        this.label = new Text(new Font("", Font.ITALIC, 10), Color.BLACK, name, new Vector(0.0, 0.0), vertical);
        this.vertical = vertical;
        if (vertical) {
            this.label.getStyle().put("align:right bottom;");
        } else {
            this.label.getStyle().put("align:left bottom;");
        }
        children.addAll(this.gideLine, this.label);
    }

    public void setPosition(double position) {
        this.position = position;
        Vector vector;
        double absPosition;
        if (vertical) {
            absPosition = xAxis.scale(position);
            vector = new Vector(absPosition, 0.95);
        } else {
            absPosition = yAxis.scale(position);
            vector = new Vector(-0.95, absPosition);
        }
        gideLine.setVector(absPosition, vertical);
        label.setVector(vector);
    }

    @Override
    public void setAxises(Axis xAxis, Axis yAxis) {
        super.setAxises(xAxis, yAxis);
        Dbg.p(xAxis, this.xAxis);
        setPosition(position);
    }
}
