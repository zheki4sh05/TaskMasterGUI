package UIComponents;

import Data.ProjectData;
import Data.UserProject;
import Data.UserTask;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.Optional;

public class EditProjectDialog {
    private String title;
    public EditProjectDialog(String title){
        this.title = title;
    }
    public ProjectData showDialog(UserProject oldValue){ // returns new UserProject
        Dialog<ProjectData> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText("");
        ButtonType loginButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        ButtonType closeType = new ButtonType("Закрыть", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, closeType);
        ColorPicker colorPicker = new ColorPicker();
        String currentColor  = oldValue.getColor().replace("#", "0x");
        colorPicker.setValue(Color.web(currentColor));
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField title = new TextField();
        title.setPromptText("Название");
//        TextField desc = new TextField();
//        desc.setPromptText("Описание");
        title.setText(oldValue.getName().toString());
        grid.add(new Label("Название:"), 0, 0);
        grid.add(title, 1, 0);
        grid.add(new Label("Цвет:"), 0, 1);
        grid.add(colorPicker, 1, 1);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new ProjectData(title.getText(),
                        colorPicker.getValue().toString().replace("0x", "#"));
            }
            return null;
        });

        Optional<ProjectData> result = dialog.showAndWait();
        if(result.isPresent()){
            return result.get();
        }else{
            return null;
        }


    }
}
