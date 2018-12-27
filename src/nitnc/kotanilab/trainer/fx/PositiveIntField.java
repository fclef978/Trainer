package nitnc.kotanilab.trainer.fx;

import java.util.function.DoubleUnaryOperator;
import java.util.regex.Pattern;

public class PositiveIntField extends LimitedField {
    public PositiveIntField() {
        this("0");
    }

    public PositiveIntField(int number) {
        this(String.valueOf(number));
    }

    public PositiveIntField(String text) {
        super(Pattern.compile("[^0-9]+"), text);
    }

    public int getValueAsInt() {
        return Integer.decode(getText());
    }

    public void setValueAsInt(int val) {
        setText(String.valueOf(val));
    }

    public void increment() {
        int current = getValueAsInt();
        if (current == Integer.MAX_VALUE || current <= 0) {
            setValueAsInt(Integer.MAX_VALUE);
        } else {
            setValueAsInt(current + 1);
        }
    }

    public void decrement() {
        int current = getValueAsInt();
        if (current <= 0) {
            setValueAsInt(0);
        } else {
            setValueAsInt(current - 1);
        }
    }
}
