package nitnc.kotanilab.trainer.gl.chart.plot;

import nitnc.kotanilab.trainer.gl.chart.Axis;
import nitnc.kotanilab.trainer.gl.node.Node;
import nitnc.kotanilab.trainer.gl.shape.Line;
import nitnc.kotanilab.trainer.gl.shape.Text;
import nitnc.kotanilab.trainer.gl.util.Vector;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GideLine extends Plot{
    protected Map<String, Line> gideLineContextMap = new HashMap<>();

    protected Axis xAxis;
    protected Axis yAxis;
    Line gideLine;
    double width;
    Text label;
    Color color;
    boolean vertical;
    java.util.List<Node> nodes;

    public GideLine(String label, double position, Color color, double width, boolean vertical) {
        this.gideLine = new Line(0.0, vertical, color, width);
        this.label = new Text(new Font("", Font.ITALIC, 10), Color.BLACK, label, new Vector(0.0, 0.0), vertical);
        this.color = color;
        this.width = width;
        this.vertical = vertical;
        this.nodes = Arrays.asList(this.gideLine, this.label);
        ;
        if (vertical) {
            this.label.getStyle().put("align:right bottom;");
        } else {
            this.label.getStyle().put("align:left bottom;");
        }
        setPosition(position);
    }

    public void setPosition(double position) {
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
    public Set<String> getKeys() {
        return gideLineContextMap.keySet();
    }

    public List<Node> getNodes() {
        return nodes;
    }
}
