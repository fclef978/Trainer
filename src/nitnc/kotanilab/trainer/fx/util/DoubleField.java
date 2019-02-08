package nitnc.kotanilab.trainer.fx.util;

import java.util.function.DoubleUnaryOperator;
import java.util.regex.Pattern;

/**
 * 実数のみを入力可能なTextFieldです。
 */
public class DoubleField extends LimitedField {
    /**
     * 0を初期表示するコンストラクタです。
     */
    public DoubleField() {
        this(0.0);
    }

    /**
     * 指定した実数を初期表示するコンストラクタです。
     *
     * @param number 初期表示する実数
     */
    public DoubleField(double number) {
        super(Pattern.compile("[^0-9.\\-]+"), String.valueOf(number));
    }

    /**
     * 入力されている値を返します。
     *
     * @return フィールドに入力されている数
     */
    public double getValue() {
        return Double.parseDouble(getText());
    }

    /**
     * 指定した実数を入力します。
     * 負数を指定した場合絶対値が表示されます。
     *
     * @param val フィールドに入力する実数
     */
    public void setValue(double val) {
        setText(String.valueOf(val));
    }
}
