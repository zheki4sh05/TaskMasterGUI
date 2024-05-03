package UIComponents.KanbanBoard;

import Data.UserTask;
import Utils.NodeLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ColumnItem  extends Pane implements Initializable{
    public UserTask getTask() {
        return task;
    }

    private UserTask task;
    public ColumnItem(UserTask task){
        this.task = task;
        VBox newNode = (VBox) NodeLoader.getNode("board_item.fxml",null);
        getChildren().add(newNode);
//        setOnMouseClicked(event -> {
//            if (event.getClickCount() == 2) {
//                Optional<UserTask> result = editDialog(this.task);
//                this.task = result.get();
//                selectElements(this.task);
//            }
//        });
       selectElements(task);
    }
    private void selectElements(UserTask task){
        Label month = (Label) lookup("#itemDateMonth");
        month.setText(MonthName.getShortName(task.getDateTo().getMonth().getValue()-1));
        Label day = (Label) lookup("#itemDateDay");
        day.setText(String.valueOf(task.getDateTo().getDayOfMonth()));
        Label name = (Label)  lookup("#itemHeader");
        name.setText(task.getTaskName().toString());
        Label desc = (Label)  lookup("#itemDesc");
        desc.setText(task.getTaskDesc().toString());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public Optional<UserTask>  editDialog(UserTask oldValue){
        Dialog<UserTask> dialog = new Dialog<>();
        dialog.setTitle("Редактирование");
        dialog.setHeaderText("");

// Set the icon (must be included in the project).


// Set the button types.
        ButtonType loginButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

// Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField title = new TextField(task.getTaskName().toString());
        title.setPromptText("Название");
        TextField desc = new TextField(task.getTaskDesc().toString());
        desc.setPromptText("Описание");
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(oldValue.getDateTo());
        grid.add(new Label("Название:"), 0, 0);
        grid.add(title, 1, 0);
        grid.add(new Label("Описание:"), 0, 1);
        grid.add(desc, 1, 1);
        grid.add(new Label("Дата:"), 0, 2);
        grid.add(datePicker, 1, 2);

// Enable/Disable login button depending on whether a username was entered.
//        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
//        loginButton.setDisable(true);
//
//// Do some validation (using the Java 8 lambda syntax).
//        username.textProperty().addListener((observable, oldValue, newValue) -> {
//            loginButton.setDisable(newValue.trim().isEmpty());
//        });

        dialog.getDialogPane().setContent(grid);

// Request focus on the username field by default.
       // Platform.runLater(() -> username.requestFocus());

// Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new UserTask(datePicker.getValue(),new StringBuilder(title.getText()),new StringBuilder(desc.getText()),new StringBuilder(" "));
            }
            return oldValue;
        });

       Optional<UserTask> result = dialog.showAndWait();

       return result;
    }

}
