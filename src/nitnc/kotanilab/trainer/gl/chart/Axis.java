package nitnc.kotanilab.trainer.gl.chart;


import nitnc.kotanilab.trainer.gl.shape.*;
import nitnc.kotanilab.trainer.gl.util.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * グラフの線形軸です。
 * 一般的なグラフに使用できます。
 * 将来的には極座標軸なども追加していく予定です。
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

    private boolean reverce = false;

    /**
     * コンストラクタです。
     *
     * @param name     軸の名前
     * @param min      最小値
     * @param max      最大値
     * @param size     補助線の間隔
     * @param vertical 垂直軸かどうか
     */
    public Axis(String name, double min, double max, double size, boolean vertical) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.size = size;
        this.vertical = vertical;
    }

    /**
     * 水平軸で作成します。
     *
     * @param name 軸の名前
     * @param min  最小値
     * @param max  最大値
     * @param size 補助線の間隔
     */
    public Axis(String name, double min, double max, double size) {
        this(name, min, max, size, false);
    }

    /**
     * 軸の方向を反転させるかどうかを設定します。
     *
     * @param reverse 反転ならtrue
     */
    public void setReverse(boolean reverse) {
        this.reverce = reverse;
    }

    /**
     * 軸を垂直にするか水平にするかを設定します。
     *
     * @param vertical 垂直ならtrue
     */
    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    /**
     * 軸の絶対中間値を返します。
     * 絶対中間値は軸の最小値と最大値の中間値です。
     *
     * @return 軸の絶対中間値
     */
    protected double getAbsIntermediate() {
        return (max + min) / 2.0;
    }

    /**
     * 軸の相対中間値を返します。
     * 相対中間値は軸の最小値を0にずらしたときの中間値です。
     *
     * @return 軸の相対中間値
     */
    protected double getRltIntermediate() {
        return (max - min) / 2.0;
    }

    /**
     * 値を軸の値でOpenGLのベクトルに変換します。
     *
     * @param val 値
     * @return ベクトル
     */
    public double scale(double val) {
        return (val - getAbsIntermediate()) / getRltIntermediate();
    }

    public double getMin() {
        return min;
    }

    /**
     * 値の範囲を返します。
     *
     * @return 値の範囲
     */
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

    /**
     * 目盛りの文字のリストを返します。
     *
     * @return 目盛りの文字のリスト
     */
    public List<Text> getTickMarks() {
        List<Text> strings = new ArrayList<>();
        for (double i = min; i <= max * 1.001; i += size) {
            double pos = scale(i);
            String tmp = String.format("%.5f", i);
            String[] split = tmp.split("\\.");
            while (split[1].endsWith("0")) {
                split[1] = split[1].substring(0, split[1].length() - 1);
            }
            String str = split[1].isEmpty() ? split[0] : split[0] + "." + split[1];
            strings.add(createText(str, getTickMarkVector(pos).inverseX(reverce)));
        }
        return strings;
    }

    /**
     * 目盛り線のリストを返します。
     *
     * @return 目盛り線のリスト
     */
    public List<Line> getGraduationLines() {
        List<Line> grids = new ArrayList<>();
        for (double i = min + size; i < max; i += size) {
            double pos = scale(i);
            Color color;
            double range = max - min;
            if (i < range / 100 && i > -range / 100) color = Color.GRAY.darker();
            else color = Color.GRAY.brighter();
            grids.add(new Line(pos, !vertical, color, 1.0));
        }
        return grids;
    }

    /**
     * 目盛り文字のVectorを返します。
     *
     * @param pos OpenGLのベクトル
     * @return 目盛り文字のVector
     */
    protected Vector getTickMarkVector(double pos) {
        Vector vector;
        if (vertical) {
            vector = new Vector(0.4, pos);
        } else {
            vector = new Vector(pos, 0.4);
        }
        return vector;
    }

    /**
     * 文字列と位置からそれに対応したOpenGL文字列オブジェクトを細かい設定なしで作成するユーティリティメソッドです。
     * @param str 文字列
     * @param vector 位置
     * @return OpenGL文字列オブジェクト
     */
    protected Text createText(String str, Vector vector) {
        return new Text(new Font("", Font.PLAIN, 10), str, vector, vertical);
    }
}
