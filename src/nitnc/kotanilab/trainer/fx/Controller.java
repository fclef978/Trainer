package nitnc.kotanilab.trainer.fx;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import nitnc.kotanilab.trainer.math.analysis.Analyzer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.IntFunction;

public abstract class Controller extends HBox {
    protected Analyzer analyzer;
    protected Map<String, CheckBox> visible = new LinkedHashMap<>();

    public Controller(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void setVisible(String... keys) {
        for (String key: keys) {
            visible.put(key, new CheckBox(key));
        }
        getChildren().addAll(visible.values());
    }

    public void start(double fs) {
        boolean[] tmp = new boolean[this.visible.size()];
        int[] i = {0};
        this.visible.values().stream().map(CheckBox::isSelected).forEach(bool -> tmp[i[0]++] = bool);
        analyzer.setVisible(tmp);
    }

    public abstract void stop();

    public Analyzer getAnalyzer() {
        return analyzer;
    }
}
