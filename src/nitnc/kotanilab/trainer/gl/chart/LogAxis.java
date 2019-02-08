package nitnc.kotanilab.trainer.gl.chart;

import nitnc.kotanilab.trainer.gl.shape.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * グラフの対数軸です。
 * グリッドの間隔と補助目盛りの有無は固定です。
 */
public class LogAxis extends Axis {


    /**
     * コンストラクタです。
     *
     * @param name     軸の名前
     * @param min      最小値
     * @param max      最大値
     * @param vertical 垂直軸かどうか
     */
    public LogAxis(String name, double min, double max, boolean vertical) {
        super(name, Math.log10(min), Math.log10(max), 0.0, vertical);
    }


    /**
     * 水平軸で作成します。
     *
     * @param name     軸の名前
     * @param min      最小値
     * @param max      最大値
     */
    public LogAxis(String name, double min, double max) {
        super(name, Math.log10(min), Math.log10(max), 0.0);
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
    public List<Text> getTickMarks() {
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
            strings.add(createText(str, getTickMarkVector(pos)));
        }
        return strings;
    }

    @Override
    public List<Line> getGraduationLines() {
        List<Line> grids = new ArrayList<>();
        boolean first = true;
        for (double i = min; i < max; i++) {
            for (int j = 1; j < 10; j++) {
                if (first) {
                    first = false;
                    continue;
                }
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
