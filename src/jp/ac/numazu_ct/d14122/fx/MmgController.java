package jp.ac.numazu_ct.d14122.fx;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import jp.ac.numazu_ct.d14122.math.analysis.MmgAnalyzer;
import jp.ac.numazu_ct.d14122.util.Saver;

public class MmgController extends Controller {
    private NumberField samplingNumber = new NumberField("256");
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
