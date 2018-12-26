package jp.ac.numazu_ct.d14122.gl.util;

/**
 * Created by Hirokazu SUZUKI on 2018/07/16.
 * 24bitRGBカラー
 */
public class Color {
    public static int max = 255;

    /** assets */
    public static Color WHITE = new Color(max, max, max);
    public static Color GRAY = new Color(max / 2, max / 2, max / 2);
    public static Color BLACK = new Color(0, 0, 0);
    public static Color RED = new Color(max, 0, 0);
    public static Color GREEN = new Color(0, max, 0);
    public static Color BLUE = new Color(0, 0, max);
    public static Color YELLOW = new Color(max, max, 0);
    public static Color CYAN = new Color(0, max, max);
    public static Color PURPLE = new Color(max, 0, max);

    private int red;
    private int green;
    private int blue;

    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public float getRed() {
        return (float)red / (float)max;
    }

    public float getBlue() {
        return (float)blue / (float)max;
    }

    public float getGreen() {
        return (float)green / (float)max;
    }
}
