package nitnc.kotanilab.trainer.fx.controller;

import javafx.scene.control.Label;
import javafx.scene.text.Text;
import nitnc.kotanilab.trainer.fx.util.DoubleField;
import nitnc.kotanilab.trainer.fx.setting.HrSetting;
import nitnc.kotanilab.trainer.math.analysis.HrAnalyzer;
import nitnc.kotanilab.trainer.fx.setting.Saver;

public class HrController extends Controller {
    private DoubleField thLowField = new DoubleField("-1.0");
    private DoubleField thHighField = new DoubleField("1.0");
    private Text hr = new Text("0");
    private HrAnalyzer analyzer;
    private HrSetting setting;

    public HrController(HrAnalyzer analyzer) {
        super(analyzer);
        this.analyzer = analyzer;
        setting = (HrSetting) Saver.load("HrSetting");
        thLowField.setStyle("-fx-max-width: 50px;");
        thLowField.setOnAction(event -> analyzer.setThresholdLower(thLowField.getValueAsDouble()));
        thHighField.setStyle("-fx-max-width: 50px;");
        thHighField.setOnAction(event -> analyzer.setThresholdHigher(thHighField.getValueAsDouble()));
        getChildren().addAll(new Label("Low"), thLowField, new Label("High"), thHighField);
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
        analyzer.setHrEvent(value -> hr.setText(String.valueOf(Math.round(value))));
        getChildren().add(hr);
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
