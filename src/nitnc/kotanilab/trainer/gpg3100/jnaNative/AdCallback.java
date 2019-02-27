package nitnc.kotanilab.trainer.gpg3100.jnaNative;


import com.sun.jna.Callback;

/**
 * サンプリング停止時に呼び出されるコールバック関数です。
 * GPG-3100のPLPADCALLBACK型に対応しています。
 */
public interface AdCallback extends Callback {
    /**
     * 処理の本体です。
     * このメソッドをオーバーライドして使用してください。
     *
     * @param uData AdSetBoardConfig()メソッドで登録されたユーザ・データ
     */
    public void invoke(int uData);
}
