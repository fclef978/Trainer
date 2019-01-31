package nitnc.kotanilab.trainer.gl.util;

import nitnc.kotanilab.trainer.gl.style.Style;

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
        this.xScale = style.get("width").getValueAsRltNumber() * parent.getXScale();
        this.xOffset = style.get("margin-x").getValueAsRltNumber() * parent.getXScale() + parent.getXOffset();
        this.yScale = style.get("height").getValueAsRltNumber() * parent.getYScale();
        this.yOffset = style.get("margin-y").getValueAsRltNumber() * parent.getYScale() + parent.getYOffset();
    }

    public Position(Style style) {
        this.xScale = style.get("width").getValueAsRltNumber();
        this.xOffset = style.get("margin-x").getValueAsRltNumber();
        this.yScale = style.get("height").getValueAsRltNumber();
        this.yOffset = style.get("margin-y").getValueAsRltNumber();
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
