package nitnc.kotanilab.trainer.gpg3100.wrapper;

import nitnc.kotanilab.trainer.adConverter.SamplingSetting;
import nitnc.kotanilab.trainer.gpg3100.jnaNative.AdSamplingRequest;

/**
 * ADSMPLREQ構造体のラッパです。
 * SamplingSettingクラスとして振舞えます。
 */
public class SamplingConfig extends SamplingSetting {

    private AdSamplingRequest entity;

    /**
     * 指定したADSMPLREQ構造体で作成します。
     * @param entity 機器デフォルトのADSMPLREQ構造体です。
     */
    public SamplingConfig(AdSamplingRequest entity) {
        super((int)entity.chCount, entity.samplingNumber, entity.samplingFrequency);
        this.entity = entity;
    }

    public AdSamplingRequest getEntity() {
        return entity;
    }

    @Override
    public void setChCount(int chCount) {
        entity.chCount = chCount;
        for (int i = 0; i < chCount; i++) {
            entity.samplingChRequests[i].chNumber = i + 1;
        }
        super.setChCount(chCount);
    }

    @Override
    public void setSamplingNumber(int samplingNumber) {
        entity.samplingNumber = samplingNumber;
        super.setSamplingNumber(samplingNumber);
    }

    @Override
    public void setSamplingFrequency(double samplingFrequency) {
        entity.samplingFrequency = (float)samplingFrequency;
        super.setSamplingFrequency(samplingFrequency);
    }

    @Override
    public void setAll(int chCount, int samplingNumber, double samplingFrequency) {
        setChCount(chCount);
        setSamplingNumber(samplingNumber);
        setSamplingFrequency(samplingFrequency);
    }

    @Override
    public void setAll(int chCount, double mSec, double samplingFrequency) {
        setChCount(chCount);
        setSamplingNumber(timeToNumber(mSec));
        setSamplingFrequency(samplingFrequency);
    }
}
