package nitnc.kotanilab.trainer.fx.controller;

import nitnc.kotanilab.trainer.fx.setting.UserSetting;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.math.analysis.EmgAnalyzer;

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
