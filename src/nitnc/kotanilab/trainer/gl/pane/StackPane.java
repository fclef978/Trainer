package nitnc.kotanilab.trainer.gl.pane;

import nitnc.kotanilab.trainer.gl.pane.Pane;

/**
 * 下から上へ重ねて子をレイアウトするPaneです。
 */
public class StackPane extends Pane {

    /**
     * コンストラクタです。
     */
    public StackPane() {
    }

    /**
     * 指定したスタイルで作成します。
     *
     * @param style スタイルシート
     */
    public StackPane(String style) {
        super(style);
    }

}
