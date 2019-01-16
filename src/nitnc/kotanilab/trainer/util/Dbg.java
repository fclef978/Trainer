package nitnc.kotanilab.trainer.util;

import java.util.Arrays;

public class Dbg {
    private static long startTime = 0;

    public static void start() {
        startTime = System.nanoTime();
    }

    public static void stop() {
        System.out.println((System.nanoTime() - startTime) / 1000000000.0);
    }

    public static void p() {
        p("debug");
    }

    public static void p(Object o) {
        System.out.println(o.toString());
    }

    public static void p(double[] o) {
        System.out.println(Arrays.toString(o));
    }

    public static void p(Object... os) {
        p(" ", os);
    }

    public static void p(String separator, Object... os) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < os.length; i++) {
            if (i == os.length - 1) str.append(os[i]);
            else str.append(os[i]).append(separator);
        }
        p(str);
    }

    public static void nc(Object o) {
        if (o == null) throw new NullPointerException("ぬるぽ！！！！！！！！！！");
    }
}
