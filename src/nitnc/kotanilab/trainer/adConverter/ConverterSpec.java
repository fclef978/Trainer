package nitnc.kotanilab.trainer.adConverter;

/**
 * ADコンバータのスペックを表すスーパークラスです。
 */
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

    /**
     * コンストラクタです。
     *
     * @param name コンバータの品名
     * @param chCount 最大チャンネル数
     * @param resolution 分解能(ビット数)
     * @param range 測定範囲
     */
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
