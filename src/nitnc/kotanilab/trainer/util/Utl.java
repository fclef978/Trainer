package nitnc.kotanilab.trainer.util;

import java.io.File;
import java.util.function.Supplier;

public class Utl {
    public static <T> T doByOS(Supplier<T> nt, Supplier<T> posix) {
        switch (File.separatorChar) {
            case '\\':
                return nt.get();
            case '/':
                return posix.get();
            default:
                throw new RuntimeException("サポートしていないOSです。");
        }
    }

    public static void sleep(double t) {
        try {
            Thread.sleep(Math.round(t * 1000.0));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static double ceil(double x) {
        return x > 0 ? Math.ceil(x) : Math.floor(x);
    }
}
