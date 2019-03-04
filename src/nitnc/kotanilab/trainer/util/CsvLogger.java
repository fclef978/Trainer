package nitnc.kotanilab.trainer.util;

import nitnc.kotanilab.trainer.math.point.Point;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * データをCSV形式でファイルに保存するクラスです。
 */
public class CsvLogger {
    private String filename;
    private FileWriter fw;
    private PrintWriter pw;

    /**
     * 指定したファイル名で作成します。
     *
     * @param filename CSVのファイル名
     */
    public CsvLogger(String filename) {
        this.filename = filename;
        try {
            fw = new FileWriter(filename, false);
            pw = new PrintWriter(new BufferedWriter(fw));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定された複数のオブジェクトをtoString()して一行に書き込みます。
     * オブジェクトはコンマでつながれ、最後に改行されます。
     *
     * @param vs 一行に書き込むオブジェクト
     * @return 書き込みに成功したらtrue
     */
    public boolean print(Object... vs) {
        try {
            for (Object v : vs) {
                pw.print(v);
                pw.print(",");
            }
            pw.println();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 指定されたPointを書き込みます。
     * X,Y\n
     * のように書き込まれます。
     *
     * @param point 書き込むPoint
     * @return 書き込みに成功したらtrue
     */
    public boolean print(Point point) {
        try {
            pw.print(point.getX());
            pw.print(",");
            pw.println(point.getY());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * ファイルを閉じます。
     * ファイナライザでも呼び出されますが、書き込みが終わったら呼び出してください。
     */
    public void close() {
        try {
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }
}
