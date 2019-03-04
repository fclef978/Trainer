package nitnc.kotanilab.trainer.fx.controller;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import nitnc.kotanilab.trainer.fx.setting.UserSetting;
import nitnc.kotanilab.trainer.fx.setting.HrSetting;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.math.analysis.HrAnalyzer;
import nitnc.kotanilab.trainer.util.Saver;

/**
 * 心拍数のControllerです。
 */
public class HrController extends Controller<HrAnalyzer> {
    /**
     * レジスタンストレーニングに最適な運動強度
     */
    public static final double OPT_MET = 0.7;
    private Indicator hr = new Indicator();
    private HrSetting setting;

    /**
     * コンストラクタです。
     *
     * @param masterPane  OpenGLの親ペイン
     * @param userSetting ユーザ設定
     */
    public HrController(Pane masterPane, UserSetting userSetting) {
        super("HR", new HrAnalyzer(masterPane), userSetting);
        analyzer.setAge(userSetting.getAge());
        setting = (HrSetting) Saver.load("HrSetting");
        addAllVisible("Wave", "HR", "ACF", "Diff");
        if (setting != null) {
            channel.setValue(setting.getChannel());
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

    @Override
    public void start(double fs) {
        super.start(fs);
        analyzer.start(fs, 0);
    }

    @Override
    public void stop() {
        analyzer.stop();
        setting.setChannel(channel.getValue());
        setting.setWave(visible.get("Wave").isSelected());
        setting.setHr(visible.get("HR").isSelected());
        setting.setAcf(visible.get("ACF").isSelected());
        setting.setDiff(visible.get("Diff").isSelected());
        Saver.save("HrSetting", setting);
    }

    /**
     * 年齢から最大心拍数を計算します。
     *
     * @param age 年齢
     * @return 最大心拍数
     */
    public static double getMaxHR(int age) {
        return 220 - age;
    }

    /**
     * HRのインジケータです。
     * 表示に必要なコントロールを持ちます。
     */
    private class Indicator {
        private HBox wrapper = new HBox(3);
        private Label label = new Label("心拍数");
        private Text hr = new Text("0");
        private Label unit = new Label("bpm");

        private Indicator() {
            wrapper.getChildren().addAll(label, hr, unit);
        }

        private HBox getWrapper() {
            return wrapper;
        }

        private void setHr(double val) {
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
