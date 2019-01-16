package nitnc.kotanilab.trainer.gpg3100.wrapper;

import nitnc.kotanilab.trainer.adConverter.SamplingSetting;
import nitnc.kotanilab.trainer.gpg3100.jnaNative.AdSamplingRequest;

import java.util.ArrayList;
import java.util.List;

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
        super(new ArrayList<>(), entity.samplingNumber, entity.samplingFrequency);
        this.entity = entity;
    }

    public AdSamplingRequest getEntity() {
        return entity;
    }

    @Override
    public void setChannelList(List<Integer> channelList) {
        entity.chCount = channelList.size();
        for (Integer channel : channelList) {
            entity.samplingChRequests[channel - 1].chNumber = channel;
        }
        super.setChannelList(channelList);
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
    public void setAll(List<Integer> channelList, int samplingNumber, double samplingFrequency) {
        setChannelList(channelList);
        setSamplingNumber(samplingNumber);
        setSamplingFrequency(samplingFrequency);
    }

    @Override
    public void setAll(List<Integer> channelList, double mSec, double samplingFrequency) {
        setChannelList(channelList);
        setSamplingNumber(timeToNumber(mSec));
        setSamplingFrequency(samplingFrequency);
    }
}
