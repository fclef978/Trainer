package nitnc.kotanilab.trainer.adConverter;

import nitnc.kotanilab.trainer.math.WaveBuffer;
import nitnc.kotanilab.trainer.math.point.PointOfWave;
import nitnc.kotanilab.trainer.math.series.Wave;

import java.util.List;
import java.util.concurrent.BlockingDeque;

/**
 * ADコンバータドライバのインターフェースです。
 * TODO 設定は外部ファイル依存であるべきでは？
 */
public interface ADConverter {

    /**
     * コンバータの性能を返します。
     * @return コンバータの性能
     */
    ConverterSpec getConverterSpec();

    /**
     * サンプリング設定を返します。
     * 初期値はデフォルト設定です。
     * @return サンプリング設定
     */
    SamplingSetting getSamplingSetting();

    /**
     * サンプリング設定をセットします。
     * @param samplingSetting サンプリング設定です。
     */
    void setSamplingSetting(SamplingSetting samplingSetting);

    /**
     * サンプリング設定の条件で連続でサンプリングします。
     * サンプリング回数は有限です。
     * @return チャンネルごとの波形
     */
    List<Wave> convertContinuously();

    /**
     * 単発でサンプリングします。
     * @return チャンネルごとの値
     */
    List<PointOfWave> convertOnce();

    /**
     * stop()が呼ばれるまで連続でサンプリングします。
     * 動作は非同期になります。
     * @return チャンネルごとのバッファ
     */
    List<WaveBuffer> convertEternally();

    /**
     * サンプリングを停止します。
     */
    void stop();

    /**
     * デバイスをオープンします。
     */
    void open();

    /**
     * デバイスをクローズします。
     */
    void close();
}
