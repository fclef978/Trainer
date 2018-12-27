package nitnc.kotanilab.trainer.gpg3100.jnaNative;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * ADSMPLREQ 構造体
 * サンプリング条件設定関数(AdSetSamplingConfig関数)、データ変換関数(AdDataConv関数)で指定するサンプリング時の条件を設定する構造体です。
 */
public class AdSamplingRequest extends Structure {
    public AdSamplingRequest() {
        super();
    }

    public AdSamplingRequest(Pointer p) {
        super(p);
    }

    public static class ByReference extends AdSamplingRequest implements Structure.ByReference {}

    public static class ByValue extends AdSamplingRequest implements Structure.ByValue {}

    @Override
    public String toString() {
        return "AdSamplingRequest{" +
                "chCount=" + chCount +
                ", samplingMode=" + samplingMode +
                ", singleDiff=" + singleDiff +
                ", samplingNumber=" + samplingNumber +
                ", samplingEventNumber=" + samplingEventNumber +
                ", samplingFrequency=" + samplingFrequency +
                ", triggerPoint=" + triggerPoint +
                ", triggerMode=" + triggerMode +
                ", triggerDelay=" + triggerDelay +
                ", triggerCh=" + triggerCh +
                ", triggerLevel1=" + triggerLevel1 +
                ", triggerLevel2=" + triggerLevel2 +
                ", externalClockEdge=" + externalClockEdge +
                ", analogTriggerPulse=" + analogTriggerPulse +
                ", triggerEdge=" + triggerEdge +
                ", triggerDIn=" + triggerDIn +
                ", fastMode=" + fastMode +
                '}';
    }

    /**
     * サンプリングを行うチャンネルの数を 1 からデバイスが提供する最大チャンネル数の範囲で指定します。サンプリングを行うチャンネル番号はADSMPLCHREQ 構造体の ulChNo メンバで指定します。
     * ・デフォルト：1
     */
    public long chCount;
    /**
     * 各チャンネル毎のサンプリング条件を設定する ADSMPLCHREQ 構造体です。
     */
    public AdSamplingChRequest[] samplingChRequests = new AdSamplingChRequest[256];
    /**
     * サンプリング方式を指定します。
     */
    public long samplingMode;
    /**
     * 入力仕様を指定します。
     */
    public long singleDiff;
    /**
     * サンプリングするデータの個数を PC 上で確保できるメモリ領域の範囲で指定します。
     * ・デフォルト：1024
     */
    public int samplingNumber;
    /**
     * 通知サンプリング件数を指定します。サンプリング済み件数が本メンバで指定した件数に達するたびにイベントが通知されます。
     * ・デフォルト：0
     */
    public long samplingEventNumber;
    /**
     * サンプリング周波数を 0.01f からデバイスが提供する最大サンプリング周波数の範囲で、Hz を単位として設定します。
     * 外部クロックを使用する場合は、0.0f を指定してください。
     */
    public float samplingFrequency;
    /**
     * トリガポイントを指定します。
     */
    public long triggerPoint;
    /**
     * トリガを指定します。
     */
    public long triggerMode;
    /**
     * トリガディレイをサンプルの個数を単位として指定します。
     * triggerMode でトリガが設定されている場合に有効です。
     */
    public long triggerDelay;
    /**
     * トリガ判定を行うチャンネル番号を指定します。
     * triggerMode に、レベルトリガが指定されている場合に有効です。
     * triggerCh に設定されるチャンネルは、ADSMPLCHREQ 構造体で指定されていなければなりません。
     */
    public long triggerCh;
    /**
     * トリガレベル1を指定します。ulTrigMode にレベルトリガが指定されているときに有効です。指定する値は、入力レンジが電圧であれば［V］を、電流であれば［mA］を単位として指定します。
     */
    public float triggerLevel1;
    /**
     * トリガレベル2を指定します。ulTrigMode にレベルトリガが指定されているときに有効です。指定する値は、入力レンジが電圧であれば［V］を、電流であれば［mA］を単位として指定します。
     */
    public float triggerLevel2;
    /**
     * 外部クロック入力のエッジ極性を極性指定識別子のうちいずれか 1 つを指定します。fSmplFreq に 0.0f（外部クロック）が指定されている場合に有効です。
     */
    public long externalClockEdge;
    /**
     * アナログトリガ出力のパルス極性をパルス極性指定識別子のうちいずれか 1つを指定します。アナログトリガ出力機能があるデバイスの場合に有効です。
     */
    public long analogTriggerPulse;
    /**
     * 外部トリガの極性を極性指定識別子のうちいずれか 1 つを指定します。
     * triggerModeに外部トリガまたはDIマスク付き外部トリガが指定されている場合に有効です。
     */
    public long triggerEdge;
    /**
     * 汎用デジタル入力端子による外部トリガのマスクを指定します。
     * ulTrigDI の 16bit のうちいずれか 1bit を 1 にセットします。
     * 1 にセットされている bit に対応した汎用デジタル入力端子の状態が Lowレベルになっている間、外部トリガ入力が有効となります。
     * 1 をセットする bit の位置は、使用するデバイスが持っている汎用デジタル入力端子の数に注意してください。
     * triggerMode に DI マスク付き外部トリガが指定されている場合に有効です。
     * triggerDInの形式は、デジタル入力データの形式と同じ形式です。
     * ・デフォルト：1
     */
    public long triggerDIn;
    /**
     * 倍速モードの指定をします。
     */
    public long fastMode;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("chCount", "samplingChRequests", "samplingMode", "singleDiff", "samplingNumber",
                "samplingEventNumber", "samplingFrequency", "triggerPoint", "triggerMode", "triggerDelay", "triggerCh",
                "triggerLevel1", "triggerLevel2", "externalClockEdge", "analogTriggerPulse", "triggerEdge", "triggerDIn", "fastMode");
    }
}
