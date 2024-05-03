package com.example.taskmaster.Controllers;

import Data.Subtask;
import Data.UserTask;
import Data.UserTaskWrapper;
import DataOperations.IDataTransfer;
import DataOperations.Interfaces.IDataModelSupplier;
import SmallCalendarPicker.CalendarPickerController;
import UIComponents.Calendar.CalendarController;
import UIComponents.EditTaskDialog;
import UIComponents.KanbanBoard.MonthName;
import Utils.NodeLoader;
import com.example.taskmaster.Content;
import com.google.inject.Inject;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CalendarPageController implements IChildPage, Initializable{

    private StringBuilder activeDate = new StringBuilder("");
    @Inject
    private IDataModelSupplier dataModel ;
    private ZonedDateTime dateFocus;
    private ZonedDateTime today;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane CalendarActivity;
    @FXML
    private VBox tasksContainer;

    @FXML
    private Pane calendarPickerContainer;
    @FXML
    private VBox taskControlBar;
    @FXML
    private VBox subTasksContainer;
    @Inject
    private IDataTransfer dataTransfer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();

       drawCalendarActivity();

    }
    private void addSubTask(){

    }
    private void setComponent(){
        calendarPickerContainer.getChildren().add(NodeLoader.getNode(Content.CALENDAR_PICKER.location,new CalendarPickerController()));
    }
    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        CalendarActivity.getChildren().clear();
        drawCalendarActivity();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        CalendarActivity.getChildren().clear();
        drawCalendarActivity();
    }

    private void drawCalendarActivity(){

        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(MonthName.getFullName(dateFocus.getMonthValue()-1));

        double CalendarActivityWidth = CalendarActivity.getPrefWidth();
        double CalendarActivityHeight = CalendarActivity.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = CalendarActivity.getHgap();
        double spacingV = CalendarActivity.getVgap();



        int monthMaxDate = dateFocus.getMonth().maxLength();
        //Check for leap year
        if(dateFocus.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1,0,0,0,0,dateFocus.getZone()).getDayOfWeek().getValue();
        int currentMonth = dateFocus.getMonthValue();
        int currentYear = dateFocus.getYear();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                MFXButton button = new MFXButton();
                double rectangleWidth =(CalendarActivityWidth/7) - strokeWidth - spacingH;
                button.setPrefWidth(40);
                double rectangleHeight = (CalendarActivityHeight/6) - strokeWidth - spacingV;
                button.setPrefHeight(40);
                button.setCursor(Cursor.HAND);
                button.setStyle("-fx-background-radius:100;-fx-background-color: white;-fx-border-color:rgb(82, 95, 110);-fx-border-radius:100;");

                int calculatedDate = (j+1)+(7*i);
                if(calculatedDate > dateOffset){
                    int currentDate = calculatedDate - dateOffset;
                    if(currentDate <= monthMaxDate){
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        System.out.println(currentDate);
                        date.setTranslateY(textTranslationY);

                        button.setText(date.getText());

                        button.setOnMouseClicked(e->{
                            if(dataModel.getActiveProject()!=null){
                                String currentDayStr = currentDate <= 9 ? "0"+ currentDate: String.valueOf(currentDate);
                                String dateStr = currentYear+"-"+currentMonth+"-"+currentDayStr;
                                taskControlBar.setVisible(false);
                                showTasksOnActiveDay(dateStr);
                            }
                        });
                        CalendarActivity.getChildren().add(button);
                    }
                    if(today.getYear() == dateFocus.getYear() && today.getMonthValue() == dateFocus.getMonthValue() && today.getDayOfMonth() == currentDate){
                        button.setStyle("-fx-background-radius:100; -fx-background-color: rgb(82, 95, 110); -fx-text-fill:white;");

                    }
                }

            }
        }
    }
    @FXML
    private void backToday(ActionEvent event){
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        CalendarActivity.getChildren().clear();
        drawCalendarActivity();
        showTasksOnActiveDay(today.getYear()+"-"+today.getMonthValue()+"-"+today.getDayOfMonth());
    }
    public void showTasksOnActiveDay(String dateStr){
        if(!dataModel.getData().isEmpty()){
            activeDate.delete(0, activeDate.length());
            activeDate.insert(0, dateStr);
            // taskControlBar.setVisible(false);
            tasksContainer.getChildren().clear();
            taskControlBar.setVisible(false);
            ObservableList<UserTask> tasks = dataModel.getProject(dataModel.getActiveProject()).getTasksListByDate(dateStr);
            if( tasks!=null){
                tasks.forEach(task->{
                    AnchorPane anchorPane = (AnchorPane) NodeLoader.getNode(Content.TASK_EDIT_BAR.location, null);
                    anchorPane.setPrefWidth(tasksContainer.getPrefWidth());
                    anchorPane.setOnMouseClicked(e->{
                        taskControlBar.setVisible(true);
                        initControlBar(taskControlBar,task);
                    });
                    tasksContainer.getChildren().add(selectItems(anchorPane,task));
                });
            }else{
                taskControlBar.setVisible(false);
            }
        }


    }
    private AnchorPane selectItems(AnchorPane startAnchor,UserTask task){
        Label taskName = (Label) startAnchor.lookup("#taskNameLbl");
        taskName.setText(task.getTaskName().toString());
        TextFlow taskDesc = (TextFlow) startAnchor.lookup("#taskDescTextAr");
        taskDesc.getChildren().add(new Text(task.getTaskDesc().toString()));
        Label processLbl = (Label) startAnchor.lookup("#processLbl");
        processLbl.setText(task.getTaskStatus().toString());

        MFXButton button = (MFXButton) startAnchor.lookup("#editTask");
        button.setOnMouseClicked(e->{
            EditTaskDialog editTaskDialog= new EditTaskDialog("Редактирование задачи");
            UserTask result = editTaskDialog.showDialog(task,dataModel.getStatusNamesList(),true);
            int index = tasksContainer.getChildren().indexOf(startAnchor);
            tasksContainer.getChildren().remove(startAnchor);

            UserTaskWrapper userTaskWrapper = new UserTaskWrapper(dataModel.getProject(dataModel.getActiveProject()).getId(),task);
            dataTransfer.save(UserTaskWrapper.class,userTaskWrapper);
            dataModel.getProject(dataModel.getActiveProject()).deleteTask(task);
            result.setSubTasksList(task.getSubtasksList());
            dataModel.getProject(dataModel.getActiveProject()).addTask(result);
            AnchorPane anchorPane = (AnchorPane) NodeLoader.getNode(Content.TASK_EDIT_BAR.location, null);
            tasksContainer.getChildren().add(index,selectItems(anchorPane,result));
            initControlBar(taskControlBar,result);
        });
        return startAnchor;
    }
    private void initControlBar(VBox taskControlBar, UserTask targetTask){
       Label taskNameLbl = (Label) taskControlBar.lookup("#taskNameLbl");
        taskNameLbl.setText(targetTask.getTaskName().toString());
        subTasksContainer.getChildren().clear();
        MFXButton addSt = (MFXButton) taskControlBar.lookup("#addSubtaskBtn");
        addSt.setOnMouseClicked(e->{
            TextField subTaskNameField =(TextField) taskControlBar.lookup("#subTaskNameField");
            if (subTaskNameField.getText()!=null || !subTaskNameField.getText().equals("")){
                Subtask subtask = new Subtask(subTaskNameField.getText(), new SimpleBooleanProperty(false));
                subtask.setId(dataTransfer.add(Subtask.class, subtask,targetTask.getId()));
                targetTask.addSubtask(subtask);
                subTasksContainer.getChildren().add(initSubTaskPane(subtask,targetTask));
                subTaskNameField.clear();
            }
        });

        updateSubTasksList(subTasksContainer,targetTask);
        MFXButton delTaskBtn = (MFXButton) taskControlBar.lookup("#delTaskBtn");
        delTaskBtn.setOnMouseClicked(e->{
            String date = targetTask.getDateTo().toString();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Удаление задачи на "+date);
            alert.setHeaderText("Вы уверены что хотите удалить задачу "+targetTask.getTaskName().toString());
            alert.setContentText("Нажмите OK, чтобы удалить или Cansel, чтобы отменить");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    dataTransfer.delete(UserTask.class,targetTask);
                   dataModel.getProject(dataModel.getActiveProject()).deleteTask(targetTask);

                   showTasksOnActiveDay(date);
                   taskControlBar.setVisible(false);
                }
            });
        });



    }
    private void updateSubTasksList(VBox subContainer, UserTask targetTask){
        subContainer.getChildren().clear();
        targetTask.getSubtasksList().forEach(st->{
            subContainer.getChildren().add(initSubTaskPane(st, targetTask));
        });
    }
    private Pane initSubTaskPane(Subtask subtask,UserTask userTask){
        Pane subtaskPane = (Pane)NodeLoader.getNode(Content.SUBTASK.location, null);
        CheckBox checkBox =(CheckBox) subtaskPane.lookup("#subTaskControl");
        checkBox.selectedProperty().bindBidirectional(subtask.checkedProperty());
        subtask.checkedProperty().addListener(e->{
            dataTransfer.save(Subtask.class,subtask);
            System.out.println(subtask);
        });

        TextField subTaskName =(TextField) subtaskPane.lookup("#subTaskName");
        subTaskName.disableProperty().bind(checkBox.selectedProperty());
        subTaskName.setText(subtask.getName());
        subTaskName.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                subTaskName.setEditable(true);
            }
        });
        subTaskName.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                Subtask newSubtask = new Subtask(subTaskName.getText(), checkBox.selectedProperty());
                userTask.deleteSubtask(subtask);
                newSubtask.setId(subtask.getId());
                dataTransfer.save(Subtask.class,newSubtask);
                userTask.addSubtask(newSubtask);
            }
        });
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem  = new MenuItem("Удалить");
        menuItem.setOnAction(event -> {
            userTask.deleteSubtask(subtask);
            dataTransfer.delete(Subtask.class,subtask);
            updateSubTasksList(subTasksContainer,userTask);
        });
        contextMenu.getItems().add(menuItem);
        subtaskPane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(subtaskPane, event.getScreenX(), event.getScreenY());
            }
        });
        return subtaskPane;
    }
    public StringBuilder getActiveDate() {
        return activeDate;
    }
    public static String getTodayDate(){
        ZonedDateTime today = ZonedDateTime.now();
        return today.getYear()+"-"+today.getMonthValue()+"-"+ (today.getDayOfMonth() < 10 ? "0"+today.getDayOfMonth(): today.getDayOfMonth());
    }

}
