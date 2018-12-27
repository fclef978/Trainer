package nitnc.kotanilab.trainer.adConverter;

/**
 * サンプリング時の設定を表すクラスです。
 * 使用する機器に合わせて継承してください。
 */
public class SamplingSetting {

    /**
     * サンプリングするチャンネル数です。
     */
    protected int chCount;
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

    public SamplingSetting(int chCount, int samplingNumber, double samplingFrequency) {
        this.chCount = chCount;
        this.samplingNumber = samplingNumber;
        this.samplingTime = numberToTime(samplingNumber);
        this.samplingFrequency = samplingFrequency;
    }

    public SamplingSetting(int chCount, double samplingTime, double samplingFrequency) {
        this.chCount = chCount;
        this.samplingNumber = timeToNumber(samplingTime);
        this.samplingTime = samplingTime;
        this.samplingFrequency = samplingFrequency;
    }

    protected double numberToTime(int num) {
        return (double)num / samplingFrequency * 1000.0;
    }

    protected int timeToNumber(double time) {
        return (int)(time * samplingFrequency / 1000.0);
    }

    public int getChCount() {
        return chCount;
    }

    public int getSamplingNumber() {
        return samplingNumber;
    }

    public double getSamplingFrequency() {
        return samplingFrequency;
    }

    public void setAll(int chCount, int samplingNumber, double samplingFrequency) {
        setChCount(chCount);
        setSamplingNumber(samplingNumber);
        setSamplingFrequency(samplingFrequency);
    }

    public void setAll(int chCount, double mSec, double samplingFrequency) {
        setAll(chCount, timeToNumber(mSec), samplingFrequency);
    }

    public void setChCount(int chCount) {
        this.chCount = chCount;
    }

    public void setSamplingNumber(int samplingNumber) {
        this.samplingNumber = samplingNumber;
    }

    public void setSamplingFrequency(double samplingFrequency) {
        this.samplingFrequency = samplingFrequency;
    }

}
