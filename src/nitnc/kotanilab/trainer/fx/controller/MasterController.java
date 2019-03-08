package nitnc.kotanilab.trainer.fx.controller;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import nitnc.kotanilab.trainer.adConverter.ADConverter;
import nitnc.kotanilab.trainer.adConverter.SamplingSetting;
import nitnc.kotanilab.trainer.fx.setting.MasterSetting;
import nitnc.kotanilab.trainer.util.Saver;
import nitnc.kotanilab.trainer.fx.setting.UserSetting;
import nitnc.kotanilab.trainer.math.WaveBuffer;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * メイン画面のコントローラです。
 * Controllerの管理も行います。
 */
public class MasterController {
    private Pane root = new VBox();
    private HBox samplingControls = new HBox(0);
    private HBox analysisControls = new HBox(0);
    private TextField samplingFrequency = new TextField("100");
    private Button startButton = new Button("Start");
    private Button stopButton = new Button("Stop");
    private ToggleButton pauseButton = new ToggleButton("Pause");
    private Button screenShotButton = new Button("SS");
    private Button addAnalysisButton = new Button("Set");
    private Button removeAnalysisButton = new Button("Delete");
    private Button liftAnalysisButton = new Button("↑");
    private Button lowerAnalysisButton = new Button("↓");
    private ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(
            "MMG", "MMG(mic.)", "EMG", "HR"
    ));
    private TableView<Controller> analysisTable = new TableView<>();
    private ADConverter adc;
    private SamplingSetting samplingSetting;
    private MasterSetting setting;
    private List<WaveBuffer> buffers;
    private UserSetting userSetting;
    private nitnc.kotanilab.trainer.gl.pane.Pane masterPane;

    /**
     * コンストラクタです。
     *
     * @param masterPane OpenGLの親ペイン
     * @param adc        ADConverter ADコンバータのオブジェクト
     */
    public MasterController(nitnc.kotanilab.trainer.gl.pane.Pane masterPane, ADConverter adc) {
        this.masterPane = masterPane;
        setting = (MasterSetting) Saver.load("MasterSetting");
        if (setting != null) {
            samplingFrequency.setText(String.valueOf(setting.getSamplingFrequency()));
        } else {
            setting = new MasterSetting();
        }
        this.adc = adc;

        samplingSetting = adc.getSamplingSetting();

        Label labelSF = new Label("Sampling Frequency");
        samplingFrequency.setStyle("-fx-max-width: 50px");
        Label labelCH = new Label("Channel");
        setColumns();

        stopButton.setDisable(true);
        removeAnalysisButton.setOnMouseClicked(event -> {
            analysisTable.getSelectionModel().getSelectedCells().forEach(tablePosition -> {
                analysisTable.getItems().remove(tablePosition.getRow());
            });
        });
        liftAnalysisButton.setOnMouseClicked(event -> {
            liftElement(analysisTable.getItems(), analysisTable.getSelectionModel().getSelectedItem());
        });
        lowerAnalysisButton.setOnMouseClicked(event -> {
            lowerElement(analysisTable.getItems(), analysisTable.getSelectionModel().getSelectedItem());
        });
        pauseButton.setOnMouseClicked(event -> {
            if (!pauseButton.isSelected()) {
                analysisTable.getItems().forEach(controller -> controller.getAnalyzer().setPause(false));
            } else {
                analysisTable.getItems().forEach(controller -> controller.getAnalyzer().setPause(true));
            }
        });
        screenShotButton.setOnMouseClicked(event -> {
            analysisTable.getItems().forEach(Controller::saveAsImage);
        });
        addAnalysisButton.setOnMouseClicked(event -> addAnalysis());
        startButton.setOnMouseClicked(event -> startAnalysis());
        stopButton.setOnMouseClicked(event -> stopAnalysis());
        samplingControls.getChildren().addAll(labelSF, samplingFrequency, startButton, stopButton, pauseButton, screenShotButton);
        analysisControls.getChildren().addAll(labelCH, comboBox, addAnalysisButton, removeAnalysisButton, liftAnalysisButton, lowerAnalysisButton);
        root.getChildren().addAll(samplingControls, analysisControls, analysisTable);
    }

    @SuppressWarnings("unchecked")
    private void setColumns() {
        TableColumn<Controller, Label> nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(80);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nameLabel"));

        TableColumn<Controller, ? extends TextField> chCol = new TableColumn<>("Channel");
        chCol.setMinWidth(60);
        chCol.setCellValueFactory(new PropertyValueFactory<>("channelField"));

        TableColumn<Controller, ? extends Pane> ctrlCol = new TableColumn<>("Operator");
        ctrlCol.setMinWidth(400);
        ctrlCol.setCellValueFactory(new PropertyValueFactory<>("operator"));

        TableColumn<Controller, ? extends Pane> indicatorCol = new TableColumn<>("Indicator");
        indicatorCol.setMinWidth(180);
        indicatorCol.setCellValueFactory(new PropertyValueFactory<>("indicator"));

        analysisTable.getColumns().addAll(nameCol, chCol, ctrlCol, indicatorCol);
    }

    private static void liftElement(List<Controller> list, Controller target) {
        shiftElement(list, target, 0, -1);
    }

    private static void lowerElement(List<Controller> list, Controller target) {
        shiftElement(list, target, -1, 1);
    }

    private static void shiftElement(List<Controller> list, Controller target, int limit, int quantity) {
        int pos = list.indexOf(target);
        if (pos == -1 || pos == (limit < 0 ? list.size() + limit : limit)) return;
        Controller tmp1 = list.get(pos);
        Controller tmp2 = list.get(pos + quantity);
        Controller tmp = tmp1;
        tmp1 = tmp2;
        tmp2 = tmp;
        list.set(pos, tmp1);
        list.set(pos + quantity, tmp2);
    }

    private void addAnalysis() {
        String str = comboBox.getValue();
        Controller controller = null;
        if (str != null) {
            switch (str) {
                case "MMG":
                    controller = new MmgController(masterPane, userSetting);
                    break;
                case "MMG(mic.)":
                    controller = new MmgMicController(masterPane, userSetting);
                    break;
                case "EMG":
                    controller = new EmgController(masterPane, userSetting);
                    break;
                case "HR":
                    controller = new HrController(masterPane, userSetting);
                    break;
                case "":
                    break;
                default:
                    throw new IllegalArgumentException("不明な選択です");
            }
            if (controller != null) {
                analysisTable.getItems().add(controller);
            }
        }
    }

    private void startAnalysis() {
        List<Integer> channelList = getChannelList();
        Collections.sort(channelList);
        double fs = getFs();
        if (channelList.size() == 0) return;
        samplingSetting.setAll(channelList, fs, fs);
        adc.setSamplingSetting(samplingSetting);
        buffers = adc.convertEternally();
        int[] i = {0};
        analysisTable.getItems().stream().sorted(Comparator.comparingInt(Controller::getChannel)).forEach(controller -> {
            WaveBuffer waveBuffer = buffers.get(i[0]++);
            controller.getAnalyzer().setSource(waveBuffer);
        });
        buffers.forEach(WaveBuffer::start);

        startButton.setDisable(true);
        forEachAnalysisControls(button -> button.setDisable(true));
        addAnalysisButton.setDisable(true);
        getControllers().forEach(controller -> controller.start(fs));
        stopButton.setDisable(false);
        pauseButton.setDisable(false);
    }

    public void forEachAnalysisControls(Consumer<? super Button> action) {
        castStream(analysisControls.getChildren().stream(), Button.class).forEach(action);
    }

    @SuppressWarnings("unchecked")
    public <T> Stream<T> castStream(Stream<?> stream, Class<T> clazz) {
        return stream.filter(o -> clazz.equals(o.getClass())).map(o -> (T) o);
    }

    private void stopAnalysis() {
        if (getAnalysisNumber() == 0) return;
        adc.stop();
        buffers.forEach(WaveBuffer::stop);
        stopButton.setDisable(true);
        pauseButton.setDisable(true);
        getControllers().forEach(Controller::stop);
        startButton.setDisable(false);
        forEachAnalysisControls(button -> button.setDisable(false));

        setting.setSamplingFrequency(samplingSetting.getSamplingFrequency());
        Saver.save("MasterSetting", setting);
    }

    /**
     * 解析を停止します。
     */
    public void stop() {
        stopAnalysis();
    }

    /**
     * 使用するチャンネルのリストを返します。
     *
     * @return 使用するチャンネルのリスト
     */
    public List<Integer> getChannelList() {
        return analysisTable.getItems().stream().map(Controller::getChannel).collect(Collectors.toList());
    }

    /**
     * 解析数を返します。
     *
     * @return 解析数
     */
    public int getAnalysisNumber() {
        return analysisTable.getItems().size();
    }

    /**
     * サンプリング周波数を返します。
     *
     * @return サンプリング周波数
     */
    public double getFs() {
        return Double.parseDouble(samplingFrequency.getText());
    }

    /**
     * 現在登録されているコントローラのリストを返します。
     *
     * @return 現在登録されているコントローラのリスト
     */
    public List<Controller> getControllers() {
        return new ArrayList<>(analysisTable.getItems());
    }

    public Pane getRoot() {
        return root;
    }

    public void setUserSetting(UserSetting userSetting) {
        this.userSetting = userSetting;
    }
}
