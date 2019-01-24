package nitnc.kotanilab.trainer.gl.chart;

import nitnc.kotanilab.trainer.gl.shape.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hirokazu SUZUKI on 2018/08/01.
 * 対数軸
 */
public class LogAxis extends Axis {

    public LogAxis(String name, double min, double max, double size, boolean vertical) {
        super(name, Math.log10(min), Math.log10(max), size, vertical);
    }

    public LogAxis(String name, int min, int max, double size, boolean vertical) {
        super(name, min, max, size, vertical);
    }

    public LogAxis(String name, double min, double max, double size) {
        super(name, Math.log10(min), Math.log10(max), size);
    }

    public LogAxis(String name, int min, int max, double size) {
        super(name, min, max, size);
    }

    @Override
    public void setMin(double min) {
        super.setMin(Math.log10(min));
    }

    @Override
    public void setMax(double max) {
        super.setMax(Math.log10(max));
    }

    @Override
    public double scale(double val) {
        return super.scale(Math.log10(val));
    }

    @Override
    public List<Text> getGridStrings() {
        List<Text> strings = new ArrayList<>();
        for (double i = min; i <= max; i++) {
            double num = Math.pow(10, i);
            double pos = scale(num);
            String str;
            if (num < 1) {
                str = String.format("%.1f", num);
            } else {
                str = String.format("%d", Math.round(num));
            }
            strings.add(createText(str, getVector(pos)));
        }
        return strings;
    }

    @Override
    public List<Line> getGrids() {
        List<Line> grids = new ArrayList<>();
        for (double i = min; i < max; i++) {
            for (int j = 1; j < 10; j++) {
                double num = j * Math.pow(10, i);
                if (num > Math.pow(10, max) || num <= min) break;
                double pos = scale(num);
                if (j == 1) grids.add(new Line(pos, !vertical, Color.GRAY.darker(), 1.0));
                else grids.add(new Line(pos, !vertical, Color.GRAY.brighter(), 1.0));
            }
        }
        return grids;
    }
}
