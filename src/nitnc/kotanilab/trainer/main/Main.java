package nitnc.kotanilab.trainer.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nitnc.kotanilab.trainer.adConverter.ADConverter;
import nitnc.kotanilab.trainer.fx.controller.MasterController;
import nitnc.kotanilab.trainer.fx.stage.UserSettingStage;
import nitnc.kotanilab.trainer.gl.node.Window;
import nitnc.kotanilab.trainer.gl.pane.HPane;
import nitnc.kotanilab.trainer.gl.pane.Pane;
import nitnc.kotanilab.trainer.gpg3100.wrapper.GPG3100;
import nitnc.kotanilab.trainer.adConverter.virtual.FunctionGenerator;
import nitnc.kotanilab.trainer.adConverter.virtual.VirtualADC;
import nitnc.kotanilab.trainer.util.Utl;

/**
 * メインクラス
 * JavaFXに対応しています。
 */
public class Main extends Application {

    private Window window;
    private Pane masterPane;
    private VBox root;
    private MasterController masterController;
    private Stage primaryStage;

    public static void main(final String... args) {
        launch();
    }

    /**
     * OpenGLの初期化
     */
    public void glInit() {
        System.out.println("初期化開始");

        System.out.println("ウィンドウセットアップ");
        window = new Window("Chart", 1600, 300);
        masterPane = new HPane("size:100% 100%;margin:0 0;");
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
        ADConverter adc = Utl.doByOS(
                () -> new VirtualADC(5, -5,
                        FunctionGenerator.csv("./csv/source.csv"),
                        FunctionGenerator.sin(1.0, 2.5, 10, 50, 100, 300),
                        //FunctionGenerator.rangeSin(0 , 2, 2.0, 2.5),
                        //FunctionGenerator.randSin(10.0, 1.0, 0.0)
                        FunctionGenerator.sin(3, 2.0, 0.0)
                        //FunctionGenerator.sin(0.5, 0.0, 1, 5)
                ),
                () -> new GPG3100(1)
        );
        masterController = new MasterController(masterPane,adc);
        root.getChildren().addAll(masterController.getRoot());
        Scene scene = new Scene(root, 720, 320);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Panel");

    }

    @Override
    public void stop() throws Exception {
        masterController.stop();
        window.dispose(null);
    }
}
