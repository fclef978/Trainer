package nitnc.kotanilab.trainer.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nitnc.kotanilab.trainer.fx.controller.MasterController;
import nitnc.kotanilab.trainer.fx.stage.UserSettingStage;
import nitnc.kotanilab.trainer.gl.node.Window;
import nitnc.kotanilab.trainer.gl.pane.HEnumPane;
import nitnc.kotanilab.trainer.gl.pane.Pane;

/**
 * メイン
 */
public class Main extends Application {

    private int ch = 1;
    private int n = 256;
    private double fs = 100;

    private Window window;
    private Pane masterPane;
    private VBox root;
    private MasterController masterController;
    private Stage primaryStage;


    public static void main(final String... args) {
        launch();
    }

    public void glInit() {
        System.out.println("初期化開始");

        System.out.println("ウィンドウセットアップ");
        window = new Window("Chart", 1600, 500);
        masterPane = new HEnumPane("size:100% 100%;margin:0 0;");
        window.getChildren().add(masterPane);


        window.launch();
        System.out.println("ウィンドウスタート");

        new UserSettingStage(userData -> {
            masterController.setUserSetting(userData);
            primaryStage.show();
        });
    }

    @Override
    public void start(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        glInit();

        root = new VBox();

        masterController = new MasterController();
        root.getChildren().addAll(masterController.getRoot());
        masterController.setSetEvent(root, masterPane, ch -> {
            this.ch = ch;
        });

        masterController.setStartEvent((ch, fs) -> {
        });

        masterController.setStopEvent((o) -> {
        });
        Scene scene = new Scene(root, 720, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chart");

    }

    @Override
    public void stop() throws Exception {
        masterController.stop();
        window.dispose(null);
    }
}
