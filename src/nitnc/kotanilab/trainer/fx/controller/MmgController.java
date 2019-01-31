package nitnc.kotanilab.trainer.fx.controller;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import nitnc.kotanilab.trainer.fx.setting.MgSetting;
import nitnc.kotanilab.trainer.fx.setting.UserSetting;
import nitnc.kotanilab.trainer.fx.util.PositiveIntField;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.math.analysis.MmgAnalyzer;
import nitnc.kotanilab.trainer.fx.setting.Saver;

public class MmgController extends Controller<MmgAnalyzer> {
    private PositiveIntField samplingNumber = new PositiveIntField(256);
    private MgSetting setting;
    protected MmgField filed;
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
        filed = new MmgField();
        analyzer.setMfCallback(mf -> filed.setMf(mf));
        indicator.getChildren().add(filed.getWrapper());
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

    public class MmgField {
        private HBox wrapper = new HBox(3);
        private Label label = new Label("中央周波数");
        private Text mf = new Text("0");
        private Label unit = new Label("Hz");

        public MmgField() {
            wrapper.getChildren().addAll(label, mf, unit);
        }

        public HBox getWrapper() {
            return wrapper;
        }

        public void setMf(double mf) {
            this.mf.setText(String.format("%.1f", mf));
        }
    }
}
