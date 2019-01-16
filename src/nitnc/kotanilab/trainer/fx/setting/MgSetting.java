package nitnc.kotanilab.trainer.fx.setting;

public class MgSetting {
    private static final long serialVersionUID = 1L;

    private int channel;
    private int samplingNumber;
    private boolean wave;
    private boolean spectrum;
    private boolean frequency;
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
