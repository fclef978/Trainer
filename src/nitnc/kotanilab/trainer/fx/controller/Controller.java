package nitnc.kotanilab.trainer.fx.controller;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import nitnc.kotanilab.trainer.fx.setting.UserSetting;
import nitnc.kotanilab.trainer.fx.util.PositiveIntField;
import nitnc.kotanilab.trainer.math.analysis.Analyzer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 解析の制御を行うコントローラです。
 * MasterControllerの各列に対応したフィールドを持ち、解析とOpenGL描画を行うAnalyzerクラスをフィールドに持ちます。
 *
 * @param <T> そのコントローラが使うアナライザ
 */
public abstract class Controller<T extends Analyzer> {
    /**
     * コントローラの名前
     */
    protected final String name;
    /**
     * アナライザ
     */
    protected T analyzer;
    /**
     * ユーザ設定
     */
    protected UserSetting userSetting;
    /**
     * グラフの可視化設定マップ
     * グラフ名をキーにして、追加順に並ぶ。
     */
    protected Map<String, CheckBox> visible = new LinkedHashMap<>();

    /**
     * メイン画面上に表示されるコントローラの名前
     */
    protected Label nameLabel;
    /**
     * メイン画面上に表示されるチャンネル番号のフィールド
     */
    protected PositiveIntField channel;
    /**
     * 操作パネルのペイン
     */
    protected javafx.scene.layout.Pane operator;
    /**
     * インジケータのペイン
     */
    protected javafx.scene.layout.Pane indicator;

    /**
     * コンストラクタです。
     *
     * @param name        コントローラの名前
     * @param analyzer    使用するアナライザ
     * @param userSetting ユーザ設定
     */
    public Controller(String name, T analyzer, UserSetting userSetting) {
        this.name = name;
        this.nameLabel = new Label(name);
        this.channel = new PositiveIntField(0);
        operator = new HBox(2);
        indicator = new HBox(2);
        this.analyzer = analyzer;
        this.userSetting = userSetting;
    }

    /**
     * 指定した全てのキーで可視化選択チェックボックスを作成します。
     *
     * @param keys キー
     */
    public void addAllVisible(String... keys) {
        for (String key : keys) {
            visible.put(key, new CheckBox(key));
        }
        operator.getChildren().addAll(visible.values());
    }

    /**
     * 解析を開始します。
     *
     * @param fs サンプリング周波数
     */
    public void start(double fs) {
        Map<String, Boolean> tmp = visible.keySet().stream().collect(Collectors.toMap(key -> key, key -> visible.get(key).isSelected()));
        analyzer.setVisible(tmp);
    }

    /**
     * 解析を終了します。
     */
    public abstract void stop();

    /**
     * 表示中のグラフのスクリーンショットを保存します。
     */
    public void saveAsImage() {
        getAnalyzer().getCharts().forEach(chart -> chart.shot("images/" + userSetting.getName() + "_" + name));
    }


    public T getAnalyzer() {
        return analyzer;
    }

    /**
     * チャンネル番号を返します。
     *
     * @return チャンネル番号
     */
    public int getChannel() {
        return channel.getValue();
    }

    public Label getNameLabel() {
        return nameLabel;
    }

    public void setNameLabel(Label nameLabel) {
        this.nameLabel = nameLabel;
    }

    public PositiveIntField getChannelField() {
        return channel;
    }

    public void setChannel(PositiveIntField channel) {
        this.channel = channel;
    }

    public javafx.scene.layout.Pane getOperator() {
        return operator;
    }

    public void setOperator(javafx.scene.layout.Pane operator) {
        this.operator = operator;
    }

    public javafx.scene.layout.Pane getIndicator() {
        return indicator;
    }

    public void setIndicator(javafx.scene.layout.Pane indicator) {
        this.indicator = indicator;
    }
}
