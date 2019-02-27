package nitnc.kotanilab.trainer.gpg3100.jnaNative;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * デバイスの仕様を格納するクラスです。AdGetDeviceInfo関数で使用されます。
 * GPG-3100のADBOARDSPEC構造体に対応しています。
 */
public class AdBoardSpec extends Structure {

    public AdBoardSpec() {
        super();
    }

    public AdBoardSpec(Pointer pointer) {
        super(pointer);
    }

    public static class ByReference extends AdBoardSpec implements Structure.ByReference {}

    public static class ByValue extends AdBoardSpec implements Structure.ByValue {}

    @Override
    public String toString() {
        return "AdBoardSpec{" +
                "boardType=" + boardType +
                ", boardId=" + boardId +
                ", samplingMode=" + samplingMode +
                ", chCountSingleEnd=" + chCountSingleEnd +
                ", chCountDifferential=" + chCountDifferential +
                ", resolution=" + resolution +
                ", range=" + range +
                ", isolation=" + isolation +
                ", dIn=" + dIn +
                ", dOut=" + dOut +
                '}';
    }

    /**
     * デバイスの型式番号を格納します。
     * 例：PCI-3135であれば、3135が格納されます。CTP-3135であれば3135が格納されます。
     */
    public long boardType;
    /**
     * デバイスの識別番号（RSW1 の値）を格納します。
     */
    public long boardId;
    /**
     * デバイスが対応しているサンプリング方式をビットアサインで格納します。
     */
    public long samplingMode;
    /**
     * シングルエンド入力時のデバイスのチャンネル数を格納します。
     */
    public long chCountSingleEnd;
    /**
     * 差動入力時のデバイスのチャンネル数を格納します。
     */
    public long chCountDifferential;
    /**
     * デバイスの分解能を格納します。
     * ・12bit AD デバイスの場合：12 が格納されます。
     */
    public long resolution;
    /**
     * デバイスが対応しているレンジをビットアサインで格納します。
     */
    public long range;
    /**
     * デバイスの絶縁／非絶縁を格納します。
     */
    public long isolation;
    /**
     * デバイスのデジタル入力点数を格納します。
     */
    public long dIn;
    /**
     * デバイスのデジタル出力点数を格納します。
     */
    public long dOut;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("boardType", "boardId", "samplingMode", "chCountSingleEnd", "chCountDifferential", "resolution", "range", "isolation", "dIn", "dOut");
    }
}
