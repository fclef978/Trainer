package jp.ac.numazu_ct.d14122.util;

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
        System.out.println(o);
    }

    public static void p(Object... os){
        StringBuilder str = new StringBuilder();
        for (Object o : os) {
            str.append(o).append(" ");
        }
        p(str);
    }

    public static void nc(Object o) {
        if(o == null) throw new NullPointerException("ぬるぽ！！！！！！！！！！");
    }
}
