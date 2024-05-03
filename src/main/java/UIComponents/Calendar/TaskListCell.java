package UIComponents.Calendar;

import Data.UserTask;
import UIComponents.EditTaskDialog;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;


public class TaskListCell extends ListCell<UserTask> {
    private Label label;
    private String color;

    public TaskListCell(String color) {
        label = new Label();
        this.color = color;
        label.setPrefWidth(getPrefWidth());
       }

    @Override
    protected void updateItem(UserTask item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            label.textProperty().unbind();
            label.setText("");
            label.setStyle("-fx-background-color:"+"white");
        } else {
            label.textProperty().bind(item.getPropertyName());
            label.setStyle("-fx-background-color:"+color);
        }

        setGraphic(label);
    }

}
