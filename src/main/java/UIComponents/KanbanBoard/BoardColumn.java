package UIComponents.KanbanBoard;

import Data.UserTask;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public  class BoardColumn extends VBox {
    public String getTitle() {
        return title;
    }

    private String title;

    private List<UserTask> tasks;
    public BoardColumn(List<UserTask> tasks,String title){
        setPrefHeight(this.prefHeight);
        setPrefWidth(this.prefWidth);
        this.tasks = tasks;
        this.title=title;
        setSpacing(10);
        Button delBtn = new Button(title);
        delBtn.getStyleClass().clear();
        delBtn.setId("delcolumnbtn");
        delBtn.setPrefWidth(prefWidth);
        delBtn.setAlignment(Pos.CENTER);
        delBtn.setCursor(Cursor.HAND);
//        Label titleLbl = new Label(title);
//        titleLbl.setId("#delcolumnbtn");
//        titleLbl.setPrefWidth(prefWidth);
//        titleLbl.setAlignment(Pos.CENTER);
//        getChildren().add(titleLbl);
        getChildren().add(delBtn);
        initItems();

    }
        private void initItems(){
        if (tasks!=null){
            for (UserTask item : tasks){
                getChildren().add(new ColumnItem(item));
            }
//            getChildren().forEach(node -> {
//                if (node instanceof ColumnItem)
//                    addOnClickHandler(node);
//            });
        }

    }
    private final int prefWidth = 259;
    private final int prefHeight = 470;
    public boolean deleteConfirm(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Вы уверены что хотите удалить?");
        ButtonType buttonTypeOk = new ButtonType("Удалить", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeCansel = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk,buttonTypeCansel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOk){
            return true;
        } else {
            return false;
        }
    }
    public ColumnItem createNewPane(UserTask task){
        ColumnItem newColumnItem  = new ColumnItem(task);
        return newColumnItem;
    }
    public void addNewPane(ColumnItem node){
        getChildren().add(node);
    }





}

