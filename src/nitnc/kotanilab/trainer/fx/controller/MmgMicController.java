package nitnc.kotanilab.trainer.fx.controller;

import nitnc.kotanilab.trainer.fx.setting.UserSetting;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.math.analysis.MmgMicAnalyzer;

/**
 * センサがマイクのMMGのControllerです。
 */
public class MmgMicController extends MmgController {
    /**
     * コンストラクタです。
     *
     * @param masterPane  OpenGLの親ペイン
     * @param userSetting ユーザ設定
     */
    protected MmgMicController(Pane masterPane, UserSetting userSetting) {
        super(userSetting, new MmgMicAnalyzer(masterPane), "MMG(mic.)", "MmgMicSetting");
    }
}
