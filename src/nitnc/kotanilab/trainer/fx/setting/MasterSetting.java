package nitnc.kotanilab.trainer.fx.setting;

/**
 * MasterControllerの設定クラスです。
 * JavaBeanに対応していて、Saverクラスによる保存・復帰ができます。
 */
public class MasterSetting {
    private static final long serialVersionUID = 1L;

    /**
     * サンプリング周波数
     */
    private double samplingFrequency;

    public MasterSetting() {
    }

    public double getSamplingFrequency() {
        return samplingFrequency;
    }

    public void setSamplingFrequency(double samplingFrequency) {
        this.samplingFrequency = samplingFrequency;
    }
}
