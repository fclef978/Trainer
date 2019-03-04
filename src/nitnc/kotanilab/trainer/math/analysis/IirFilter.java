package nitnc.kotanilab.trainer.math.analysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.function.UnaryOperator;

/**
 * デジタルフィルタのIIRフィルタをDoubleUnaryOperatorの形式で作成するクラスです。
 * 下のURLのサイトで作成して結果をtxtファイルにコピペして使用してください。
 * http://dsp.jpn.org/dfdesign/iir/i_bef.shtml
 * double型配列から作成することも可能です。
 */
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

    /**
     * 単一のセクションのIIRフィルタを作成します。
     *
     * @param coefficient フィルタ係数
     *                    順番は作成サイトの並びと同じです。
     * @return 作成したIIRフィルタ
     */
    public static DoubleUnaryOperator load(double[] coefficient) {
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

    /**
     * 複数のセクションを持つIIRフィルタを作成します。
     *
     * @param coefficients フィルタ係数
     *                     *                    順番は作成サイトの並びと同じです。
     * @return 作成したIIRフィルタ
     */
    public static DoubleUnaryOperator load(double[]... coefficients) {
        List<DoubleUnaryOperator> sections = new ArrayList<>(coefficients.length);
        for (double[] coefficient : coefficients) {
            sections.add(load(coefficient));
        }
        return x -> {
            double[] y = {x};
            sections.forEach(section -> y[0] = section.applyAsDouble(y[0]));
            return y[0];
        };
    }

    /**
     * ファイルに保存された係数を読み込んで作成します。
     * ファイル名は使用者が管理しますが、ファイルの保存場所はfilter/以下にしてください。
     * 推奨するファイル名は、"フィルタの種類(bpf,lefなど)パスバンド終端正規化周波数(-パスバンド始端正規化周波数).txt"です。
     *
     * @param filename フィルタ係数が保存されたファイル名
     *                 プレフィックスの./filter/が自動的に付与されるのでfilter/からの相対パスで指定してください。
     * @return 作成したIIRフィルタ
     */
    public static DoubleUnaryOperator load(String filename) {
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

        return load(coefficients);
    }
}
