package nitnc.kotanilab.trainer.math.analysis;

import nitnc.kotanilab.trainer.math.series.Wave;
import nitnc.kotanilab.trainer.util.Dbg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.function.UnaryOperator;

public class IirFilter {
    private static final int X0 = 0;
    private static final int X1 = 1;
    private static final int X2 = 2;
    private static final int Y1 = 3;
    private static final int Y2 = 4;

    private static final int A0 = 0;
    private static final int A1 = 1;
    private static final int A2 = 2;
    private static final int B1 = 3;
    private static final int B2 = 4;
    private static final int K = 5;

    public static DoubleUnaryOperator execute(double[] coefficient) {
        coefficient[B1] *= -1;
        coefficient[B2] *= -1;
        double[] reg = {0, 0, 0, 0, 0};
        return x -> {
            double y = 0.0;
            reg[X0] = x * coefficient[K];
            for (int i = 0; i < reg.length; i++) {
                y += reg[i] * coefficient[i];
            }

            for (int i = reg.length - 1; i > 0; i--) {
                reg[i] = reg[i - 1];
            }
            reg[Y1] = y;
            return y;
        };
    }

    public static DoubleUnaryOperator execute(double[]... coefficients) {
        List<DoubleUnaryOperator> sections = new ArrayList<>(coefficients.length);
        for (double[] coefficient : coefficients) {
            sections.add(execute(coefficient));
        }
        return x -> {
            double[] y = {x};
            sections.forEach(section -> y[0] = section.applyAsDouble(y[0]));
            return y[0];
        };
    }

    public static DoubleUnaryOperator execute(String filename) {
        List<Double> tmpList = new ArrayList<>();
        try {
            FileReader fr = new FileReader("./filter/" + filename);
            BufferedReader br = new BufferedReader(fr);
            String tmp;
            while ((tmp = br.readLine()) != null) {
                String line = tmp.trim();
                if (line.isEmpty()) continue;
                double coefficient = Double.parseDouble(line.trim());
                tmpList.add(coefficient);
            }
            br.close();

            if (tmpList.size() % 6 != 0) throw new IllegalArgumentException("係数は6の倍数個でなければなりません");
        } catch (Exception e) {
            e.printStackTrace();
        }

        int sectionNumber = tmpList.size() / 6;
        double[][] coefficients = new double[sectionNumber][6];
        for (int i = 0; i < sectionNumber; i++) {
            coefficients[i][A0] = tmpList.get(6 * i + 3);
            coefficients[i][A1] = tmpList.get(6 * i + 4);
            coefficients[i][A2] = tmpList.get(6 * i + 5);
            coefficients[i][B1] = tmpList.get(6 * i + 1);
            coefficients[i][B2] = tmpList.get(6 * i + 2);
            coefficients[i][K] = tmpList.get(6 * i);
        }

        return execute(coefficients);
    }
}
