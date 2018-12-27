package nitnc.kotanilab.trainer.fx;


import java.io.Serializable;

public class HrSetting implements Serializable {
    private static final long serialVersionUID = 1L;

    private double thLow;
    private double thHigh;
    private boolean wave;
    private boolean hr;
    private boolean debug;

    public HrSetting() {
    }

    public double getThLow() {
        return thLow;
    }

    public void setThLow(double thLow) {
        this.thLow = thLow;
    }

    public double getThHigh() {
        return thHigh;
    }

    public void setThHigh(double thHigh) {
        this.thHigh = thHigh;
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

    public boolean getDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
