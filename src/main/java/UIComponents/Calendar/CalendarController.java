package UIComponents.Calendar;

import Data.UserProject;
import Data.UserTask;
import Data.UserTaskWrapper;
import DataOperations.IDataTransfer;
import DataOperations.Interfaces.IDataModelSupplier;
import UIComponents.EditTaskDialog;
import com.example.taskmaster.Controllers.IChildPage;
import com.example.taskmaster.Controllers.Interfaces.EventClickCreator;
import com.google.inject.Inject;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import javafx.scene.text.Text;
import UIComponents.KanbanBoard.MonthName;


import java.net.URL;
import java.time.ZonedDateTime;
import java.util.*;

public class CalendarController implements Initializable, IChildPage, EventClickCreator<ActionEvent> {
    private EventHandler<ActionEvent> buttonClickListener;
    private UserProject activeProject;
   @Inject
  private IDataModelSupplier dataModel;
   @Inject
   private IDataTransfer dataTransfer;
    private ZonedDateTime dateFocus;
    private ZonedDateTime today;

    @FXML
    private Text year;
    @FXML
    private MFXButton nextMonthBtn;
    @FXML
    private MFXButton prevMonthBtn;
    @FXML
    private Text month;

    private boolean showAll=false;
    private String activeDate;
    @FXML
    private GridPane calendarGp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();

    }

    @FXML
    private void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendarGp.getChildren().clear();
        drawCalendar();
    }


    @FXML
    private void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendarGp.getChildren().clear();
        drawCalendar();
    }
    @FXML
    private void backToday(ActionEvent event){
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }


    public void drawCalendar(){
        activeProject = dataModel.getProject(dataModel.getActiveProject());

        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(MonthName.getFullName(dateFocus.getMonthValue()-1));

        double calendarWidth = calendarGp.getPrefWidth();
        double calendarHeight = calendarGp.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendarGp.getHgap();
        double spacingV = calendarGp.getVgap();

        //List of activities for a given month

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

                StackPane stackPane = new StackPane();

                ListView<String> taskList = new ListView<>();

                double rectangleWidth =(calendarWidth/7) - strokeWidth - spacingH;
                taskList.setPrefWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV;
                taskList.setPrefHeight(rectangleHeight);
                int calculatedDate = (j+1)+(7*i);
                if(calculatedDate > dateOffset){
                    int currentDate = calculatedDate - dateOffset;
                    if(currentDate <= monthMaxDate){
                        Button date = new Button(String.valueOf(currentDate));
                        if(today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate){
                            date.setStyle("-fx-background-radius:100; " +
                                    "-fx-background-color: rgb(82, 95, 110); " +
                                    "-fx-text-fill:white;" +
                                    "-fx-padding: 2 2 2 2;");
                            date.setPrefWidth(22);
                            date.setPrefHeight(22);
                            date.setAlignment(Pos.CENTER);
                        }else{
                            date.getStyleClass().clear();
                        }
                        date.setCursor(Cursor.HAND);
                        date.setOnMouseClicked(e->{
                            if (e.getClickCount() == 2) {
                                activeDate = dateFocus.getYear()+"-"+dateFocus.getMonth().getValue()+"-"+date.getText();
                               openCalendarPage(e);
                            }
                        });

                        ListView<UserTask> listView=null;
                        if(dataModel.getActiveProject()!=null){
                            String currentDayStr = currentDate <= 9 ? "0"+ currentDate: String.valueOf(currentDate);
                            String currentDateStr = currentYear+"-"+currentMonth+"-"+currentDayStr;
                            date.setText(currentDayStr);
                            ObservableList<UserTask> list;

                            list=dataModel.getProject(dataModel.getActiveProject()).getTasksListByDate(currentDateStr);

                        if(list!=null)
                                listView = new ListView<>(list);
                        }
                        StackPane.setAlignment(date, Pos.TOP_CENTER);
                        if(listView!=null){
                            listView.setPrefHeight(rectangleHeight);
                            listView.setPrefWidth(rectangleWidth);
                            listView.setCellFactory(param -> createCell(dataModel.getProject(dataModel.getActiveProject()).getColor()));
                            stackPane.getChildren().add(listView);
                        }else{
                            Pane emptyBox = new Pane();
                            emptyBox.setPrefHeight(rectangleHeight);
                            emptyBox.setPrefWidth(rectangleWidth);
                            emptyBox.setStyle("-fx-background-color:white;" +
                                    "-fx-border-color: rgb(220, 220, 220);");
                            stackPane.getChildren().add(emptyBox);
                        }

                        stackPane.getChildren().add(date);
                        calendarGp.add(stackPane,j,i);
                    }


                }

            }
        }
    }
    public TaskListCell createCell(String color){
       TaskListCell cell=  new TaskListCell(color) {
                protected void updateItem(UserTask item, boolean empty) {
                    super.updateItem(item, empty);
                    setOnMouseClicked(event -> {
                                if (event.getClickCount() == 2) {
                                    EditTaskDialog dialog = new EditTaskDialog("Редактирование задачи");
                                    UserTask newTask = dialog.showDialog(item,dataModel.getStatusNamesList(),true);
                                    item.setName(new SimpleStringProperty(newTask.getTaskName().toString()));
                                    newTask.setSubTasksList(item.getSubtasksList());
                                    if (!newTask.getDateTo().toString().equals(item.getDateTo().toString())){
                                        dataModel.getProject(dataModel.getActiveProject()).replaceTaskFromDate(item,newTask);
                                        drawCalendar();
                                    }else{
                                        dataModel.getProject(dataModel.getActiveProject()).deleteTask(item);
                                        dataModel.getProject(dataModel.getActiveProject()).addTask(newTask);
                                    }
                                    newTask.setId(item.getId());
                                    UserTaskWrapper userTaskWrapper = new UserTaskWrapper( dataModel.getProject(dataModel.getActiveProject()).getId(),newTask);
                                    dataTransfer.save(UserTaskWrapper.class,userTaskWrapper);
                                    commitEdit(item);
                                }
                            }
                    );
                }
        };



        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem("Удалить");
        menuItem.setOnAction(event -> {
            UserTask item = cell.getItem();
            // удаление элемента из списка
            dataTransfer.delete(UserTask.class, item);
            dataModel.getProject(dataModel.getActiveProject()).deleteTask(item);
        });
        contextMenu.getItems().add(menuItem);
        cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
            if (isNowEmpty) {
                cell.setContextMenu(null);
            } else {
                cell.setContextMenu(contextMenu);
            }
        });
        cell.setOnMouseClicked(event -> {
            if (!cell.isEmpty() && event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(cell, event.getScreenX(), event.getScreenY());
            }
        });
        return cell;
    }

    @Override
    public void addButtonClickListener(EventHandler<ActionEvent> listener) {
        this.buttonClickListener = listener;
    }
    private void openCalendarPage(MouseEvent event) {
        buttonClickListener.handle(new ActionEvent(this, event.getTarget()));
    }
    public String getActiveDate() {
        return activeDate;
    }

    public void showAllProjects() {
        showAll = true;
        drawCalendar();
    }
}