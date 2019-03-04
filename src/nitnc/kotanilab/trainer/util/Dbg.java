package nitnc.kotanilab.trainer.util;

import java.util.Arrays;

/**
 * デバッグ用のstaticメソッドが集められたクラスです。
 */
public class Dbg {
    private static long startTime = 0;

    /**
     * 時間測定を開始します。
     * 並列稼動は不可能です。
     */
    public static void start() {
        startTime = System.nanoTime();
    }

    /**
     * 時間測定を終了して経過時間をコンソールに出力します。
     */
    public static void stop() {
        Dbg.p((System.nanoTime() - startTime) / 1000000000.0);
    }

    /**
     * コンソールに"debug"と出力します。
     */
    public static void p() {
        p("debug");
    }

    /**
     * コンソールにオブジェクトのtoString()結果を出力します。
     * @param o 出力するオブジェクト
     */
    public static void p(Object o) {
        System.out.println(o.toString());
    }

    /**
     * コンソールに数値の配列を出力します。
     * @param vs 出力する数値の配列
     */
    public static void p(double... vs) {
        System.out.println(Arrays.toString(vs));
    }

    /**
     * コンソールにオブジェクト配列のtoString()結果を出力します。
     * @param os 出力するオブジェクトの配列
     */
    public static void p(Object... os) {
        p(" ", os);
    }

    /**
     * 指定したセパレータでセパレートしてコンソールにオブジェクトのtoString()結果を出力します。
     * @param separator 文字列を仕切るセパレータ
     * @param os 出力するオブジェクトの配列
     */
    public static void p(String separator, Object... os) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < os.length; i++) {
            if (i == os.length - 1) str.append(os[i]);
            else str.append(os[i]).append(separator);
        }
        p(str);
    }
}
