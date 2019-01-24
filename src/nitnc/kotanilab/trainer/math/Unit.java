package nitnc.kotanilab.trainer.math;

/**
 * 物理単位です。
 */
public class Unit {

    private static final Unit none = new Unit("", "");
    private static final Unit hz = new Unit("Frequency", "Hz");
    private static final Unit sec = new Unit("Time", "sec.");
    private static final Unit v = new Unit("Voltage", "V");

    public static Unit none() {
        return none;
    }

    public static Unit db(String name) {
        return new Unit(name, "B", "d");
    }

    public static Unit hz() {
        return hz;
    }

    public static Unit sec() {
        return sec;
    }

    public static Unit arb(String name) {
        return new Unit(name, "arb.");
    }

    public static Unit v() {
        return v;
    }

    private String name;
    private String unit;
    private String prefix;
    private int exponent;

    public Unit(String name, String unit, String prefix) {
        this.name = name;
        this.unit = unit;
        this.prefix = prefix;
        this.exponent = prefixToExponent(prefix);
    }

    public Unit(String name, String unit) {
        this(name, unit, "");
    }

    public Unit(String name) {
        this(name, "arb.", "");
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
