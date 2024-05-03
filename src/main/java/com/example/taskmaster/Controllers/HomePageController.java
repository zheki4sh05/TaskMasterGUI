package com.example.taskmaster.Controllers;

import Data.ProjectData;
import Data.UserProject;
import Data.UserTask;
import DataOperations.IDataTransfer;
import DataOperations.Interfaces.IDataModelSupplier;
import UIComponents.Calendar.TaskListCell;
import UIComponents.EditProjectDialog;
import UIComponents.EditTaskDialog;
import UIComponents.KanbanBoard.MonthName;
import Utils.NodeLoader;
import Utils.WeekDayName;
import com.example.taskmaster.Content;
import com.example.taskmaster.Controllers.Interfaces.EventClickCreator;
import com.google.inject.Inject;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.ResourceBundle;

public class HomePageController implements IChildPage, Initializable, EventClickCreator<ActionEvent> {
    private EventHandler<ActionEvent> buttonClickListener;
    @Inject
    private IDataModelSupplier dataModel;
    @FXML
    private MFXButton showCalendarPageBtn;
    @FXML
    private Label weekDayNameLbl;
    @FXML
    private Label fullDateLbl;
    @FXML
    private VBox projectsContainer;
    @FXML
    private Label todayTasksCountLbl;
    @FXML
    private Label projectsCountLbl;
    @Inject
    private IDataTransfer dataTransfer;
    private String currentDateStr;
    public void addProject(UserProject userProject){

        AnchorPane projectBar = (AnchorPane) NodeLoader.getNode(Content.PROJECT_EDIT_BAR.location, null);
        Label prName = (Label)projectBar.lookup("#projectNameLbl");
        prName.setText(userProject.getName());
        ObservableList<String> list =userProject.getStatusNamesList();
        MFXListView<String> nodeList = (MFXListView<String>)projectBar.lookup("#projectStatusList");

        nodeList.setItems(list);

        projectBar.setOnMouseClicked(e->{});

        Label todoCountLbl = (Label)projectBar.lookup("#todoCountLbl");
        todoCountLbl.setText(String.valueOf(userProject.getDoneTasks()));
        todoCountLbl.textProperty().bind(userProject.doneTasksProperty().asString());

        Label doneCountLbl = (Label)projectBar.lookup("#doneCountLbl");
        doneCountLbl.setText(String.valueOf(userProject.getTaskCount()));
        doneCountLbl.textProperty().bind(userProject.taskCountProperty().asString());

        ProgressBar progressBar = (ProgressBar) projectBar.lookup("#projectProgressBar");
        progressBar.progressProperty().bind(userProject.progressProperty());

        MFXButton delBtn = (MFXButton) projectBar.lookup("#delProjectBtn");
        delBtn.setOnAction(e->{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Удаление проекта "+userProject.getName());
                alert.setHeaderText("Вы уверены что хотите удалить проект? Всего задач: " + userProject.getAllTasks().size());
                alert.setContentText("Нажмите OK, чтобы удалить или Cansel, чтобы отменить");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        dataModel.deleteProject(userProject.getName());
                        projectsContainer.getChildren().remove(projectBar);
                        dataModel.getData().remove(userProject.getName());
                        dataTransfer.delete(UserProject.class,userProject);
                    }
                });
            });
        MFXButton editBtn = (MFXButton) projectBar.lookup("#editProject");
        editBtn.setOnMouseClicked(e->{
                EditProjectDialog editProjectDialog = new EditProjectDialog("Редактирование проекта");
                ProjectData newUserProject = editProjectDialog.showDialog(userProject);
                if(newUserProject!=null){
                    newUserProject.setId(userProject.getId());
                    dataTransfer.save(ProjectData.class, newUserProject);
                    dataModel.updateProject(userProject.getName(),newUserProject);
                    projectsContainer.getChildren().remove(projectBar);
                    addProject(dataModel.getProject(newUserProject.getName()));
                }

        });
        projectsContainer.getChildren().add(projectBar);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(!dataModel.getData().isEmpty()){
            for (Map.Entry<String, UserProject> set : dataModel.getData().entrySet()) {
                addProject(set.getValue());
            }
        }
        ZonedDateTime currentDate = ZonedDateTime.now();
        int day = currentDate.getDayOfMonth();
        int month = currentDate.getMonthValue();
        int year =  currentDate.getYear();
        String monthName = MonthName.getFullName(month-1);
        weekDayNameLbl.setText(WeekDayName.getFullName(currentDate.getDayOfWeek().getValue()-1));
        fullDateLbl.setText(
                day+" "+
                monthName+" "+
                        year);
        currentDateStr =  year +"-"+month+"-"+(day <= 9 ? "0" : "")+day;
        updateButtonState(dataModel.getAllTasksCountByDate(currentDateStr));

        // Создаем свойство, отслеживающее размер коллекции
        IntegerProperty sizeProperty = new SimpleIntegerProperty(dataModel.getData().size());

        // Привязываем текстовое свойство label к свойству размера коллекции

        projectsCountLbl.textProperty().bind(Bindings.createStringBinding(() ->  String.valueOf(sizeProperty.get()), sizeProperty));

        // Добавляем слушателя изменений размера коллекции
        dataModel.getData().addListener((MapChangeListener<? super String, ? super UserProject>) c -> {
            sizeProperty.set(dataModel.getData().size());
        });
    }

    @Override
    public void addButtonClickListener(EventHandler<ActionEvent> listener) {
        this.buttonClickListener = listener;
    }
    @FXML
    private void openCalendarPage(MouseEvent event) {
        buttonClickListener.handle(new ActionEvent(this, event.getTarget()));
    }
    public void updateInfo(){
        if(!dataModel.getData().isEmpty()){
            int count = dataModel.getAllTasksCountByDate(currentDateStr);
            todayTasksCountLbl.setText(String.valueOf(dataModel.getAllTasksCountByDate(currentDateStr)));
            updateButtonState(count);
        }
    }
    private void updateButtonState(int count){
        if(count>0){
            showCalendarPageBtn.setText("Просмотреть");
        }else{
            showCalendarPageBtn.setText("Запланировать");
        }
    }


}
