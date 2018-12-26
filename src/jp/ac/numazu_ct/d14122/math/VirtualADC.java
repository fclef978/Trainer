package jp.ac.numazu_ct.d14122.math;

import jp.ac.numazu_ct.d14122.adConverter.ADConverter;
import jp.ac.numazu_ct.d14122.adConverter.ConverterSpec;
import jp.ac.numazu_ct.d14122.adConverter.SamplingSetting;
import jp.ac.numazu_ct.d14122.math.point.PointOfWave;
import jp.ac.numazu_ct.d14122.math.series.Unit;
import jp.ac.numazu_ct.d14122.math.series.Wave;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.UnaryOperator;

/**
 * 仮想的なADコンバータです。
 * 仮想的なオシレータを内部に持ち、外部ADコンバータがないときにテストするためのものです。
 */
public class VirtualADC implements ADConverter {

    private Oscillator osc;
    private List<UnaryOperator<Double>> functions;
    private double max;
    private double min;
    private SamplingSetting setting;

    /**
     * コンストラクタです。
     * @param max 最大値
     * @param functions 発生する関数です。チャンネルの出力になります。
     */
    @SafeVarargs
    public VirtualADC(double max, double min, UnaryOperator<Double>... functions) {
        setting = new SamplingSetting(1, 256, 100) {
            @Override
            public void setAll(int chCount, int samplingNumber, double samplingFrequency) {
                if (1000 % samplingFrequency != 0 || samplingFrequency < 0 || samplingFrequency > 1000)
                    throw new IllegalArgumentException("サンプリング周波数は1000の約数である必要があります。 " + samplingFrequency + "Hz");
                super.setAll(chCount, samplingNumber, samplingFrequency);
            }
        };
        this.functions = Arrays.asList(functions);
        this.max = max;
        this.min = min;
    }

    public ConverterSpec getConverterSpec() {
        return null;
    }

    @Override
    public List<Wave> convertContinuously() {
        double fs = setting.getSamplingFrequency();
        int n = setting.getSamplingNumber();
        int ch = setting.getChCount();
        final List<Wave> waves = new ArrayList<>(1);

        for (int i = 0; i < ch; i++) {
            UnaryOperator<Double> function = functions.get(i % n);
            Wave wave = new Wave(max, min, Unit.v(), fs, 0.0); // 便宜上startTimeを0にしている。
            osc = new Oscillator(fs, function, wave::add);
            osc.start();
            try {
                Thread.sleep(Math.round(n * osc.getPeriod()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            osc.stop();
            waves.add(wave);
        }

        return waves;
    }

    @Override
    public List<WaveBuffer> convertEternally() {
        double fs = setting.getSamplingFrequency();
        int ch = setting.getChCount();
        final List<WaveBuffer> waves = new CopyOnWriteArrayList<>();

        for (int i = 0; i < ch; i++) {
            final WaveBuffer wave = new WaveBuffer(max, min, Unit.v(), fs);
            UnaryOperator<Double> function = functions.get(i % functions.size()); // ソースにたいしてチャンネルが多くてもエラーを吐かないように
            osc = new Oscillator(fs, function, x -> {
                try {
                    wave.put(x.getY());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            osc.start();
            waves.add(wave);
        }

        return waves;
    }

    @Override
    public List<PointOfWave> convertOnce() {
        return null;
    }

    @Override
    public void stop() {
        osc.stop();
    }

    @Override
    public void close() {
        osc.stop();
    }

    @Override
    public SamplingSetting getSamplingSetting() {
        return setting;
    }

    @Override
    public void setSamplingSetting(SamplingSetting samplingSetting) {
        this.setting = samplingSetting;
    }

    @Override
    public void open() {

    }
}
