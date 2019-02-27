package nitnc.kotanilab.trainer.gl.style;

import nitnc.kotanilab.trainer.gl.node.Node;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * スタイルを表すクラスです。
 * 各プロパティをまとめてMapで管理しています。
 * TODO 全てのプロパティをフィールドで保持してリフレクションなどを使って読み書きすべき
 */
public class Style {

    private Map<String, Property> map = new HashMap<>();
    private Node node;

    /**
     * コンストラクタです。
     *
     * @param node このプロパティが所属するノード
     */
    public Style(Node node) {
        this.node = node;
        initMap();
    }

    private void initMap() {
        List<Property> list = Arrays.asList(
                new Margin("0 0", node),
                new Size("100% 100%", node),
                new Align("center center", node),
                new Border("none", node),
                new Color("#000000", node)
        );
        list.forEach(property -> property.putsToMap(map));
    }

    /**
     * 指定したキーで指定した値を登録します。
     *
     * @param key キー
     * @param val 登録する値
     */
    public void put(String key, String val) {
        key = key.trim();
        val = val.trim();
        map.get(key).setValue(val); // ぬるぽ
    }

    /**
     * スタイルシートを用いて登録します。
     * 文法はCSSに似ていて、
     * プロパティ:値;プロパティ:値;...
     * となります。
     *
     * @param statements スタイルシート
     */
    public void put(String statements) {
        for (String statement : statements.trim().split(";")) {
            String[] set = statement.trim().split(":");
            this.put(set[0], set[1]);
        }
    }

    /**
     * 指定したキーで登録されている値を返します。
     *
     * @param key キー
     * @return 登録されている値
     */
    public Property get(String key) {
        Property ret = map.get(key);
        if (ret.getValue().isEmpty() && ret.isExtendable()) {
            ret = node.getParent().getStyle().get(key);
        }
        return ret;
    }

}
