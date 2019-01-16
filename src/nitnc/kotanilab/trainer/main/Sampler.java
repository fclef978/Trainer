package nitnc.kotanilab.trainer.main;

import nitnc.kotanilab.trainer.adConverter.ADConverter;
import nitnc.kotanilab.trainer.adConverter.SamplingSetting;
import nitnc.kotanilab.trainer.gpg3100.wrapper.GPG3100;
import nitnc.kotanilab.trainer.math.series.Wave;

import java.awt.image.SampleModel;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class Sampler {
    ADConverter adc = new GPG3100(1);
    SamplingSetting setting = adc.getSamplingSetting();

    public Sampler() {
        try {
            setting.setAll(Arrays.asList(1), 1200000.0, 1000);
            adc.setSamplingSetting(setting);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Stand By...");
            scanner.next();
            System.out.println("GO!!");
            Wave wave = adc.convertContinuously().get(0);
            FileWriter fw = new FileWriter(".\\test.csv", false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            wave.forEach(point -> {
                pw.print(point.getX().toString() + ",");
                pw.println(point.getY().toString());
            });
            pw.close();
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(final String... args) {
        new Sampler();
    }
}
