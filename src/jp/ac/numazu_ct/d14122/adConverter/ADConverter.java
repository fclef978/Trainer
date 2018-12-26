package jp.ac.numazu_ct.d14122.adConverter;

import jp.ac.numazu_ct.d14122.math.WaveBuffer;
import jp.ac.numazu_ct.d14122.math.point.PointOfWave;
import jp.ac.numazu_ct.d14122.math.series.Wave;

import java.util.List;
import java.util.concurrent.BlockingDeque;

/**
 * ADコンバータドライバのインターフェースです。
 * TODO 設定は外部ファイル依存であるべきでは？
 */
public interface ADConverter {

    /**
     * コンバータの性能を返します。
     * @return コンバータの性能です。
     */
    ConverterSpec getConverterSpec();

    /**
     * サンプリング設定を返します。
     * 初回呼び出し時にデフォルト設定を返します。
     * @return
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
     * @return チャンネルごとの波形です。
     */
    List<Wave> convertContinuously();

    /**
     * 単発でサンプリングします。
     * @return チャンネルごとの値です。
     */
    List<PointOfWave> convertOnce();

    /**
     * stop()が呼ばれるまで連続でサンプリングします。
     * 動作は非同期になります。
     * @return チャンネルごとのバッファです。
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
