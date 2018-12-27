package nitnc.kotanilab.trainer.fx.controller;

import javafx.scene.control.Label;
import nitnc.kotanilab.trainer.fx.setting.MmgSetting;
import nitnc.kotanilab.trainer.fx.util.PositiveIntField;
import nitnc.kotanilab.trainer.math.analysis.MmgAnalyzer;
import nitnc.kotanilab.trainer.fx.setting.Saver;

public class MmgController extends Controller {
    private PositiveIntField samplingNumber = new PositiveIntField("256");
    private MmgSetting setting;

    public MmgController(MmgAnalyzer analyzer) {
        super(analyzer);
        setting = (MmgSetting) Saver.load("MmgSetting");
        Label label = new Label("Sampling Number");
        getChildren().addAll(label, samplingNumber);
        samplingNumber.setStyle("-fx-max-width: 50px");
        setVisible("Wave", "Spectrum", "Frequency");
        if (setting != null) {
            samplingNumber.setText(String.valueOf(setting.getSamplingNumber()));
            visible.get("Wave").setSelected(setting.getWave());
            visible.get("Spectrum").setSelected(setting.getSpectrum());
            visible.get("Frequency").setSelected(setting.getFrequency());
        } else {
            setting = new MmgSetting();
        }
    }

    @Override
    public void start(double fs) {
        super.start(fs);
        analyzer.start(fs, samplingNumber.getValueAsInt(), 1, 2.0);
    }

    @Override
    public void stop() {
        analyzer.stop();
        setting.setSamplingNumber(samplingNumber.getValueAsInt());
        setting.setWave(visible.get("Wave").isSelected());
        setting.setSpectrum(visible.get("Spectrum").isSelected());
        setting.setFrequency(visible.get("Frequency").isSelected());
        Saver.save("MmgSetting", setting);
    }
}
