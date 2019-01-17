package nitnc.kotanilab.trainer.fx.controller;

import javafx.scene.control.Label;
import nitnc.kotanilab.trainer.fx.setting.MgSetting;
import nitnc.kotanilab.trainer.fx.setting.Saver;
import nitnc.kotanilab.trainer.fx.setting.UserSetting;
import nitnc.kotanilab.trainer.fx.util.PositiveIntField;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.math.analysis.MmgMicAnalyzer;

public class MmgMicController extends MmgController {
    protected MmgMicController(Pane masterPane, UserSetting userSetting) {
        super(userSetting, new MmgMicAnalyzer(masterPane), "MMG(mic.)", "MmgMicSetting");
    }
}
