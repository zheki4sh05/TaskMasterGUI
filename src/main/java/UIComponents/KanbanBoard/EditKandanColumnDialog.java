package UIComponents.KanbanBoard;

import Data.ProjectData;
import Data.UserProject;
import Data.UserTask;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.Optional;

public class EditKandanColumnDialog {
    private String title;
    public EditKandanColumnDialog(String title){
        this.title = title;
    }
    public BoardColumnData showDialog(String oldValue,int count,int currentIndex){
        Dialog<BoardColumnData> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText("");
        ButtonType loginButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        ButtonType closeType = new ButtonType("Закрыть", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, closeType);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        Pagination pagination = new Pagination();
        pagination.setMaxPageIndicatorCount(count);
        pagination.setPageCount(count);
        pagination.setCurrentPageIndex(currentIndex);
        TextField title = new TextField();
        title.setPromptText("Название");
        title.setText(oldValue);
        grid.add(new Label("Название:"), 0, 0);
        grid.add(title, 1, 0);
        grid.add(new Label("Позиция:"), 0, 1);
        grid.add(pagination, 1, 1);
        dialog.getDialogPane().setContent(grid);
        System.out.println("currentIndex "+ currentIndex);
        System.out.println("   pagination.getCurrentPageIndex() "+   pagination.getCurrentPageIndex());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
               return new BoardColumnData(
                       title.getText(),
                       pagination.getCurrentPageIndex(),
                       oldValue
               );
            }
            return null;
        });

        Optional<BoardColumnData> result = dialog.showAndWait();
        if(result.isPresent()){
            return result.get();
        }else{
            return null;
        }


    }
}
