package nitnc.kotanilab.trainer.fft.jnaNative;

import com.sun.jna.Library;
import com.sun.jna.Native;
import nitnc.kotanilab.trainer.util.Utl;

import java.io.File;

/**
 * JNAのインターフェースです。
 * fftsg.cを共有ライブラリにコンパイルしたものを使ってください。
 * Fast Fou/Cosine/Sine Transform
 * dimension   :one
 * data length :power of 2
 * decimation  :frequency
 * radix       :split-radix
 * data        :inplace
 * table       :use
 * <p>
 * 引数の説明
 * sign             :-1：離散フーリエ変換　1：離散逆フーリエ変換
 * target           :入出力配列であり。添字が偶数番目の位置に実部、奇数番目の位置に虚部を置く（だから配列の長さがFFT点数の2倍になる）。
 * FFTの結果はそのまま上書き出力される（in-place演算）。
 * work             :作業用配列。長さは2+sqrt(n)以上が必要で、最初にcdft()を呼ぶときにはip[0]=0を代入しておく。
 * trigonometric    :三角関数表用配列を渡す。長さはn/2。ip[0]=0が渡された場合に初期化される。
 * <p>
 * なんか虚数部の符号が逆らしい
 */
public interface OouraFftLibrary extends Library {
    OouraFftLibrary INSTANCE = loadLibrary();

    public static OouraFftLibrary loadLibrary() {
        String current = System.getProperty("user.dir") + File.separatorChar;
        String fileName = Utl.doByOS(() -> "fftsg.dll", () -> "libfftsg.so");
        return (OouraFftLibrary) Native.loadLibrary(current + fileName, OouraFftLibrary.class);
    }

    /**
     * 複素離散フーリエ変換
     *
     * @param n             データ長
     * @param sign          変換方向
     * @param target        入出力配列
     * @param work          作業用配列
     * @param trigonometric 三角関数配列
     */
    void cdft(int n, int sign, double[] target, int[] work, double[] trigonometric);

    /**
     * 実離散フーリエ変換
     *
     * @param n             データ長
     * @param sign          変換方向
     * @param target        入出力配列
     * @param work          作業用配列
     * @param trigonometric 三角関数配列
     */
    void rdft(int n, int sign, double[] target, int[] work, double[] trigonometric);

    /**
     * 離散コサイン変換
     *
     * @param n             データ長
     * @param sign          変換方向
     * @param target        入出力配列
     * @param work          作業用配列
     * @param trigonometric 三角関数配列
     */
    void ddct(int n, int sign, double[] target, int[] work, double[] trigonometric);

    /**
     * 離散サイン変換
     *
     * @param n             データ長
     * @param sign          変換方向
     * @param target        入出力配列
     * @param work          作業用配列
     * @param trigonometric 三角関数配列
     */
    void ddst(int n, int sign, double[] target, int[] work, double[] trigonometric);

}
