package nitnc.kotanilab.trainer.math.analysis;

import nitnc.kotanilab.trainer.math.point.GraphContext;
import nitnc.kotanilab.trainer.gl.chart.*;
import nitnc.kotanilab.trainer.gl.chart.plot.LinePlot;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.gl.pane.StackPane;
import nitnc.kotanilab.trainer.math.WaveBuffer;
import nitnc.kotanilab.trainer.math.Unit;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * データ処理とグラフ表示の制御を行うスーパークラスです。
 * グラフ作成のテンプレートを持ちます。
 */
public abstract class Analyzer {
    /**
     * グラフの親ペイン
     */
    protected Pane masterPane;
    /**
     * 解析する信号の発生源になるWaveBuffer
     */
    protected WaveBuffer source;
    /**
     * グラフのラッパのPane枠線のスタイルシート
     */
    protected String borderStyle;
    /**
     * このAnalyzerの名前
     */
    protected String title;
    /**
     * GraphContextのMap
     * 実装先でこのフィールドに表示したいグラフのGraphContextを追加してください。
     * 描画順は追加順です。
     */
    protected Map<String, GraphContext> graphContextMap = new LinkedHashMap<>();

    /**
     * コンストラクタです。
     *
     * @param masterPane グラフの親ペイン
     * @param title      このAnalyzerの名前
     */
    protected Analyzer(Pane masterPane, String title) {
        this.masterPane = masterPane;
        this.title = title;
        StringBuilder colorCode = new StringBuilder("#");
        for (int i = 0; i < 6; i++) {
            String tmp = Integer.toHexString((int) Math.round(Math.random() * 0xF)).toUpperCase();
            colorCode.append(tmp);
        }
        borderStyle = "border:solid 3px " + colorCode + ";";
    }

    /**
     * 解析の開始を可能にします。
     * load()を呼び出し始める前にこのメソッドを呼び出してください。
     * このメソッドはexecute()を呼び出す前に必要な処理を実装してくだい。
     *
     * @param fs サンプリング周波数
     * @param n  サンプリング数
     */
    public abstract void start(double fs, int n);

    /**
     * 解析を停止します。
     * また新たにstart()する前に呼び出してください。
     * 必要な処理がある場合オーバーライドしてください。
     */
    public void stop() {
        masterPane.getChildren().removeAll(getGraphWrappers());
    }

    /**
     * 個々のグラフの表示・非表示を設定します。
     *
     * @param visible 個々のグラフの表示・非表示設定を含むMap
     *                キーは実装先のgraphContextMap依存し、このMapが実装先に無いキーを含むと例外をスローします。
     *                trueなら表示しfalseなら非表示になります。
     * @return このAnalyzer
     */
    public Analyzer setVisible(Map<String, Boolean> visible) {
        visible.keySet().forEach(key -> graphContextMap.get(key).setVisible(visible.get(key)));
        return this;
    }

    /**
     * 解析する信号の発生源になるWaveBufferをセットします。
     * このAnalyzerのstart()を呼び出す前にWaveBufferのstart()を呼び出しておいてください。
     *
     * @param source 解析する信号の発生源になるWaveBuffer
     */
    public void setSource(WaveBuffer source) {
        this.source = source;
    }

    /**
     * グラフのラッパになるPaneを作成するユーティリティメソッドです。
     * @param size Paneの横方向のサイズ倍率
     *             標準は25%でその整数倍が指定できます。
     * @return 作成したグラフのラッパになるPane
     */
    protected Pane createWrapperPane(int size) {
        int width = 25 * size;
        Pane ret = new StackPane("height:100%;margin-x:0;margin-y:0;");
        ret.getStyle().put("width", width + "%");
        ret.getStyle().put(borderStyle);
        return ret;
    }

    /**
     * 新しいGraphContextを指定したChartで作成し、graphContextMapに指定したキーで追加するユーティリティメソッドです。
     * wrapperPaneの幅は1マスで初期非表示です。
     *
     * @param key   Mapのキー
     * @param chart GraphContextのChart
     */
    public void putNewGraphContext(String key, Chart chart) {
        graphContextMap.put(key, new GraphContext(chart, createWrapperPane(1), false));
    }

    /**
     * 新しいGraphContextをgraphContextMapに追加するユーティリティメソッドです。
     * キーは指定したChartのtitleで、wrapperPaneの幅は1マスで初期非表示です。
     *
     * @param chart GraphContextのChart
     */
    public void putNewGraphContext(Chart chart) {
        graphContextMap.put(chart.getTitle(), new GraphContext(chart, createWrapperPane(1), false));
    }

    /**
     * graphContextMapに含まれるGraphContextのラッパPaneを全て持ったCollectionを返すユーティリティメソッドです。
     * @return graphContextMapに含まれるGraphContextのラッパPaneを全て持った
     */
    public Collection<Pane> getGraphWrappers() {
        return graphContextMap.values().stream().map(GraphContext::getWrapper).collect(Collectors.toList());
    }

    /**
     * graphContextMapに含まれるGraphContextのChartを全て持ったCollectionを返すユーティリティメソッドです。
     * @return graphContextMapに含まれるGraphContextのChartを全て持ったCollection
     */
    public Collection<Chart> getCharts() {
        return graphContextMap.values().stream().map(GraphContext::getChart).collect(Collectors.toList());
    }

    /**
     * 現在の秒数をミリ秒単位で返します。
     * @return 現在の秒数[ms]
     */
    protected static double getTime() {
        return System.currentTimeMillis() / 1000.0;
    }

    /**
     * グラフの更新を一時停止するかどうかを設定します。
     * @param pause trueなら一時停止、falseなら再生
     */
    public void setPause(boolean pause) {
        graphContextMap.values().forEach(graphContext -> graphContext.setPause(pause));
    }

    private static Color[] lineColors = {
            Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW.darker(), Color.MAGENTA.darker(), Color.CYAN.darker()
    };

    private static Color getLineColor(int i) {
        return lineColors[i % lineColors.length];
    }

    private static void addLinePlotsToChart(Chart chart, String... lines) {
        int i = 0;
        for (String lineName : lines) {
            chart.getPlots().add(new LinePlot(lineName, getLineColor(i++), 1.0));
        }
    }

    protected static Chart createChart(String name, Axis xAxis, Axis yAxis) {
        Chart ret;
        ret = new Chart(name, xAxis, yAxis);
        return ret;
    }

    protected static Chart createWaveChart(String name, double xMax, Unit yUnit, double yMin, double yMax) {
        Axis xAxis = new Axis("Time[sec]", 0.0, xMax, xMax / 10.0);
        Axis yAxis = new Axis(yUnit.toString(), yMin, yMax, (yMax - yMin) / 10.0);
        return new Chart(name, xAxis, yAxis);
    }

    protected static Chart createWaveChart(String name, double xMax, Unit yUnit, double yRange) {
        Axis xAxis = new Axis("Time[sec]", 0.0, xMax, xMax / 10.0);
        return new Chart(name, xAxis, new Axis(yUnit.toString(), -yRange, yRange, yRange / 5.0));
    }

    protected static Chart createTimeSeriesChart(String name, double xMax, Axis yAxis) {
        Axis xAxis = new Axis("Time[sec]", 0.0, xMax, xMax / 10.0);
        return new Chart(name, xAxis, yAxis);
    }

    protected static Chart createSpectrumChart(String name, double xMin, double xMax, Axis yAxis) {
        Axis xAxis = new LogAxis("Frequency[Hz]", xMin, xMax);
        return new Chart(name, xAxis, yAxis);
    }
}
