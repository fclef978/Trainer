package nitnc.kotanilab.trainer.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nitnc.kotanilab.trainer.fx.controller.Controller;
import nitnc.kotanilab.trainer.fx.controller.MmgController;
import nitnc.kotanilab.trainer.gl.pane.StackPane;
import nitnc.kotanilab.trainer.math.analysis.MmgAnalyzer;
import nitnc.kotanilab.trainer.util.Dbg;

public class TableTest extends Application {
    public static void main(final String... args) {
        launch();
    }

    private VBox root = new VBox(1);
    private TableView<Channel> tableView = new TableView<>();
    private Button button = new Button("PUSH");

    @Override
    public void start(Stage stage) throws Exception {
        TableColumn<Channel, Text> chCol = new TableColumn<>("Channel");
        chCol.setMinWidth(100);
        chCol.setCellValueFactory(new PropertyValueFactory<>("channel"));
        TableColumn<Channel, Controller> ctrlCol = new TableColumn<>("Controller");
        ctrlCol.setMinWidth(540);
        ctrlCol.setCellValueFactory(new PropertyValueFactory<>("controller"));
        tableView.getColumns().addAll(chCol, ctrlCol);
        button.setOnMouseClicked(event -> Dbg.p(tableView.getSelectionModel().getSelectedCells()));
        tableView.getItems().addAll(new Channel(1, new MmgController(new MmgAnalyzer(new StackPane()))));

        root.getChildren().addAll(tableView, button);
        Scene scene = new Scene(root, 640, 360);
        stage.setScene(scene);
        stage.setTitle("Chart");
        stage.show();
    }


    public class Channel {
        private final Text channel;
        private final Controller controller;

        public Channel(int channel, Controller controller) {
            this.channel = new Text(String.valueOf(channel));
            this.controller = controller;
        }

        public Text getChannel() {
            return channel;
        }

        public Controller getController() {
            return controller;
        }
    }
}
