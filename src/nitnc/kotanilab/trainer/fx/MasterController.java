package nitnc.kotanilab.trainer.fx;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nitnc.kotanilab.trainer.adConverter.ADConverter;
import nitnc.kotanilab.trainer.adConverter.SamplingSetting;
import nitnc.kotanilab.trainer.gl.util.PeriodicTask;
import nitnc.kotanilab.trainer.gpg3100.wrapper.GPG3100;
import nitnc.kotanilab.trainer.math.FunctionGenerator;
import nitnc.kotanilab.trainer.math.VirtualADC;
import nitnc.kotanilab.trainer.math.WaveBuffer;
import nitnc.kotanilab.trainer.math.analysis.HrAnalyzer;
import nitnc.kotanilab.trainer.math.analysis.MmgAnalyzer;
import nitnc.kotanilab.trainer.util.Utl;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MasterController {
    private Pane root = new VBox();
    private HBox first = new HBox(0);
    private HBox second = new HBox(0);
    private TextField smplFreq = new TextField("100");
    private Button startBtn = new Button("Start");
    private Button stopBtn = new Button("Stop");
    private Button setBtn = new Button("Set");
    private Button deleteBtn = new Button("Delete");
    private Button upBtn = new Button("↑");
    private Button downBtn = new Button("↓");
    private ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(
            "MMG", "HR"
    ));
    private TableView<Channel> tableView = new TableView<>();
    private PeriodicTask analysisTask = new PeriodicTask(10);
    private ADConverter adc;
    private SamplingSetting setting;
    private List<WaveBuffer> buffers;
    private UserData userData;

    public MasterController() {
        adc = Utl.doByOS(
                () -> new VirtualADC(5, -5,
                        FunctionGenerator.csv("source.csv"),
                        FunctionGenerator.cosSin(5, 2, 2.5)
                ),
                () -> new GPG3100(1));
        setting = adc.getSamplingSetting();

        Label labelSF = new Label("Sampling Frequency");
        smplFreq.setStyle("-fx-max-width: 50px");
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
        first.getChildren().addAll(labelSF, smplFreq, startBtn, stopBtn);
        second.getChildren().addAll(labelCH, comboBox, setBtn, deleteBtn, upBtn, downBtn);
        root.getChildren().addAll(first, second, tableView);
        analysisTask.setCallback(count -> {
            analyze();
        });
    }

    @SuppressWarnings("unchecked")
    private void setColumns() {
        TableColumn<Channel, Text> chCol = new TableColumn<>("Channel");
        chCol.setMinWidth(100);
        chCol.setCellValueFactory(new PropertyValueFactory<>("channel"));
        TableColumn<Channel, Controller> ctrlCol = new TableColumn<>("Controller");
        ctrlCol.setMinWidth(540);
        ctrlCol.setCellValueFactory(new PropertyValueFactory<>("controller"));
        tableView.getColumns().addAll(chCol, ctrlCol);
    }

    private static void upElement(List<Channel> list, Channel target) {
        shiftElement(list, target, 0, -1);
    }

    private static void downElement(List<Channel> list, Channel target) {
        shiftElement(list, target, -1, 1);
    }

    private static void shiftElement(List<Channel> list, Channel target, int limit, int quantity) {
        int pos = list.indexOf(target);
        if (pos == -1 || pos == (limit < 0 ? list.size() + limit : limit)) return;
        Channel tmp1 = list.get(pos);
        Channel tmp2 = list.get(pos + quantity);
        Controller tmp = tmp1.controller;
        tmp1.setController(tmp2.controller);
        tmp2.setController(tmp);
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
                        MmgAnalyzer mmgAnalyzer = new MmgAnalyzer(masterPane);
                        controller = new MmgController(mmgAnalyzer);
                        break;
                    case "HR":
                        HrAnalyzer hrAnalyzer = new HrAnalyzer(masterPane);
                        hrAnalyzer.setHRGideLine(userData.getAge());
                        controller = new HrController(hrAnalyzer);
                        break;
                    case "":
                        break;
                    default:
                        throw new IllegalArgumentException("不明な選択です");
                }
                if (controller != null) {
                    Channel channel = new Channel(getCh() + 1, controller);
                    tableView.getItems().add(channel);
                }
                action.accept(getCh());
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
            int ch = getCh();
            double fs = getFs();
            if (getCh() == 0) return;

            setting.setAll(ch, fs, fs);
            adc.setSamplingSetting(setting);
            buffers = adc.convertEternally();
            tableView.getItems().forEach(channel -> {
                int chNum = channel.getChNum() - 1;
                WaveBuffer waveBuffer = buffers.get(chNum);
                channel.getController().getAnalyzer().setSource(waveBuffer);
                // getControllers().get(chNum).getAnalyzer().setSource(waveBuffer);
            });
            buffers.forEach(WaveBuffer::start);

            startBtn.setDisable(true);
            setSecondButton(button -> button.setDisable(true));
            setBtn.setDisable(true);
            action.accept(getCh(), fs);
            getControllers().forEach(controller -> controller.start(fs));
            stopBtn.setDisable(false);
            analysisTask.start();
        });
    }

    public void setStopEvent(Consumer<Object> action) {
        // eventはnullの可能性もある
        stopBtn.setOnMouseClicked(event -> {
            if (getCh() == 0) return;
            analysisTask.stop();
            adc.stop();
            buffers.forEach(WaveBuffer::stop);
            stopBtn.setDisable(true);
            action.accept(event);
            getControllers().forEach(Controller::stop);
            startBtn.setDisable(false);
            setSecondButton(button -> button.setDisable(false));
        });
    }

    public void stop() {
        stopBtn.getOnMouseClicked().handle(null);
    }

    public void analyze() {
        getControllers().parallelStream().forEach(controller -> controller.getAnalyzer().execute());
    }

    public int getCh() {
        return tableView.getItems().stream().mapToInt(Channel::getChNum).max().orElse(0);
    }

    public double getFs() {
        return Double.parseDouble(smplFreq.getText());
    }

    public List<Controller> getControllers() {
        return tableView.getItems().stream().map(Channel::getController).collect(Collectors.toList());
    }

    public Pane getRoot() {
        return root;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
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
