package nitnc.kotanilab.trainer.adConverter;

import java.util.List;

/**
 * サンプリング時の設定を表すスーパークラスです。
 * 使用する機器に合わせて継承してください。
 */
public class SamplingSetting {

    /**
     * サンプリングするチャンネルのリストです。
     */
    protected List<Integer> channelList;
    /**
     * サンプリング数です。
     */
    protected int samplingNumber;
    /**
     * サンプリング時間です。
     */
    protected double samplingTime;
    /**
     * サンプリング周波数です。
     */
    protected double samplingFrequency;

    /**
     * 引数なしコンストラクタです。
     */
    public SamplingSetting() {
    }

    /**
     * コンストラクタです。
     * サンプル数で設定します。
     *
     * @param channelList       使用するチャンネルのリスト
     * @param samplingNumber    サンプル数
     * @param samplingFrequency サンプリング周波数
     */
    public SamplingSetting(List<Integer> channelList, int samplingNumber, double samplingFrequency) {
        this.channelList = channelList;
        this.samplingNumber = samplingNumber;
        this.samplingTime = numberToTime(samplingNumber);
        this.samplingFrequency = samplingFrequency;
    }

    /**
     * コンストラクタです。
     * サンプル時間で設定します。
     *
     * @param channelList       使用するチャンネルのリスト
     * @param samplingTime      サンプル時間[ms]
     * @param samplingFrequency サンプリング周波数
     */
    public SamplingSetting(List<Integer> channelList, double samplingTime, double samplingFrequency) {
        this.channelList = channelList;
        this.samplingNumber = timeToNumber(samplingTime);
        this.samplingTime = samplingTime;
        this.samplingFrequency = samplingFrequency;
    }

    /**
     * サンプル数からサンプル時間に変換します。
     *
     * @param num サンプル数
     * @return サンプル時間[ms]
     */
    protected double numberToTime(int num) {
        return (double) num / samplingFrequency * 1000.0;
    }

    /**
     * サンプル時間からサンプル数に変換します。
     *
     * @param time サンプル時間[ms]
     * @return サンプル数
     */
    protected int timeToNumber(double time) {
        return (int) (time * samplingFrequency / 1000.0);
    }

    public List<Integer> getChannelList() {
        return channelList;
    }

    public int getSamplingNumber() {
        return samplingNumber;
    }

    public double getSamplingFrequency() {
        return samplingFrequency;
    }

    /**
     * まとめて設定するユーティリティメソッドです。
     *
     * @param channelList       チャンネルのリスト
     * @param samplingNumber    サンプル数
     * @param samplingFrequency サンプリング周波数
     */
    public void setAll(List<Integer> channelList, int samplingNumber, double samplingFrequency) {
        setChannelList(channelList);
        setSamplingNumber(samplingNumber);
        setSamplingFrequency(samplingFrequency);
    }

    /**
     * まとめて設定するユーティリティメソッドです。
     *
     * @param channelList       チャンネルのリスト
     * @param samplingTime      サンプル秒[ms]
     * @param samplingFrequency サンプリング周波数
     */
    public void setAll(List<Integer> channelList, double samplingTime, double samplingFrequency) {
        setAll(channelList, timeToNumber(samplingTime), samplingFrequency);
    }

    public void setChannelList(List<Integer> channelList) {
        this.channelList = channelList;
    }

    public void setSamplingNumber(int samplingNumber) {
        this.samplingNumber = samplingNumber;
    }

    public void setSamplingFrequency(double samplingFrequency) {
        this.samplingFrequency = samplingFrequency;
    }

}
