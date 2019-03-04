package nitnc.kotanilab.trainer.math;

import nitnc.kotanilab.trainer.adConverter.ADConverter;
import nitnc.kotanilab.trainer.adConverter.ConverterSpec;
import nitnc.kotanilab.trainer.adConverter.SamplingSetting;
import nitnc.kotanilab.trainer.gl.util.PeriodicTask;
import nitnc.kotanilab.trainer.math.point.PointOfWave;
import nitnc.kotanilab.trainer.math.series.Wave;
import nitnc.kotanilab.trainer.util.Dbg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

/**
 * 仮想的なADコンバータです。
 * 仮想的なオシレータを内部に持ち、外部ADコンバータがないときにテストするためのものです。
 */
public class VirtualADC implements ADConverter {

    private PeriodicTask osc;
    private List<UnaryOperator<Double>> functions;
    private double max;
    private double min;
    private SamplingSetting setting;

    /**
     * コンストラクタです。
     *
     * @param max       最大値
     * @param functions 発生する関数です。チャンネルの出力になります。
     */
    @SafeVarargs
    public VirtualADC(double max, double min, UnaryOperator<Double>... functions) {
        setting = new SamplingSetting(Collections.emptyList(), 256, 100) {
            @Override
            public void setAll(List<Integer> chCount, int samplingNumber, double samplingFrequency) {
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
        List<Integer> channelList = setting.getChannelList();
        final List<Wave> waves = new ArrayList<>(1);

        for (int i = 0; i < channelList.size(); i++) {
            FunctionGenerator function = new FunctionGenerator(functions.get(i % n));
            Wave wave = new Wave(max, min, Unit.v(), fs, 0.0); // 便宜上startTimeを0にしている。
            osc = new PeriodicTask(() -> wave.add(function.generate()), Math.round(1000000000 / fs), TimeUnit.NANOSECONDS);
            osc.start();
            try {
                Thread.sleep(Math.round(1000 * n / fs));
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
        List<Integer> channelList = setting.getChannelList();
        final List<WaveBuffer> waves = new CopyOnWriteArrayList<>();

        for (int i = 0; i < channelList.size(); i++) {
            FunctionGenerator function = new FunctionGenerator(functions.get((channelList.get(i) - 1) % functions.size()));
            final WaveBuffer wave = new WaveBuffer(max, min, Unit.v(), fs);
            osc = new PeriodicTask(() -> {
                try {
                    wave.put(function.generate().getY());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, Math.round(1000 / fs), TimeUnit.MILLISECONDS);
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
        if (osc != null)
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
