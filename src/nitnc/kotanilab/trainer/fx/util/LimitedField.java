package nitnc.kotanilab.trainer.fx.util;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.regex.Pattern;

/**
 * 入力できる文字を制限できるTextFieldのスーパークラスとなる抽象クラスです。
 */
public abstract class LimitedField extends TextField {
    /**
     * コンストラクタです。
     *
     * @param notMatchPattern 入力を許可しない文字のPattern
     * @param initStr         初期表示文字列
     */
    public LimitedField(Pattern notMatchPattern, String initStr) {
        super(initStr);
        TextFormatter<String> lowerFormatter = new TextFormatter<>(change -> {
            String newStr = notMatchPattern.matcher(change.getText()).replaceAll("");
            int diffCount = change.getText().length() - newStr.length();
            change.setAnchor(change.getAnchor() - diffCount);
            change.setCaretPosition(change.getCaretPosition() - diffCount);
            change.setText(newStr);
            return change;
        });
        setTextFormatter(lowerFormatter);
    }
}
