package nitnc.kotanilab.trainer.fx.controller;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import nitnc.kotanilab.trainer.fx.setting.UserSetting;
import nitnc.kotanilab.trainer.fx.util.DoubleField;
import nitnc.kotanilab.trainer.fx.setting.HrSetting;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.math.analysis.HrAnalyzer;
import nitnc.kotanilab.trainer.fx.setting.Saver;

public class HrController extends Controller<HrAnalyzer> {
    public static final double OPT_MET = 0.7;

    private DoubleField thLowField = new DoubleField("-1.0");
    private DoubleField thHighField = new DoubleField("1.0");
    private HRField hr = new HRField();
    private HrSetting setting;

    public HrController(Pane masterPane, UserSetting userSetting) {
        super("HR", new HrAnalyzer(masterPane), userSetting);
        analyzer.setAge(userSetting.getAge());
        setting = (HrSetting) Saver.load("HrSetting");
        thLowField.setStyle("-fx-max-width: 50px;");
        thLowField.setOnAction(event -> analyzer.setThresholdLower(thLowField.getValueAsDouble()));
        thHighField.setStyle("-fx-max-width: 50px;");
        thHighField.setOnAction(event -> analyzer.setThresholdHigher(thHighField.getValueAsDouble()));
        operator.getChildren().addAll(new Label("Low"), thLowField, new Label("High"), thHighField);
        setVisible("Wave", "HR", "Debug");
        if (setting != null) {
            channel.setValueAsInt(setting.getChannel());
            thLowField.setText(String.valueOf(setting.getThLow()));
            thHighField.setText(String.valueOf(setting.getThHigh()));
            visible.get("Wave").setSelected(setting.getWave());
            visible.get("HR").setSelected(setting.getHr());
            visible.get("Debug").setSelected(setting.getDebug());
        } else {
            setting = new HrSetting();
        }
        analyzer.setHrEvent(hr::setHr);
        indicator.getChildren().add(hr.getWrapper());
    }

    public void start(double fs) {
        super.start(fs);
        analyzer.setThresholdLower(thLowField.getValueAsDouble());
        analyzer.setThresholdHigher(thHighField.getValueAsDouble());
        analyzer.start(fs, 0, 2, 2.0);
    }

    public void stop() {
        analyzer.stop();
        setting.setChannel(channel.getValueAsInt());
        setting.setThHigh(thHighField.getValueAsDouble());
        setting.setThLow(thLowField.getValueAsDouble());
        setting.setWave(visible.get("Wave").isSelected());
        setting.setHr(visible.get("HR").isSelected());
        setting.setDebug(visible.get("Debug").isSelected());
        Saver.save("HrSetting", setting);
    }

    public static double getMaxHR(int age) {
        return 220 - age;
    }

    public class HRField {
        private HBox wrapper = new HBox(3);
        private Label label = new Label("心拍数");
        private Text hr = new Text("0");

        public HRField() {
            wrapper.getChildren().addAll(label, hr);
        }

        public HBox getWrapper() {
            return wrapper;
        }

        public void setHr(double val) {
            hr.setText(String.format("%.1f", val));
            if (val > getMaxHR(userSetting.getAge()) * OPT_MET + 10) {
                hr.setStyle("-fx-stroke: red");
            } else if (val > getMaxHR(userSetting.getAge()) * OPT_MET - 10) {
                hr.setStyle("-fx-stroke: green");
            } else {
                hr.setStyle("-fx-stroke: blue");
            }
        }
    }
}
