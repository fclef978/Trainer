package nitnc.kotanilab.trainer.fx;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class DoubleField extends LimitedField {
    public DoubleField() {
        this("0.0");
    }

    public DoubleField(double number) {
        this(String.valueOf(number));
    }

    public DoubleField(String text) {
        super(Pattern.compile("[^0-9.\\-]+"), text);
    }

    public double getValueAsDouble() {
        return Double.parseDouble(getText());
    }

    public void setValueAsDouble(double val) {
        setText(String.valueOf(val));
    }

    public void plus(double val) {
        setValueAsDouble(getValueAsDouble() + val);
    }

    public void mul(double val) {
        setValueAsDouble(getValueAsDouble() * val);
    }

    public void calculate(DoubleUnaryOperator operator) {
        setValueAsDouble(operator.applyAsDouble(getValueAsDouble()));
    }
}
