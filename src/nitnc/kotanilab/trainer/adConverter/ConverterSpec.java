package nitnc.kotanilab.trainer.adConverter;

public abstract class ConverterSpec {

    /**
     * ボードの名前
     */
    protected String name;
    /**
     * ポート数
     */
    protected int chCount;
    /**
     * 分解能(ビット数)
     */
    protected int resolution;
    /**
     * 絶対値
     */
    protected double range;

    public ConverterSpec(String name, int chCount, int resolution, double range) {
        this.name = name;
        this.chCount = chCount;
        this.resolution = resolution;
        this.range = range;
    }

    public String getName() {
        return name;
    }

    public int getChCount() {
        return chCount;
    }

    public int getResolution() {
        return resolution;
    }

    public double getRange() {
        return range;
    }

}
