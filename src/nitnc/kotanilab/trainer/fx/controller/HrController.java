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
    private HRField hr = new HRField();
    private HrSetting setting;

    public HrController(Pane masterPane, UserSetting userSetting) {
        super("HR", new HrAnalyzer(masterPane), userSetting);
        analyzer.setAge(userSetting.getAge());
        setting = (HrSetting) Saver.load("HrSetting");
        setVisible("Wave", "HR", "ACF", "Diff");
        if (setting != null) {
            channel.setValueAsInt(setting.getChannel());
            visible.get("Wave").setSelected(setting.getWave());
            visible.get("HR").setSelected(setting.getHr());
            visible.get("ACF").setSelected(setting.getAcf());
            visible.get("Diff").setSelected(setting.getDiff());
        } else {
            setting = new HrSetting();
        }
        analyzer.setHrEvent(hr::setHr);
        indicator.getChildren().add(hr.getWrapper());
    }

    public void start(double fs) {
        super.start(fs);
        analyzer.start(fs, 0);
    }

    public void stop() {
        analyzer.stop();
        setting.setChannel(channel.getValueAsInt());
        setting.setWave(visible.get("Wave").isSelected());
        setting.setHr(visible.get("HR").isSelected());
        setting.setAcf(visible.get("ACF").isSelected());
        setting.setDiff(visible.get("Diff").isSelected());
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
