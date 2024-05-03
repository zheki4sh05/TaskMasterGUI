package UIComponents;

import Data.UserTask;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class EditTaskDialog {
        private String title;
        public EditTaskDialog(String title){
                this.title = title;
        }
    public UserTask showDialog(UserTask oldValue, ObservableList<String> list,boolean editMode){ // returns new UserTask
            Dialog<UserTask> dialog = new Dialog<>();
            dialog.setTitle(title);
            dialog.setHeaderText("");
            LocalDate today = LocalDate.now();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            ComboBox statusBox = new ComboBox(list);
            if(editMode){
                    statusBox.setValue(oldValue.getTaskStatus());
            }else{
                    statusBox.setValue(list.get(0));
            }
            ButtonType loginButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
            ButtonType closeType = new ButtonType("Закрыть", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, closeType);
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            TextField title = new TextField();
            title.setPromptText("Название");
            TextField desc = new TextField();
            desc.setPromptText("Описание");
            DatePicker datePicker = new DatePicker();
            if(oldValue !=null){
                    title.setText(oldValue.getTaskName().toString());
                    desc.setText(oldValue.getTaskDesc().toString());
                    datePicker.setValue(oldValue.getDateTo());
            }
            grid.add(new Label("Название:"), 0, 0);
            grid.add(title, 1, 0);
            grid.add(new Label("Описание:"), 0, 1);
            grid.add(desc, 1, 1);
            grid.add(new Label("Дата:"), 0, 2);
            grid.add(datePicker, 1, 2);
            if(statusBox!=null){
                    grid.add(new Label("Статус задачи"), 0, 3);
                    grid.add(statusBox, 1, 3);
            }
            dialog.getDialogPane().setContent(grid);
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == loginButtonType) {
                    return new UserTask(datePicker.getValue()!=null ? datePicker.getValue() : today,
                            new StringBuilder(!title.getText().isEmpty() ? title.getText() : "Новая задача " + sdf.format(new Date())),
                            new StringBuilder(!desc.getText().isEmpty() ? desc.getText() : ""),
                            new StringBuilder(statusBox.getValue().toString()));
                }
                return oldValue;
            });

            Optional<UserTask> result = dialog.showAndWait();
            if(result.isPresent()){
                    return result.get();
            }else{
                    return null;
            }


    }
}
