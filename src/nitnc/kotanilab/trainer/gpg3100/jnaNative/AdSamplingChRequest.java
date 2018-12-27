package nitnc.kotanilab.trainer.gpg3100.jnaNative;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * ADSMPLCHREQ構造体です。
 * 各チャンネル毎のサンプリング条件を設定する構造体です。
 * ADSMPLREQ構造体のメンバ、 ADBMSMPLREQ構造体のメンバ、 ADMEMSMPLREQ構造体のメンバ、AdInputAD関数で使用されます。
 */
public class AdSamplingChRequest extends Structure {

    /**
     * サンプリングを行うチャンネルの番号を指定します。
     * 指定の範囲は以下の通りです。
     * ・ 1 ～ そのデバイスが提供する最大チャンネル番号
     */
    public long chNumber;

    /**
     * ulChNo で指定したチャンネルのレンジを指定します。
     * マルチ ADC 方式のデバイスではチャンネル毎にレンジの設定が可能です。
     * マルチプレクサ方式のデバイスでは全チャンネル同一のレンジ設定にする必要があります
     */
    public long range;

    public AdSamplingChRequest() {
        super();
    }

    public AdSamplingChRequest(Pointer p) {
        super(p);
    }

    public static class ByReference extends AdSamplingChRequest implements Structure.ByReference {}

    public static class ByValue extends AdSamplingChRequest implements Structure.ByValue {}

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("chNumber", "range");
    }

    @Override
    public String toString() {
        return "AdSamplingChRequest{" +
                "chNumber=" + chNumber +
                ", range=" + range +
                '}';
    }

}
