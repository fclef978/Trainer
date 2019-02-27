package nitnc.kotanilab.trainer.gl.util;

import nitnc.kotanilab.trainer.gl.style.Style;

/**
 * オブジェクトのOpenGLでの絶対描画範囲を表すクラスです。
 * ParentがPositionを管理し、ChildはPositionを元に描画します。
 * TODO いちいちコンストラクタを動かすのは非効率的　もっと言えば再利用可にしたい
 */
public class Position {

    protected double xScale;
    protected double xOffset;
    protected double yScale;
    protected double yOffset;

    /**
     * 標準値で作成します。
     */
    public Position() {
        xScale = 1.0;
        yScale = 1.0;
        xOffset = 0.0;
        yOffset = 0.0;
    }

    /**
     * 親オブジェクトのPositionとこのPositionが属するオブジェクトのStyleから作成します。
     *
     * @param parent 親オブジェクトのPosition
     * @param style  このPositionが属するオブジェクトのStyle
     */
    public Position(Position parent, Style style) {
        this.xScale = style.get("width").getValueAsRatio() * parent.getXScale();
        this.xOffset = style.get("margin-x").getValueAsRatio() * parent.getXScale() + parent.getXOffset();
        this.yScale = style.get("height").getValueAsRatio() * parent.getYScale();
        this.yOffset = style.get("margin-y").getValueAsRatio() * parent.getYScale() + parent.getYOffset();
    }

    /**
     * このPositionが属するオブジェクトのStyleから作成します。
     * 細かい設定は後から行います。
     *
     * @param style このPositionが属するオブジェクトのStyle
     */
    public Position(Style style) {
        this.xScale = style.get("width").getValueAsRatio();
        this.xOffset = style.get("margin-x").getValueAsRatio();
        this.yScale = style.get("height").getValueAsRatio();
        this.yOffset = style.get("margin-y").getValueAsRatio();
    }

    /**
     * 指定したPositionを親としてこのPositionを更新します。
     *
     * @param parent 親にしたいPosition
     */
    public void scale(Position parent) {
        xScale *= parent.getXScale();
        xOffset = xOffset * parent.getXScale() + parent.getXOffset();
        yScale *= parent.getYScale();
        yOffset = yOffset * parent.getYScale() + parent.getYOffset();
    }

    public double getXScale() {
        return xScale;
    }

    public double getXOffset() {
        return xOffset;
    }

    public double getYScale() {
        return yScale;
    }

    public double getYOffset() {
        return yOffset;
    }

    public void setXScale(double xScale) {
        this.xScale = xScale;
    }

    public void setXOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public void setYScale(double yScale) {
        this.yScale = yScale;
    }

    public void setYOffset(double yOffset) {
        this.yOffset = yOffset;
    }

    @Override
    public String toString() {
        return "Position{" +
                "xScale=" + xScale +
                ", xOffset=" + xOffset +
                ", yScale=" + yScale +
                ", yOffset=" + yOffset +
                '}';
    }
}
