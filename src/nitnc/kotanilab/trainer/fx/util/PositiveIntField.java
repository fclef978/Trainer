package nitnc.kotanilab.trainer.fx.util;

import java.util.regex.Pattern;

/**
 * 正の整数と0のみを入力可能なTextFieldです。
 */
public class PositiveIntField extends LimitedField {
    /**
     * 0を初期表示するコンストラクタです。
     */
    public PositiveIntField() {
        this(0);
    }

    /**
     * 指定した正の整数を初期表示するコンストラクタです。
     * 負数を指定した場合絶対値が表示されます。
     *
     * @param number 初期表示する整数
     */
    public PositiveIntField(int number) {
        super(Pattern.compile("[^0-9]+"), String.valueOf(number));
    }

    /**
     * 入力されている値を返します。
     *
     * @return フィールドに入力されている数
     */
    public int getValue() {
        return Integer.decode(getText());
    }

    /**
     * 指定した正の整数を入力します。
     * 負数を指定した場合絶対値が表示されます。
     *
     * @param val フィールドに入力する正の整数
     */
    public void setValue(int val) {
        setText(String.valueOf(val));
    }
}
