package nitnc.kotanilab.trainer.fx.setting;

/**
 * MmgControllerとそのサブクラスの設定クラスです。
 * JavaBeanに対応していて、Saverクラスによる保存・復帰ができます。
 */
public class MgSetting {
    private static final long serialVersionUID = 1L;

    /**
     * 使用するチャンネル
     */
    private int channel;
    /**
     * サンプル数
     */
    private int samplingNumber;
    /**
     * waveグラフを表示するかどうか
     */
    private boolean wave;
    /**
     * spectrumグラフを表示するかどうか
     */
    private boolean spectrum;
    /**
     * frequencyグラフを表示するかどうか
     */
    private boolean frequency;
    /**
     * rmsグラフを表示するかどうか
     */
    private boolean rms;

    public MgSetting() {
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getSamplingNumber() {
        return samplingNumber;
    }

    public void setSamplingNumber(int samplingNumber) {
        this.samplingNumber = samplingNumber;
    }

    public boolean getWave() {
        return wave;
    }

    public void setWave(boolean wave) {
        this.wave = wave;
    }

    public boolean getSpectrum() {
        return spectrum;
    }

    public void setSpectrum(boolean spectrum) {
        this.spectrum = spectrum;
    }

    public boolean getFrequency() {
        return frequency;
    }

    public void setFrequency(boolean frequency) {
        this.frequency = frequency;
    }

    public boolean getRms() {
        return rms;
    }

    public void setRms(boolean rms) {
        this.rms = rms;
    }
}
