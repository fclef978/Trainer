package nitnc.kotanilab.trainer.fx.stage;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nitnc.kotanilab.trainer.fx.setting.UserSetting;
import nitnc.kotanilab.trainer.fx.util.PositiveIntField;
import nitnc.kotanilab.trainer.fx.setting.Saver;

import java.util.function.Consumer;

/**
 * ユーザ設定ダイアログです。
 * コンストラクタが実行されると自動的に表示されます。
 */
public class UserSettingStage extends Stage {
    private Scene scene;
    private UserSetting userSetting;
    private TextField nameField = new TextField("noname");
    private PositiveIntField ageField = new PositiveIntField(20);
    private VBox root = new VBox(1);
    private Consumer<UserSetting> finishAction;
    private Button enter = new Button("Enter");

    /**
     * コンストラクタです。
     * Enterボタンが押されるとfinishActionが実行されます。
     *
     * @param finishAction 終了時の動作
     */
    public UserSettingStage(Consumer<UserSetting> finishAction) {
        this.finishAction = finishAction;
        userSetting = (UserSetting) Saver.load("UserSetting");
        VBox wrapper = new VBox(1);
        wrapper.getChildren().addAll(createRow("Name", nameField), createRow("Age", ageField));
        ageField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                finish();
            }
        });
        enter.setOnMouseClicked(event -> finish());
        root.getChildren().addAll(wrapper, enter);
        if (userSetting != null) {
            nameField.setText(userSetting.getName());
            ageField.setText(String.valueOf(userSetting.getAge()));
        } else {
            userSetting = new UserSetting();
        }
        scene = new Scene(root, 320, 240);
        setScene(scene);
        setTitle("User Setting");
        show();
    }

    private Pane createRow(String name, Node node) {
        HBox wrapper = new HBox(1);
        wrapper.getChildren().addAll(new Label(name), node);
        return wrapper;
    }

    private void finish() {
        finishAction.accept(new UserSetting(nameField.getText(), ageField.getValue()));
        userSetting.setName(nameField.getText());
        userSetting.setAge(ageField.getValue());
        Saver.save("UserSetting", userSetting);
        this.close();
    }

}
