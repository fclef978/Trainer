package jp.ac.numazu_ct.d14122.gpg3100.wrapper;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;
import jp.ac.numazu_ct.d14122.adConverter.ADConverter;
import jp.ac.numazu_ct.d14122.adConverter.ConverterSpec;
import jp.ac.numazu_ct.d14122.adConverter.SamplingSetting;
import jp.ac.numazu_ct.d14122.gl.util.PeriodicTask;
import jp.ac.numazu_ct.d14122.math.WaveBuffer;
import jp.ac.numazu_ct.d14122.math.point.PointOfWave;
import jp.ac.numazu_ct.d14122.math.series.Unit;
import jp.ac.numazu_ct.d14122.math.series.Wave;
import jp.ac.numazu_ct.d14122.gpg3100.jnaNative.*;
import jp.ac.numazu_ct.d14122.util.Dbg;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * GPG3100のラッパクラスです。
 */
public class GPG3100 implements ADConverter {

    private GPG3100Library instance = GPG3100Library.INSTANCE;

    private int deviceNumber;
    private BoardSpec converterSpec;
    private SamplingConfig samplingSetting;
    private boolean closed = false;
    private PeriodicTask task;

    /**
     * 指定したデバイス番号で作成します。
     * @param deviceNumber デバイス番号です。
     */
    public GPG3100(int deviceNumber) {
        this.deviceNumber = deviceNumber;
        open();
        converterSpec = getDeviceInfo();
        samplingSetting = getSamplingConfig();
    }

    @Override
    public SamplingSetting getSamplingSetting() {
        return samplingSetting;
    }

    @Override
    public ConverterSpec getConverterSpec() {
        return converterSpec;
    }

    @Override
    public void setSamplingSetting(SamplingSetting samplingSetting) {
        this.samplingSetting.setAll(
                samplingSetting.getChCount(),
                samplingSetting.getSamplingNumber(),
                samplingSetting.getSamplingFrequency()
        );
        evaluateErrorCode(
                instance.AdSetSamplingConfig(deviceNumber, (AdSamplingRequest.ByReference) this.samplingSetting.getEntity())
        );
    }

    @Override
    public List<Wave> convertContinuously() {
        int size = samplingSetting.getSamplingNumber(), ch = samplingSetting.getChCount();
        double fs = samplingSetting.getSamplingFrequency();
        List<Wave> waveList = new ArrayList<>(ch);
        LongByReference num = new LongByReference(size);
        Pointer pointer = new Memory(size * ch * Native.getNativeSize(Short.TYPE));
        double samplingStartTime = System.nanoTime() / 1000000000.0;

        evaluateErrorCode(instance.AdStartSampling(deviceNumber, Defines.FLAG_SYNC));
        evaluateErrorCode(instance.AdGetSamplingData(deviceNumber, pointer, num));

        int length = (int) num.getValue();
        for (int i = 0; i < ch; i++) {
            Wave wave = new Wave(length, converterSpec.getRange(), -converterSpec.getRange(), Unit.v(), fs, 0.0);
            for (int j = 0; j < length; j++) {
                short data = pointer.getShort((i + j * ch) * Native.getNativeSize(Short.TYPE));
                wave.add(new PointOfWave(
                        samplingStartTime + j / getSamplingSetting().getSamplingFrequency(),
                        shortToDouble(data)
                ));
            }
            waveList.add(wave);
        }
        return waveList;
    }

    @Override
    public List<PointOfWave> convertOnce() {
        return null;
    }

    @Override
    public List<WaveBuffer> convertEternally() {
        final List<WaveBuffer> list = new CopyOnWriteArrayList<>();
        int ch = samplingSetting.getChCount();

        for (int i = 0; i < samplingSetting.getChCount(); i++)
            list.add(new WaveBuffer(converterSpec.getRange(), -converterSpec.getRange(), Unit.v(), samplingSetting.getSamplingFrequency()));

        LongByReference status = new LongByReference(0);
        LongByReference count = new LongByReference(0);
        LongByReference available = new LongByReference(0);
        final long[] previous = {0};

        task = new BufferTask((int) (1.0 / samplingSetting.getSamplingFrequency()), n -> {
            evaluateErrorCode(instance.AdGetStatus(deviceNumber, status, count, available));
            long length = count.getValue() - previous[0];
            long offset = previous[0] % samplingSetting.getSamplingNumber();
            double fs = samplingSetting.getSamplingFrequency();

            if (length > 0) {
                LongByReference size = new LongByReference(length);
                Pointer pointer = new Memory(length * ch * Native.getNativeSize(Short.TYPE));
                evaluateErrorCode(instance.AdReadSamplingBuffer(deviceNumber, offset, size, pointer));
                try {
                    for (int i = 0; i < ch; i++) {
                        WaveBuffer wave = list.get(i);
                        for (int j = 0; j < length; j++) {
                            short data = pointer.getShort((i + j * ch) * Native.getNativeSize(Short.TYPE));
                            wave.put(shortToDouble(data));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            previous[0] = count.getValue();
        });

        samplingSetting.getEntity().triggerMode = 9;
        setSamplingSetting(samplingSetting);
        evaluateErrorCode(instance.AdStartSampling(deviceNumber, Defines.FLAG_ASYNC));

        task.start();

        return list;
    }

    @Override
    public void stop() {
        if (task != null) task.stop();
        evaluateErrorCode(instance.AdStopSampling(deviceNumber));
    }

    @Override
    public void open() {
        evaluateErrorCode(instance.AdOpen(deviceNumber));
    }

    @Override
    public void close() {
        if (!closed) {
            instance.AdClose(deviceNumber);
            closed = true;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {
            close();
        }
    }

    private BoardSpec getDeviceInfo() {
        AdBoardSpec bs = new AdBoardSpec.ByReference();
        evaluateErrorCode(instance.AdGetDeviceInfo(deviceNumber, (AdBoardSpec.ByReference) bs));
        return new BoardSpec(bs);
    }

    private SamplingConfig getSamplingConfig() {
        AdSamplingRequest sr = new AdSamplingRequest.ByReference();
        evaluateErrorCode(instance.AdGetSamplingConfig(deviceNumber, (AdSamplingRequest.ByReference) sr));
        return new SamplingConfig(sr);
    }

    private double shortToDouble(short val) {
        int data;

        if (val < 0) data = val + 0x8000;
        else data = val - 0x8000;

        return (double) (data) / (double) 0x8000 * 5.0;
    }

    /**
     * CSVに出力します。
     * @param fileName CSVのファイルネームです。
     */
    public void convertToCsv(String fileName) {
        evaluateErrorCode(instance.AdStartFileSampling(deviceNumber, fileName, Defines.FLAG_CSV));
    }

    private void evaluateErrorCode(int errorCode) {
        switch (errorCode) {
            case ErrorCode.AD_ERROR_SUCCESS:
                break;
            case ErrorCode.AD_ERROR_INVALID_DEVICENO:
                throw new IllegalArgumentException("デバイス番号が正しくありません");
            case ErrorCode.AD_ERROR_NOT_DEVICE:
                throw new IllegalArgumentException("デバイスが存在しません No=" + deviceNumber);
            case ErrorCode.AD_ERROR_NOT_OPEN:
                throw new IllegalArgumentException("ドライバをオープンできません");
            case ErrorCode.AD_ERROR_NOW_SAMPLING:
                throw new IllegalArgumentException("サンプリング中です");
            case ErrorCode.AD_ERROR_INVALID_PARAMETER:
                throw new IllegalArgumentException("不正な設定値です");
            case ErrorCode.AD_ERROR_FILE_OPEN:
                throw new IllegalArgumentException("存在しないファイルです");
            case ErrorCode.AD_ERROR_FILE_CLOSE:
                throw new IllegalArgumentException("ファイルクローズに失敗しました ファイルの破損に注意してください");
            case ErrorCode.AD_ERROR_FILE_WRITE:
                throw new IllegalArgumentException("ファイルに書き込めません");
            case ErrorCode.AD_ERROR_NULL_POINTER:
                throw new NullPointerException("バッファがNULLです");
            case ErrorCode.AD_ERROR_GET_DATA:
                throw new NullPointerException("サンプリングが行われていません");
            case ErrorCode.AD_ERROR_ALREADY_OPEN:
                throw new IllegalArgumentException("すでにオープンしているデバイスです No=" + deviceNumber);
            default:
                throw new IllegalArgumentException("不明なエラーです : " + Integer.toHexString(errorCode));
        }
    }

    private class BufferTask extends PeriodicTask {

        private BufferTask(int period, Consumer<Long> callback) {
            super(period < 1 ? 1 : period);
            setCallback(callback);
        }

    }

}
