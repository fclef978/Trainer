package nitnc.kotanilab.trainer.fx.controller;

import javafx.scene.control.Label;
import nitnc.kotanilab.trainer.fx.setting.MgSetting;
import nitnc.kotanilab.trainer.fx.setting.Saver;
import nitnc.kotanilab.trainer.fx.setting.UserSetting;
import nitnc.kotanilab.trainer.fx.util.PositiveIntField;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.math.analysis.EmgAnalyzer;
import nitnc.kotanilab.trainer.math.analysis.MmgAnalyzer;

/**
 * EMGのControllerです。
 */
public class EmgController extends MmgController {
    /**
     * コンストラクタです。
     *
     * @param masterPane  OpenGLの親ペイン
     * @param userSetting ユーザ設定
     */
    public EmgController(Pane masterPane, UserSetting userSetting) {
        super(userSetting, new EmgAnalyzer(masterPane), "EMG", "EmgSetting");
    }
}
