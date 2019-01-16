package nitnc.kotanilab.trainer.adConverter;

import java.util.List;

/**
 * サンプリング時の設定を表すクラスです。
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

    public SamplingSetting() {
    }

    public SamplingSetting(List<Integer> channelList, int samplingNumber, double samplingFrequency) {
        this.channelList = channelList;
        this.samplingNumber = samplingNumber;
        this.samplingTime = numberToTime(samplingNumber);
        this.samplingFrequency = samplingFrequency;
    }

    public SamplingSetting(List<Integer> channelList, double samplingTime, double samplingFrequency) {
        this.channelList = channelList;
        this.samplingNumber = timeToNumber(samplingTime);
        this.samplingTime = samplingTime;
        this.samplingFrequency = samplingFrequency;
    }

    protected double numberToTime(int num) {
        return (double) num / samplingFrequency * 1000.0;
    }

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

    public void setAll(List<Integer> channelList, int samplingNumber, double samplingFrequency) {
        setChannelList(channelList);
        setSamplingNumber(samplingNumber);
        setSamplingFrequency(samplingFrequency);
    }

    public void setAll(List<Integer> channelList, double mSec, double samplingFrequency) {
        setAll(channelList, timeToNumber(mSec), samplingFrequency);
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
