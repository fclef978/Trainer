package nitnc.kotanilab.trainer.math;

/**
 * 物理単位を表すイミュータブルクラスです。
 * アセットとしてstaticなメソッドやフィールドが用意されています。
 * TODO 現在は表示する際の文字列を管理するだけの機能しかありませんが、将来的には計算の補助に適用できるようにする予定
 */
public class Unit {

    private static final Unit none = new Unit("", "");
    private static final Unit hz = new Unit("Frequency", "Hz");
    private static final Unit sec = new Unit("Time", "sec.");
    private static final Unit v = new Unit("Voltage", "V");

    /**
     * 名前も単位記号もない空のUnitを返します。
     *
     * @return 名前も単位記号もない空のUnit
     */
    public static Unit none() {
        return none;
    }

    /**
     * 指定された名前のデシベルのUnitを返します。
     *
     * @param name 単位名
     * @return 指定された名前のデシベルのUnit
     */
    public static Unit db(String name) {
        return new Unit(name, "B", "d");
    }

    /**
     * 周波数のUnitを返します。
     *
     * @return 周波数のUnit
     */
    public static Unit hz() {
        return hz;
    }

    /**
     * 秒数のUnitを返します。
     *
     * @return 秒数のUnit
     */
    public static Unit sec() {
        return sec;
    }

    /**
     * 指定された名前の任意単位のUnitを返します。
     *
     * @param name 単位名
     * @return 指定された名前の任意単位のUnit
     */
    public static Unit arb(String name) {
        return new Unit(name, "arb.");
    }

    /**
     * 電圧のUnitを返します。
     *
     * @return 電圧のUnit
     */
    public static Unit v() {
        return v;
    }

    private String name;
    private String unit;
    private String prefix;
    private int exponent;

    /**
     * 指定された名前、単位記号、SI接頭辞で作成します。
     *
     * @param name   名前
     * @param unit   単位記号
     * @param prefix SI接頭辞
     */
    public Unit(String name, String unit, String prefix) {
        this.name = name;
        this.unit = unit;
        this.prefix = prefix;
        this.exponent = prefixToExponent(prefix);
    }

    /**
     * 指定された名前、単位記号で作成します。
     *
     * @param name 名前
     * @param unit 単位記号
     */
    public Unit(String name, String unit) {
        this(name, unit, "");
    }

    @Override
    public String toString() {
        return name + "[" + prefix + unit + "]";
    }

    private static int prefixToExponent(String prefix) {
        int exponent;
        switch (prefix) {
            case "k":
                exponent = 3;
                break;
            case "M":
                exponent = 6;
                break;
            case "m":
                exponent = -3;
                break;
            case "u":
                exponent = -6;
                break;
            default:
                exponent = 0;
        }
        return exponent;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public String getPrefix() {
        return prefix;
    }
}
