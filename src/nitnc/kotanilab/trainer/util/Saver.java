package nitnc.kotanilab.trainer.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

/**
 * JavaBeanオブジェクトの保存・復帰を行うstaticメソッドのみを持つクラスです。
 * 内部的にXMLDecoder, XMLEncoderクラスを使用しており、そのラッパメソッドを提供します。
 */
public class Saver {
    private static final String prefix = "./javabean/";
    private static final String suffix = ".xml";

    /**
     * JavaBeanオブジェクトを指定したファイル名(拡張子なし)でファイルに保存します。
     *
     * @param filename ファイル名
     *                 "./javabean/"がプレフィックスとして自動的に付くため、javabean/からの相対パスを指定してください。
     *                 拡張子は".xml"が自動でつきます。
     * @param o        保存するオブジェクト
     */
    public static void save(String filename, Object o) {
        try {
            XMLEncoder encoder = new XMLEncoder(
                    new BufferedOutputStream(
                            new FileOutputStream(prefix + filename + suffix)
                    )
            );
            encoder.writeObject(o);
            encoder.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定したファイル名で保存されたJavaBeanオブジェクトを読み込んで返します。
     *
     * @param filename ファイル名
     *                 "./javabean/"がプレフィックスとして自動的に付くため、javabean/からの相対パスを指定してください。
     *                 拡張子は".xml"が自動でつきます。
     * @return 読み込んだオブジェクト
     */
    public static Object load(String filename) {
        try {
            XMLDecoder decoder = new XMLDecoder(
                    new BufferedInputStream(
                            new FileInputStream(prefix + filename + suffix)
                    )
            );
            Object ret = decoder.readObject();
            decoder.close();
            return ret;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
