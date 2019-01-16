package nitnc.kotanilab.trainer.fx.setting;

public class MasterSetting {
    private static final long serialVersionUID = 1L;

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
