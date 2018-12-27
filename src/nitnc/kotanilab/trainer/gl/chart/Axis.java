package nitnc.kotanilab.trainer.gl.chart;


import nitnc.kotanilab.trainer.gl.shape.*;
import nitnc.kotanilab.trainer.gl.util.Vector;
import nitnc.kotanilab.trainer.util.Dbg;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * グラフの軸です。
 */
public class Axis {

    /**
     * 軸名
     */
    protected String name;
    /**
     * 最小値
     */
    protected double min;
    /**
     * 最大値
     */
    protected double max;
    /**
     * ステップ値
     */
    protected double size;
    /**
     * 縦か横か
     */
    protected boolean vertical;

    private boolean isReverce = false;

    public Axis(String name, double min, double max, double size, boolean vertical) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.size = size;
        this.vertical = vertical;
    }

    public Axis(String name, double min, double max, double size) {
        this(name, min, max, size, false);
    }

    public void setReverse(boolean isReverse) {
        this.isReverce = isReverse;
    }

    public void setVertical() {
        vertical = true;
    }

    protected double getIntermediate() {
        return (max + min) / 2.0;
    }

    protected double getHalf() {
        return (max - min) / 2.0;
    }

    public double scale(double val) {
        return (val - getIntermediate()) / getHalf();
    }

    protected double restrict(double val) {
        if (val < min) val = min;
        if (val > max) val = max;
        return val;
    }

    public double getMin() {
        return min;
    }

    public double getRange() {
        return max - min;
    }

    public double getMax() {
        return max;
    }

    public double getSize() {
        return size;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public List<Text> getGridStrings() {
        List<Text> strings = new ArrayList<>();
        for (double i = min; i <= max * 1.001; i += size) {
            double pos = scale(i);
            String tmp = String.format("%.5f", i);
            String[] split = tmp.split("\\.");
            while (split[1].endsWith("0")) {
                split[1] = split[1].substring(0, split[1].length() - 1);
            }
            String str = split[1].isEmpty() ? split[0] : split[0] + "." + split[1];
            strings.add(createText(str, getVector(pos).reverceX(isReverce)));
        }
        return strings;
    }

    public List<Line> getGrids() {
        List<Line> grids = new ArrayList<>();
        for (double i = min + size; i < max; i += size) {
            double pos = scale(i);
            grids.add(new Line(pos, !vertical, Color.GRAY, 1.0));
        }
        return grids;
    }

    protected Vector getVector(double pos) {
        Vector vector;
        if (vertical) {
            vector = new Vector(0.4, pos);
        } else {
            vector = new Vector(pos, 0.4);
        }
        return vector;
    }

    protected Text createText(String str, Vector vector) {
        return new Text(new Font("", Font.BOLD, 10), Color.WHITE, str, vector, vertical);
    }
}
