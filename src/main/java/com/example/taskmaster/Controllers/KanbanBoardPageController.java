package com.example.taskmaster.Controllers;

import Data.DTO.StatusDTO;
import Data.UserTask;
import Data.UserTaskWrapper;
import DataOperations.IDataTransfer;
import DataOperations.Interfaces.IDataModelSupplier;
import UIComponents.EditTaskDialog;
import UIComponents.KanbanBoard.BoardColumn;
import UIComponents.KanbanBoard.BoardColumnData;
import UIComponents.KanbanBoard.ColumnItem;
import UIComponents.KanbanBoard.EditKandanColumnDialog;
import com.google.inject.Inject;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.*;

public class KanbanBoardPageController implements Initializable, IChildPage {
    @Inject
    private IDataModelSupplier dataModel;
    private final Map<String, BoardColumn> columnList = new HashMap<>();
    private final Map<String,ArrayList<UserTask>> sortedListByStatus = new HashMap<>();
    public KanbanBoardPageController(){}
    @FXML
    private AnchorPane kanbanBoardWrapper;
    @FXML
    private ComboBox<String> columnsComboBox;
    @FXML
    private MFXButton editBoardBtn;
    @FXML
    private MFXButton deleteBoardBtn;
    @Inject
    private IDataTransfer dataTransfer;
//    @FXML
//    private GridPane boardHeader;
    @FXML
    private HBox columnsContainer;
//    private void drawBorderPane(){
//        boardHeader.getChildren().forEach(node->{
//            GridPane.setHgrow(node, Priority.ALWAYS);
//            GridPane.setHalignment(node, HPos.CENTER);
//        });
//    }
    public void  voidDrawColumns(){

        if(dataModel.getStatusNamesList().size()!=0){
            dataModel.getStatusNamesList().forEach(status->{
                columnList.put(status,new BoardColumn( null,status));
                columnsContainer.getChildren().add(columnList.get(status));
            });
        }
    }
    public void sortTasksByColumns(){

        if(dataModel.getActiveProject()!=null){
            if(dataModel.getStatusNamesList().size()!=0){
                dataModel.getProject(dataModel.getActiveProject()).getAllTasks().forEach(task->{
                    addTask(task);
                });
            }
        }

    }
    public void drawKanban(){
        columnsContainer.getChildren().clear();
        columnList.clear();
        voidDrawColumns();
        sortTasksByColumns();
       // editBoardBtn.setDisable(true);
        columnsComboBox.setItems(dataModel.getStatusNamesList());
        columnsComboBox.disableProperty().bind(Bindings.isEmpty(columnsComboBox.getItems()));
        deleteBoardBtn.disableProperty().bind(Bindings.isEmpty(columnsComboBox.getItems()));
        editBoardBtn.disableProperty().bind(Bindings.isEmpty(columnsComboBox.getItems()));
//        deleteBoardBtn.disableProperty().bind(Bindings.isEmpty(columnsComboBox.getItems()));
//        columnsComboBox.disableProperty().bind(Bindings.isEmpty(columnsComboBox.getItems()));

//        if(dataModel.getActiveProject()!=null){
//            if(dataModel.getStatusNamesList().size()!=0){
//
//
//
//            }
//
//            dataModel.getStatusNamesList().forEach(status->{
//                sortedListByStatus.put(status,new ArrayList<UserTask>());
//            });
//            dataModel.getProject(dataModel.getActiveProject()).getAllTasks().forEach(task->{
//                dataModel.getStatusNamesList().forEach(status->{
//                    sortedListByStatus.get(status).add(task);
//                });
//            });
//            if(dataModel.getStatusNamesList().size()!=0){
//                dataModel.getStatusNamesList().forEach(status->{
//                    columnList.put(status,new BoardColumn( sortedListByStatus.get(status),status));
//                    columnsContainer.getChildren().add(columnList.get(status));
//                });
//            }
//
//        }
    }
//    public <E> void addListenerTo(ObservableList<UserTask> list){
//        ListChangeListener<UserTask> listener = new ListChangeListener<UserTask>() {
//            @Override
//            public void onChanged(Change<? extends UserTask> c) {
//                while (c.next()) {
//                    if (c.wasAdded()) {
//                        System.out.println("Added: " + c.getAddedSubList());
//                    }
//                    if (c.wasRemoved()) {
//                        System.out.println("Removed: " + c.getRemoved().get(0));
//                    }
//                    if (c.wasReplaced()) {
//                        System.out.println("Replaced: " + c.getFrom() + " to " + c.getTo());
//                    }
//                }
//            }
//        };
//        list.addListener(listener);

//    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {







    }
    @FXML
    private void createBoardConfirm(){
        if(!dataModel.getData().isEmpty()){
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Создание колонки Канбан");
            dialog.setHeaderText("Для начала придумаем название:");
            dialog.setContentText("");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                System.out.println(result.get());
                BoardColumn boardColumn = new BoardColumn(null, result.get());
                Button delBtn = (Button) boardColumn.lookup("#delcolumnbtn");
                delBtn.setOnMouseClicked(e->{
                    if(e.getClickCount() ==2){
                        if (boardColumn.deleteConfirm()){
                            dataModel.getProject(dataModel.getActiveProject()).deleteStatus(boardColumn.getTitle());
                            columnsContainer.getChildren().remove(boardColumn);
                        }
                    }

                });

                columnList.put(result.get(), boardColumn);
                dataModel.addStatusName(result.get());
                columnsContainer.getChildren().add(columnList.get(result.get()));
                dataTransfer.add(String.class,result.get(), dataModel.getProject(dataModel.getActiveProject()).getId());
//            boardHeader.add(new Label(result.get()),columnNamesList.size()-1,0);
//            drawBorderPane();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Внимание!");
            alert.setHeaderText("Нужно создать хотя бы один проект");
            ButtonType buttonTypeOk = new ButtonType("ОК", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeOk);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOk){
                alert.close();
            } else {
                alert.close();
            }
        }

    }
    public void addTask(UserTask task){
        BoardColumn boardColumn =  columnList.get(task.getTaskStatus().toString());
        ColumnItem newPane = boardColumn.createNewPane(task);
        newPane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                EditTaskDialog editTaskDialog= new EditTaskDialog("Редактирование задачи");
                UserTask result = editTaskDialog.showDialog(newPane.getTask(),dataModel.getStatusNamesList(),true);
                boardColumn.getChildren().remove(newPane);
                result.setSubTasksList(task.getSubtasksList());
                result.setId(task.getId());
                dataModel.getProject(dataModel.getActiveProject()).deleteTask(newPane.getTask());
                dataModel.getProject(dataModel.getActiveProject()).addTask(result);

                UserTaskWrapper userTaskWrapper = new UserTaskWrapper(dataModel.getProject(dataModel.getActiveProject()).getId(),result);
                dataTransfer.save(UserTaskWrapper.class,userTaskWrapper);
                addTask(result);

            }
        });

        Button b = (Button) newPane.lookup("#deleteBtn");
        b.setOnAction(event -> {
            if (boardColumn.deleteConfirm()){
                boardColumn.getChildren().remove(newPane);
                dataTransfer.delete(UserTask.class, newPane.getTask());
                dataModel.getProject(dataModel.getActiveProject()).deleteTask(newPane.getTask());
            }
        });
        boardColumn.addNewPane(newPane);
    }
    @FXML
    private void deleteBoardHandler(MouseEvent mouseEvent){
        String status  = columnsComboBox.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Внимание!");
        alert.setHeaderText("Вы дейтсвительно хотите удалить колонку "+columnsComboBox.getSelectionModel().getSelectedItem()+"?");
        alert.setContentText("Все задачи с этим статусом тоже удалятся!");
        ButtonType buttonTypeOk = new ButtonType("ОК", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOk){
            alert.close();
            dataModel.getProject(dataModel.getActiveProject()).deleteStatus(status);
            StatusDTO statusDTO = new StatusDTO(dataModel.getProject(dataModel.getActiveProject()).getId(), status);
            dataTransfer.delete(StatusDTO.class, statusDTO);
            drawKanban();
        } else {
            alert.close();
        }
    }
    @FXML
    private void editBoardHandler(MouseEvent mouseEvent){
        EditKandanColumnDialog editKandanColumnDialog = new EditKandanColumnDialog("Редактированиеколонки Канбан доски");
        BoardColumnData boardColumnData =  editKandanColumnDialog.showDialog(
                columnsComboBox.getSelectionModel().getSelectedItem(),
                dataModel.getStatusNamesList().size(),
                dataModel.getStatusNamesList().indexOf(columnsComboBox.getSelectionModel().getSelectedItem())
        );
        System.out.println(" dataModel.getStatusNamesList().indexOf(columnsComboBox.getSelectionModel().getSelectedItem()) "+ dataModel.getStatusNamesList().indexOf(columnsComboBox.getSelectionModel().getSelectedItem()));
        if(boardColumnData!=null){
            dataModel.getProject(dataModel.getActiveProject()).changeStatusName(columnsComboBox.getSelectionModel().getSelectedItem(), boardColumnData.getName(), boardColumnData.getOrder());

            StatusDTO statusDTO = new StatusDTO(dataModel.getProject(dataModel.getActiveProject()).getId(), boardColumnData.getName(),boardColumnData.getOldName());
            dataTransfer.save(StatusDTO.class, statusDTO);
            drawKanban();
        }
            System.out.println(boardColumnData);


    }

}
