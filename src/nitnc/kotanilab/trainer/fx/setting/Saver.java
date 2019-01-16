package nitnc.kotanilab.trainer.fx.setting;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class Saver {
    private static final String suffix = ".xml";

    public static void save(String name, Object o) {
        try {
            XMLEncoder encoder = new XMLEncoder(
                    new BufferedOutputStream(
                            new FileOutputStream(name + suffix)
                    )
            );
            encoder.writeObject(o);
            encoder.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static Object load(String name) {
        try {
            XMLDecoder decoder = new XMLDecoder(
                    new BufferedInputStream(
                            new FileInputStream(name + suffix)
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
