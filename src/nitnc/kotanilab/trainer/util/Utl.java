package nitnc.kotanilab.trainer.util;

import java.io.File;
import java.util.function.Supplier;

/**
 * 様々なユーティリティメソッドを持つクラスです。
 */
public class Utl {
    /**
     * Windows系とLinux系で異なるSupplierを実行させるメソッドです。
     * @param nt WindowsのSupplier
     * @param posix LinuxのSupplier
     * @param <T> Supplierのクラス
     * @return Supplierの結果
     */
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

    /**
     * 現在のスレッドを指定した秒数分スリープさせます。
     * Thread.sleep()をラップするユーティリティメソッドです。
     * @param t スリープする秒数[ミリ秒]
     */
    public static void sleep(double t) {
        try {
            Thread.sleep(Math.round(t * 1000.0));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 与えられた数を絶対値の大きい方向へ丸めます。
     * @param x 丸める数
     * @return 丸められた数
     */
    public static double ceil(double x) {
        return x > 0 ? Math.ceil(x) : Math.floor(x);
    }

    /**
     * 与えられた数を絶対値の小さい方向へ丸めます。
     * @param x 丸める数
     * @return 丸められた数
     */
    public static double floor(double x) {
        return x > 0 ? Math.floor(x) : Math.ceil(x);
    }
}
