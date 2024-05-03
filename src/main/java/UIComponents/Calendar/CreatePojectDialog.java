package UIComponents.Calendar;

import Data.UserProject;
import com.example.taskmaster.Content;
import com.example.taskmaster.Controllers.IChildPage;
import com.example.taskmaster.TaskMasterApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CreatePojectDialog extends Dialog<UserProject> implements IChildPage {

    private TextField textField = new TextField();

    private ColorPicker colorPicker = new ColorPicker();

    public String getColor() {
        return color;
    }

    private String color ;

    public CreatePojectDialog() {
        textField.setPromptText("Название");
        textField.requestFocus();
        setTitle("Создать проект");
        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType);
        VBox vbox = new VBox();
        vbox.setPrefWidth(200);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.getChildren().add(textField);
        vbox.getChildren().add(colorPicker);
        getDialogPane().setContent(vbox);
        getDialogPane().setContent(vbox);
        setResultConverter(dialogButton -> {

            if (dialogButton == saveButtonType) {
                return new UserProject(textField.getText(),colorPicker.getValue().toString().replace("0x", "#"));
            }
            return null;
        });
    }

    public UserProject showDialog() {
        Optional<UserProject> value = showAndWait();
        return value.orElse(null);
    }
    private Node loadDialogGUI(){
        Node n=null;
        FXMLLoader fxmlLoader = new FXMLLoader(TaskMasterApplication.class.getResource(Content.CREATE_PLANE_POPUP.location));
        fxmlLoader.setController(this);
        try {
            n = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return n;
    }


}
