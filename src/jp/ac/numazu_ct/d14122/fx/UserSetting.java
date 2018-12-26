package jp.ac.numazu_ct.d14122.fx;

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
import jp.ac.numazu_ct.d14122.util.Saver;

import java.util.function.Consumer;

public class UserSetting extends Stage {
    private Scene scene;
    private UserData userData;
    private TextField nameField = new TextField("noname");
    private NumberField ageField = new NumberField("20");
    private VBox root = new VBox(1);
    private Consumer<UserData> finishAction;
    private Button enter = new Button("Enter");

    public UserSetting(Consumer<UserData> finishAction) {
        this.finishAction = finishAction;
        userData = (UserData) Saver.load("UserData");
        VBox wrapper = new VBox(1);
        wrapper.getChildren().addAll(createColumn("Name", nameField), createColumn("Age", ageField));
        ageField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                finish();
            }
        });
        enter.setOnMouseClicked(event -> finish());
        root.getChildren().addAll(wrapper, enter);
        if (userData != null) {
            nameField.setText(userData.getName());
            ageField.setText(String.valueOf(userData.getAge()));
        } else {
            userData = new UserData();
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
        finishAction.accept(new UserData(nameField.getText(), ageField.getValueAsInt()));
        userData.setName(nameField.getText());
        userData.setAge(ageField.getValueAsInt());
        Saver.save("UserData", userData);
        this.close();
    }

}
