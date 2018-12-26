package jp.ac.numazu_ct.d14122.fx;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import jp.ac.numazu_ct.d14122.math.analysis.HrAnalyzer;
import jp.ac.numazu_ct.d14122.math.analysis.MmgAnalyzer;
import jp.ac.numazu_ct.d14122.util.Saver;

import java.io.Serializable;

public class HrController extends Controller{
    private NumberField thLowField = new NumberField("-1.0");
    private NumberField thHighField = new NumberField("1.0");
    HrAnalyzer analyzer;
    HrSetting setting;

    public HrController(HrAnalyzer analyzer) {
        super(analyzer);
        this.analyzer = analyzer;
        setting = (HrSetting) Saver.load("HrSetting");
        thLowField.setStyle("-fx-max-width: 100px;");
        thHighField.setStyle("-fx-max-width: 100px;");
        getChildren().addAll(thLowField, thHighField);
        setVisible("Wave", "HR", "Debug");
        if (setting != null) {
            thLowField.setText(String.valueOf(setting.getThLow()));
            thHighField.setText(String.valueOf(setting.getThHigh()));
            visible.get("Wave").setSelected(setting.getWave());
            visible.get("HR").setSelected(setting.getHr());
            visible.get("Debug").setSelected(setting.getDebug());
        } else {
            setting = new HrSetting();
        }
    }

    public void start(double fs) {
        super.start(fs);
        analyzer.setThresholdLower(thLowField.getValueAsDouble());
        analyzer.setThresholdHigher(thHighField.getValueAsDouble());
        analyzer.start(fs, 0, 2, 2.0);
    }

    public void stop() {
        analyzer.stop();
        setting.setThHigh(thHighField.getValueAsDouble());
        setting.setThLow(thLowField.getValueAsDouble());
        setting.setWave(visible.get("Wave").isSelected());
        setting.setHr(visible.get("HR").isSelected());
        setting.setDebug(visible.get("Debug").isSelected());
        Saver.save("HrSetting", setting);
    }
}
