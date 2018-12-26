package jp.ac.numazu_ct.d14122.gl.util;

import jp.ac.numazu_ct.d14122.gl.style.Style;

/**
 * TODO いちいちコンストラクタを動かすのは非効率的　もっと言えば再利用可にしたい
 */
public class Position {

    protected double xScale;
    protected double xOffset;
    protected double yScale;
    protected double yOffset;

    public Position() {
        xScale = 1.0;
        yScale = 1.0;
        xOffset = 0.0;
        yOffset = 0.0;
    }

    public Position(Position parent, Style style) {
        this.xScale = style.get("width").getNumber() * parent.getXScale();
        this.xOffset = style.get("margin-x").getNumber() * parent.getXScale() + parent.getXOffset();
        this.yScale = style.get("height").getNumber() * parent.getYScale();
        this.yOffset = style.get("margin-y").getNumber() * parent.getYScale() + parent.getYOffset();
    }

    public Position(Style style) {
        this.xScale = style.get("width").getNumber();
        this.xOffset = style.get("margin-x").getNumber();
        this.yScale = style.get("height").getNumber();
        this.yOffset = style.get("margin-y").getNumber();
    }

    public Position(Position parent, Position style) {
        this.xScale = style.getXScale() * parent.getXScale();
        this.xOffset = style.getXOffset() * parent.getXScale() + parent.getXOffset();
        this.yScale = style.getYScale() * parent.getYScale();
        this.yOffset = style.getYOffset() * parent.getYScale() + parent.getYOffset();
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
