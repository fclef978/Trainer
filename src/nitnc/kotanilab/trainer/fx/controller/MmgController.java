package nitnc.kotanilab.trainer.fx.controller;

import javafx.scene.control.Label;
import nitnc.kotanilab.trainer.fx.setting.MgSetting;
import nitnc.kotanilab.trainer.fx.setting.UserSetting;
import nitnc.kotanilab.trainer.fx.util.PositiveIntField;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.math.analysis.MmgAnalyzer;
import nitnc.kotanilab.trainer.fx.setting.Saver;

public class MmgController extends Controller<MmgAnalyzer> {
    private PositiveIntField samplingNumber = new PositiveIntField(256);
    private MgSetting setting;
    private String settingName;

    public MmgController(Pane masterPane, UserSetting userSetting) {
        this(userSetting, new MmgAnalyzer(masterPane), "MMG", "MmgSetting");
    }

    protected MmgController(UserSetting userSetting, MmgAnalyzer analyzer, String name, String settingName) {
        super(name, analyzer, userSetting);
        this.settingName = settingName;
        setting = (MgSetting) Saver.load(settingName);
        Label label = new Label("Sampling Number");
        operator.getChildren().addAll(label, samplingNumber);
        samplingNumber.setStyle("-fx-max-width: 50px");
        setVisible("Wave", "Spectrum", "Frequency", "RMS");
        if (setting != null) {
            samplingNumber.setText(String.valueOf(setting.getSamplingNumber()));
            channel.setValueAsInt(setting.getChannel());
            visible.get("Wave").setSelected(setting.getWave());
            visible.get("Spectrum").setSelected(setting.getSpectrum());
            visible.get("Frequency").setSelected(setting.getFrequency());
            visible.get("RMS").setSelected(setting.getRms());
        } else {
            setting = new MgSetting();
        }
    }

    @Override
    public void start(double fs) {
        super.start(fs);
        analyzer.start(fs, samplingNumber.getValueAsInt());
    }

    @Override
    public void stop() {
        analyzer.stop();
        setting.setChannel(channel.getValueAsInt());
        setting.setSamplingNumber(samplingNumber.getValueAsInt());
        setting.setWave(visible.get("Wave").isSelected());
        setting.setSpectrum(visible.get("Spectrum").isSelected());
        setting.setFrequency(visible.get("Frequency").isSelected());
        setting.setRms(visible.get("RMS").isSelected());
        Saver.save(settingName, setting);
    }
}
