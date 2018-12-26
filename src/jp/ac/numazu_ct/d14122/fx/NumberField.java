package jp.ac.numazu_ct.d14122.fx;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import jp.ac.numazu_ct.d14122.util.Dbg;

import java.util.regex.Pattern;

public class NumberField extends TextField {
    public NumberField() {
        this("");
    }

    public NumberField(String text) {
        super(text);
        Pattern notNumberPattern = Pattern.compile("[^0-9.\\-]+");
        TextFormatter<String> lowerFormatter = new TextFormatter<>(change -> {
            String newStr = notNumberPattern.matcher(change.getText()).replaceAll("");
            int diffcount = change.getText().length() - newStr.length();
            change.setAnchor(change.getAnchor() - diffcount);
            change.setCaretPosition(change.getCaretPosition() - diffcount);
            change.setText(newStr);
            return change;
        });
        setTextFormatter(lowerFormatter);
    }

    public double getValueAsDouble() {
        return Double.parseDouble(getText());
    }

    public int getValueAsInt() {
        return (int) Math.round(getValueAsDouble());
    }

    public long getValueAsLong() {
        return Math.round(getValueAsDouble());
    }
}
