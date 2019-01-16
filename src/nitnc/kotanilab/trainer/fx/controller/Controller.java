package nitnc.kotanilab.trainer.fx.controller;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import nitnc.kotanilab.trainer.fx.setting.UserSetting;
import nitnc.kotanilab.trainer.fx.util.PositiveIntField;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.math.analysis.Analyzer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.IntFunction;

public abstract class Controller<T extends Analyzer> {
    protected T analyzer;
    protected UserSetting userSetting;
    protected Map<String, CheckBox> visible = new LinkedHashMap<>();

    protected Label name;
    protected PositiveIntField channel;
    protected javafx.scene.layout.Pane operator;
    protected javafx.scene.layout.Pane indicator;

    public Controller(String name , T analyzer, UserSetting userSetting) {
        this.name = new Label(name);
        this.channel = new PositiveIntField(0);
        operator = new HBox(2);
        indicator = new HBox(2);
        this.analyzer = analyzer;
        this.userSetting = userSetting;
    }

    public void setVisible(String... keys) {
        for (String key: keys) {
            visible.put(key, new CheckBox(key));
        }
        operator.getChildren().addAll(visible.values());
    }

    public void start(double fs) {
        boolean[] tmp = new boolean[this.visible.size()];
        int[] i = {0};
        this.visible.values().stream().map(CheckBox::isSelected).forEach(bool -> tmp[i[0]++] = bool);
        analyzer.setVisible(tmp);
    }

    public abstract void stop();

    public T getAnalyzer() {
        return analyzer;
    }

    public int getChannel() {
        return channel.getValueAsInt();
    }

    public Label getName() {
        return name;
    }

    public void setName(Label name) {
        this.name = name;
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
