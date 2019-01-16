package nitnc.kotanilab.trainer.fx.controller;

import javafx.scene.control.Label;
import nitnc.kotanilab.trainer.fx.setting.MgSetting;
import nitnc.kotanilab.trainer.fx.setting.Saver;
import nitnc.kotanilab.trainer.fx.setting.UserSetting;
import nitnc.kotanilab.trainer.fx.util.PositiveIntField;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.math.analysis.EmgAnalyzer;

public class EmgController extends Controller<EmgAnalyzer> {
    private PositiveIntField samplingNumber = new PositiveIntField("256");
    private MgSetting setting;

    public EmgController(Pane masterPane, UserSetting userSetting) {
        super("EMG", new EmgAnalyzer(masterPane), userSetting);
        setting = (MgSetting) Saver.load("EmgSetting");
        Label label = new Label("Sampling Number");
        operator.getChildren().addAll(label, samplingNumber);
        samplingNumber.setStyle("-fx-max-width: 50px");
        setVisible("Wave", "Spectrum", "Frequency");
        if (setting != null) {
            samplingNumber.setText(String.valueOf(setting.getSamplingNumber()));
            channel.setValueAsInt(setting.getChannel());
            visible.get("Wave").setSelected(setting.getWave());
            visible.get("Spectrum").setSelected(setting.getSpectrum());
            visible.get("Frequency").setSelected(setting.getFrequency());
        } else {
            setting = new MgSetting();
        }
    }

    @Override
    public void start(double fs) {
        super.start(fs);
        analyzer.start(fs, samplingNumber.getValueAsInt(), 1, 3.0);
    }

    @Override
    public void stop() {
        analyzer.stop();
        setting.setChannel(channel.getValueAsInt());
        setting.setSamplingNumber(samplingNumber.getValueAsInt());
        setting.setWave(visible.get("Wave").isSelected());
        setting.setSpectrum(visible.get("Spectrum").isSelected());
        setting.setFrequency(visible.get("Frequency").isSelected());
        Saver.save("EmgSetting", setting);
    }
}
