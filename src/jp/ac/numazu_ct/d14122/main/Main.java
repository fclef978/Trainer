package jp.ac.numazu_ct.d14122.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.ac.numazu_ct.d14122.fx.*;
import jp.ac.numazu_ct.d14122.gl.node.Window;
import jp.ac.numazu_ct.d14122.gl.pane.HEnumPane;
import jp.ac.numazu_ct.d14122.gl.pane.Pane;
import jp.ac.numazu_ct.d14122.util.Dbg;

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
        masterPane = new HEnumPane("width:100%;height:100%;margin:0 0;");
        window.getChildren().add(masterPane);


        window.launch();
        System.out.println("ウィンドウスタート");

        new UserSetting(userData -> {
            masterController.setUserData(userData);
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
        Scene scene = new Scene(root, 640, 360);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chart");

    }

    @Override
    public void stop() throws Exception {
        masterController.stop();
        window.dispose(null);
    }
}
