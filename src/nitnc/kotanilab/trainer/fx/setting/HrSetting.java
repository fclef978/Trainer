package nitnc.kotanilab.trainer.fx.setting;


import java.io.Serializable;

/**
 * HrControllerの設定クラスです。
 * JavaBeanに対応していて、Saverクラスによる保存・復帰ができます。
 */
public class HrSetting implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 使用するチャンネル
     */
    private int channel;
    /**
     * waveグラフを表示するかどうか
     */
    private boolean wave;
    /**
     * hrグラフを表示するかどうか
     */
    private boolean hr;
    /**
     * acfグラフを表示するかどうか
     */
    private boolean acf;
    /**
     * diffグラフを表示するかどうか
     */
    private boolean diff;

    public HrSetting() {
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public boolean getWave() {
        return wave;
    }

    public void setWave(boolean wave) {
        this.wave = wave;
    }

    public boolean getHr() {
        return hr;
    }

    public void setHr(boolean hr) {
        this.hr = hr;
    }

    public boolean getAcf() {
        return acf;
    }

    public void setAcf(boolean acf) {
        this.acf = acf;
    }

    public boolean getDiff() {
        return diff;
    }

    public void setDiff(boolean diff) {
        this.diff = diff;
    }
}
