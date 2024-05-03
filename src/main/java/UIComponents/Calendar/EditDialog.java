package UIComponents.Calendar;

import com.example.taskmaster.TaskMasterApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditDialog extends Dialog<String> implements Initializable {
   @FXML
   private TextField textField;
   @FXML
    private TextField description;

    public String getColor() {
        return color;
    }

    private String color ;

    public EditDialog(String initialValue, String style) {
        setTitle("Реадктировать задачу");
        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(loadDialogGUI());
        getDialogPane().setContent(vbox);
        textField.setText(initialValue);
        getDialogPane().setContent(vbox);
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return textField.getText();
            }
            return null;
        });
    }

    public String showDialog() {
        Optional<String> result = showAndWait();
        return result.orElse(null);
    }
    private Node loadDialogGUI(){
        Node n=null;
        FXMLLoader fxmlLoader = new FXMLLoader(TaskMasterApplication.class.getResource("editTaskDialogGUi.fxml"));
        fxmlLoader.setController(this);
        try {
            n = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return n;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}