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
import nitnc.kotanilab.trainer.fx.setting.Saver;
import nitnc.kotanilab.trainer.fx.util.PositiveIntField;
import nitnc.kotanilab.trainer.fx.setting.UserSetting;
import nitnc.kotanilab.trainer.gl.util.PeriodicTask;
import nitnc.kotanilab.trainer.gpg3100.wrapper.GPG3100;
import nitnc.kotanilab.trainer.math.FunctionGenerator;
import nitnc.kotanilab.trainer.math.VirtualADC;
import nitnc.kotanilab.trainer.math.WaveBuffer;
import nitnc.kotanilab.trainer.util.Dbg;
import nitnc.kotanilab.trainer.util.Utl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MasterController {
    private Pane root = new VBox();
    private HBox first = new HBox(0);
    private HBox second = new HBox(0);
    private TextField samplingFreq = new TextField("100");
    private Button startBtn = new Button("Start");
    private Button stopBtn = new Button("Stop");
    private Button setBtn = new Button("Set");
    private Button deleteBtn = new Button("Delete");
    private Button upBtn = new Button("↑");
    private Button downBtn = new Button("↓");
    private ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(
            "MMG", "MMG(mic.)", "EMG", "HR"
    ));
    private TableView<Controller> tableView = new TableView<>();
    private PeriodicTask analysisTask = new PeriodicTask(10);
    private ADConverter adc;
    private SamplingSetting samplingSetting;
    private MasterSetting setting;
    private List<WaveBuffer> buffers;
    private UserSetting userSetting;

    public MasterController() {
        setting = (MasterSetting) Saver.load("MasterSetting");
        if (setting != null) {
            samplingFreq.setText(String.valueOf(setting.getSamplingFrequency()));
        } else {
            setting = new MasterSetting();
        }
        adc = Utl.doByOS(
                () -> new VirtualADC(5, -5,
                        FunctionGenerator.csv("source.csv"),
                        FunctionGenerator.white(0 , 2, 2.0, 2.5),
                        FunctionGenerator.squa(60, 1.0, 0.0)
                        //FunctionGenerator.sin(2.0, 2.5, 10, 50, 100, 300)
                ),
                () -> new GPG3100(1)
        );
        samplingSetting = adc.getSamplingSetting();

        Label labelSF = new Label("Sampling Frequency");
        samplingFreq.setStyle("-fx-max-width: 50px");
        Label labelCH = new Label("Channel");
        setColumns();

        stopBtn.setDisable(true);
        deleteBtn.setOnMouseClicked(event -> {
            tableView.getSelectionModel().getSelectedCells().forEach(tablePosition -> {
                tableView.getItems().remove(tablePosition.getRow());
            });
        });
        upBtn.setOnMouseClicked(event -> {
            upElement(tableView.getItems(), tableView.getSelectionModel().getSelectedItem());
        });
        downBtn.setOnMouseClicked(event -> {
            downElement(tableView.getItems(), tableView.getSelectionModel().getSelectedItem());
        });
        first.getChildren().addAll(labelSF, samplingFreq, startBtn, stopBtn);
        second.getChildren().addAll(labelCH, comboBox, setBtn, deleteBtn, upBtn, downBtn);
        root.getChildren().addAll(first, second, tableView);
        analysisTask.setCallback(count -> {
            analyze();
        });
    }

    @SuppressWarnings("unchecked")
    private void setColumns() {
        TableColumn<Controller, Label> nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(80);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Controller, ? extends TextField> chCol = new TableColumn<>("Channel");
        chCol.setMinWidth(60);
        chCol.setCellValueFactory(new PropertyValueFactory<>("channelField"));

        TableColumn<Controller, ? extends Pane> ctrlCol = new TableColumn<>("Operator");
        ctrlCol.setMinWidth(400);
        ctrlCol.setCellValueFactory(new PropertyValueFactory<>("operator"));

        TableColumn<Controller, ? extends Pane> indicatorCol = new TableColumn<>("Indicator");
        indicatorCol.setMinWidth(180);
        indicatorCol.setCellValueFactory(new PropertyValueFactory<>("indicator"));

        tableView.getColumns().addAll(nameCol, chCol, ctrlCol, indicatorCol);
    }

    private static void upElement(List<Controller> list, Controller target) {
        shiftElement(list, target, 0, -1);
    }

    private static void downElement(List<Controller> list, Controller target) {
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

    public void setSetEvent(Pane parent, nitnc.kotanilab.trainer.gl.pane.Pane masterPane, Consumer<Integer> action) {
        setBtn.setOnMouseClicked(event -> {
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
                    tableView.getItems().add(controller);
                }
                action.accept(getChannelNumber());
            }
        });
    }

    public void setSecondButton(Consumer<? super Button> action) {
        test(second.getChildren().stream(), Button.class).forEach(action);
    }

    @SuppressWarnings("unchecked")
    public <T> Stream<T> test(Stream<?> stream, Class<T> clazz) {
        return stream.filter(o -> clazz.equals(o.getClass())).map(o -> (T) o);
    }

    public void setStartEvent(BiConsumer<Integer, Double> action) {
        startBtn.setOnMouseClicked(event -> {
            List<Integer> channelList = getChannelList();
            double fs = getFs();
            if (channelList.size() == 0) return;

            samplingSetting.setAll(channelList, fs, fs);
            adc.setSamplingSetting(samplingSetting);
            buffers = adc.convertEternally();
            for (int i = 0; i < buffers.size(); i++) {
                Controller controller = tableView.getItems().get(i);
                WaveBuffer waveBuffer = buffers.get(i);
                controller.getAnalyzer().setSource(waveBuffer);
            }
            buffers.forEach(WaveBuffer::start);

            startBtn.setDisable(true);
            setSecondButton(button -> button.setDisable(true));
            setBtn.setDisable(true);
            action.accept(channelList.size(), fs);
            getControllers().forEach(controller -> controller.start(fs));
            stopBtn.setDisable(false);
            analysisTask.start();
        });
    }

    public void setStopEvent(Consumer<Object> action) {
        // eventはnullの可能性もある
        stopBtn.setOnMouseClicked(event -> {
            if (getChannelNumber() == 0) return;
            analysisTask.stop();
            adc.stop();
            buffers.forEach(WaveBuffer::stop);
            stopBtn.setDisable(true);
            action.accept(event);
            getControllers().forEach(Controller::stop);
            startBtn.setDisable(false);
            setSecondButton(button -> button.setDisable(false));

            setting.setSamplingFrequency(samplingSetting.getSamplingFrequency());
            Saver.save("MasterSetting", setting);
        });
    }

    public void stop() {
        stopBtn.getOnMouseClicked().handle(null);
    }

    public void analyze() {
        getControllers().parallelStream().forEach(controller -> controller.getAnalyzer().execute());
    }

    public List<Integer> getChannelList() {
        return tableView.getItems().stream().map(Controller::getChannel).collect(Collectors.toList());
    }

    public int getChannelNumber() {
        return tableView.getItems().size();
    }

    public double getFs() {
        return Double.parseDouble(samplingFreq.getText());
    }

    public List<Controller> getControllers() {
        return new ArrayList<>(tableView.getItems());
    }

    public Pane getRoot() {
        return root;
    }

    public void setUserSetting(UserSetting userSetting) {
        this.userSetting = userSetting;
    }

    public class Channel {
        private PositiveIntField channel;
        private Controller controller;

        public Channel(int channel, Controller controller) {
            this.channel = new PositiveIntField(channel);
            this.controller = controller;
        }

        public PositiveIntField getChannel() {
            return channel;
        }

        public int getChNum() {
            return channel.getValueAsInt();
        }

        public Controller getController() {
            return controller;
        }

        public void setChannel(PositiveIntField channel) {
            this.channel = channel;
        }

        public void setController(Controller controller) {
            this.controller = controller;
        }
    }
}
