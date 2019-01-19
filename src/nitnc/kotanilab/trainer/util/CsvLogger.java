package nitnc.kotanilab.trainer.util;

import nitnc.kotanilab.trainer.math.point.Point;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class CsvLogger {
    private String filename;
    private FileWriter fw;
    private PrintWriter pw;

    public CsvLogger(String filename) {
        this.filename = filename;
        try {
            fw = new FileWriter(filename, false);
            pw = new PrintWriter(new BufferedWriter(fw));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
