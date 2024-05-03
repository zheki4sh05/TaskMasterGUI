package com.example.taskmaster.Controllers.Interfaces;

import Data.DTO.ProjectDTO;
import Data.UserTask;
import DataOperations.Interfaces.IDataModelSupplier;
import UIComponents.KanbanBoard.ColumnItem;
import com.example.taskmaster.Content;
import com.example.taskmaster.Controllers.IChildPage;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SearchController implements Initializable, IChildPage,EventClickCreator<ActionEvent> {
    public String getActiveDate() {
        return activeDate;
    }

    private String activeDate;

    private EventHandler<ActionEvent> buttonClickListener;
    @Inject
    private IDataModelSupplier dataModel;
    @FXML
    private VBox searchItemsList;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void  trySearch(String searchText){
        if(!searchText.isEmpty()){
            searchItemsList.getChildren().clear();
            List<UserTask> userTasks = new ArrayList<>();
            dataModel.getProject(dataModel.getActiveProject()).getAllTasks().forEach(task->{
                if (task.getTaskName().toString().toLowerCase().contains(searchText.toLowerCase()) ||
                        task.getTaskDesc().toString().toLowerCase().contains(searchText.toLowerCase()) ||
                        task.getDateTo().toString().toLowerCase().contains(searchText.toLowerCase())){
                    userTasks.add(task);
                };
            });


            if(userTasks.size()>0){
                ObservableList<ColumnItem> items = FXCollections.observableArrayList();
                userTasks.forEach(task->{
                    System.out.println(task.toString());
                    items.add(addSearchResultItem(task));
                });

                mapData(items);

            }else{

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Элементы не найдены!");
                alert.setContentText("");
                alert.showAndWait();
            }



        }
    }
    public ColumnItem addSearchResultItem(UserTask userTask){
        ColumnItem newPane = new ColumnItem(userTask);
        newPane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                activeDate = newPane.getTask().getDateTo().toString();
                searchItemsList.getChildren().clear();
                buttonClickListener.handle(new ActionEvent(this, event.getTarget()));;
            }
        });

        Button b = (Button) newPane.lookup("#deleteBtn");
        b.setOnAction(event -> {
            searchItemsList.getChildren().remove(newPane);
        });

        return  newPane;
    }

    @Override
    public void addButtonClickListener(EventHandler<ActionEvent> listener) {
        this.buttonClickListener = listener;
    }
    private void mapData(ObservableList<ColumnItem> list){

        if(list.size()!=0){
            System.out.println(list);
            list.forEach(task->{
                searchItemsList.getChildren().add(task);
            });
        }

    }
}
