package nitnc.kotanilab.trainer.gpg3100.jnaNative;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

/**
 * AdDataConv 関数で使用するコールバック関数です。AdDataConv 関数の lpfnConv に fnConv 関数
 * へのポインタを設定することにより、データ変換時に fnConv 関数を呼び出すことができます。
 * fnConv 関数は、データ 1 点毎に呼び出されます。
 * PADCONVPROC型に対応しています。
 */
public interface AdConversionProcedure extends Callback {
    /**
     * 処理の本体です。
     * このメソッドをオーバーライドして使用してください。
     *
     * @param wCh     pData が指すデータのチャンネル番号
     * @param dwCount pData が指すデータが、データの先頭から何番目にあたるか
     * @param pDat    AdDataConv 関数により変換されたデータが格納されているデータへのポインタ
     */
    public void invoke(int wCh, long dwCount, Pointer pDat);
}
