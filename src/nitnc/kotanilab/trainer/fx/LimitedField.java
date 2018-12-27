package nitnc.kotanilab.trainer.fx;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.regex.Pattern;

public abstract class LimitedField extends TextField {
    protected Pattern notNumberPattern;

    public LimitedField(Pattern notNumberPattern, String initStr) {
        super(initStr);
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
}
