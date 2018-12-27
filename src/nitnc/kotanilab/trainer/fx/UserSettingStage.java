package nitnc.kotanilab.trainer.fx;

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

public class UserSettingStage extends Stage {
    private Scene scene;
    private UserSetting userSetting;
    private TextField nameField = new TextField("noname");
    private PositiveIntField ageField = new PositiveIntField("20");
    private VBox root = new VBox(1);
    private Consumer<UserSetting> finishAction;
    private Button enter = new Button("Enter");

    public UserSettingStage(Consumer<UserSetting> finishAction) {
        this.finishAction = finishAction;
        userSetting = (UserSetting) Saver.load("UserSetting");
        VBox wrapper = new VBox(1);
        wrapper.getChildren().addAll(createColumn("Name", nameField), createColumn("Age", ageField));
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

    private Pane createColumn(String name, Node node) {
        HBox wrapper = new HBox(1);
        wrapper.getChildren().addAll(new Label(name), node);
        return wrapper;
    }

    public void finish() {
        finishAction.accept(new UserSetting(nameField.getText(), ageField.getValueAsInt()));
        userSetting.setName(nameField.getText());
        userSetting.setAge(ageField.getValueAsInt());
        Saver.save("UserSetting", userSetting);
        this.close();
    }

}
