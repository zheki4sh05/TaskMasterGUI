package com.example.taskmaster.Controllers;
import Data.*;
import Data.DTO.ProjectDTO;
import DataOperations.DataProvider;
import DataOperations.IDataTransfer;
import DataOperations.Interfaces.IDataModelSupplier;
import DataOperations.Interfaces.IDataParserToJSON;
import DataOperations.Interfaces.IMakeConnection;
import UIComponents.Calendar.CalendarController;
import UIComponents.Calendar.CreatePojectDialog;
import UIComponents.EditTaskDialog;
import Data.Settings.ISettingsSupplier;
import UIComponents.KanbanBoard.ColumnItem;
import Utils.NodeLoader;
import com.example.taskmaster.Content;

import com.example.taskmaster.Controllers.Interfaces.SearchController;
import com.example.taskmaster.Controllers.Interfaces.SettingsPageController;
import com.example.taskmaster.GuiceControllerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.awt.*;

import java.io.File;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable, EventHandler<ActionEvent> {
    private GuiceControllerFactory guiceControllerFactory;
    private KanbanBoardPageController kanbanBoardPageController;
    private CalendarController overviewCalendarController;
    private CalendarPageController calendarPageController;
    private HomePageController homePageController;
    private SettingsPageController settingsPageController;
    private SearchController searchController;
    private Map<String, Node> contentList = new HashMap<>();
    private String mainControllerPath;
    @Inject
     private IDataModelSupplier dataModel;
    @Inject
    private IMakeConnection clientConnection;
    @Inject
    private DataProvider dataProvider;
     @FXML
     private MFXButton overviewBtn;
     @FXML
     private MFXButton CalendarActivityBtn;
     @FXML
     private AnchorPane RootStage;
     @FXML
     private AnchorPane ContentArea;
    @FXML
    private MFXButton createPlanBtn;
    @FXML
    private ComboBox<String> projectsDropDownList;
    @FXML
    private Pane createTaskBox;
    @FXML
    private Pane greetMsgBox;
    @FXML
    private Label userNameLbl;
    @Inject
    private UserInfoSupplier userInfo;
    @Inject
    private ISettingsSupplier settings;
    @FXML
    private MFXButton settingsBtn;
    @Inject
    private IDataTransfer dataTransfer;
    @FXML
    private TextField searchInput;
    @FXML
    private MFXButton searchBtn;
    @FXML
    private Label userLoginLbl;



    private BooleanProperty logout = new SimpleBooleanProperty(false);

    @Inject
    public MainController(@Named("MAIN_PAGE") String path)

    {
        this.mainControllerPath = path;
     }
    public String getMainControllerFXMLPath() {
        return mainControllerPath;
    }
     public void initStages(){
        contentList.put(Content.OVERVIEW_PAGE.location, NodeLoader.getNode(Content.OVERVIEW_PAGE.location, overviewCalendarController));
        contentList.put(Content.CALENDAR_PAGE.location, NodeLoader.getNode(Content.CALENDAR_PAGE.location, calendarPageController));
        contentList.put(Content.KANBAN_PAGE.location, NodeLoader.getNode(Content.KANBAN_PAGE.location, kanbanBoardPageController));
        contentList.put(Content.HOME_PAGE.location, NodeLoader.getNode(Content.HOME_PAGE.location, homePageController));
        contentList.put(Content.SETTINGS_PAGE.location,NodeLoader.getNode(Content.SETTINGS_PAGE.location, settingsPageController));
        contentList.put(Content.SEARCH_RESULT.location,NodeLoader.getNode(Content.SEARCH_RESULT.location, searchController));
        showNewPage(contentList.get(Content.HOME_PAGE.location),true);
     }

    @FXML
    private void showMainPage(MouseEvent event){
        showNewPage(contentList.get(Content.HOME_PAGE.location),true);
        homePageController.updateInfo();
        userNameLbl.setText(userInfo.getLogin());

    }
     @FXML
     private void showOverviewCalendarPage(MouseEvent event) {
        overviewCalendarController.drawCalendar();
         showNewPage(contentList.get(Content.OVERVIEW_PAGE.location),false);

     }
     @FXML
     private void showCalendarPage(MouseEvent event){

        showNewPage(contentList.get(Content.CALENDAR_PAGE.location),false);
        calendarPageController.showTasksOnActiveDay(calendarPageController.getActiveDate().toString());
     }
     @FXML
     private void showKanbanPage(MouseEvent event){
         showNewPage(contentList.get(Content.KANBAN_PAGE.location),false);
         kanbanBoardPageController.drawKanban();

     }
     @FXML
     private void showSettingsPage(MouseEvent event){
         showNewPage(contentList.get(Content.SETTINGS_PAGE.location),false);
     }
     private void showNewPage(Node page,boolean showGreetMsg){
         ContentArea.getChildren().clear();
         AnchorPane.setTopAnchor(page, 10.0);
         AnchorPane.setLeftAnchor(page, 10.0);
         AnchorPane.setRightAnchor(page, 10.0);
         AnchorPane.setBottomAnchor(page, 10.0);
         ContentArea.getChildren().add(page);
         createTaskBox.setVisible(!showGreetMsg);
         greetMsgBox.setVisible(showGreetMsg);
     }
     @FXML
    private void showPlanMakerPopup(MouseEvent event){
        CreatePojectDialog createPojectDialog = new CreatePojectDialog();
         UserProject newProject = createPojectDialog.showDialog();
         if(newProject!=null && !newProject.getName().equals("")){

             if(settings.isGeneration()){
                 settings.getKanbans().forEach(newProject::addStatusName);
             }

             newProject.setId(dataTransfer.add(UserProject.class,newProject, userInfo.getId()));



             dataModel.addProject(newProject);
             dataModel.setActiveProject(newProject.getName());
             projectsDropDownList.setValue(newProject.getName());
             if(settings.isGeneration()){
                 settings.getKanbans().forEach(status->{
                     dataTransfer.add(String.class,status, dataModel.getProject(dataModel.getActiveProject()).getId());
                 });
             }
             homePageController.addProject(dataModel.getProject(newProject.getName()));
         }
     }
    public void setControllerFactory(GuiceControllerFactory guiceControllerFactory){
        this.guiceControllerFactory = guiceControllerFactory;
        calendarPageController = (CalendarPageController) guiceControllerFactory.call(CalendarPageController.class);
        overviewCalendarController = (CalendarController) guiceControllerFactory.call(CalendarController.class);
        overviewCalendarController.addButtonClickListener(this);
        kanbanBoardPageController = (KanbanBoardPageController) guiceControllerFactory.call(KanbanBoardPageController.class);
        homePageController =(HomePageController) guiceControllerFactory.call(HomePageController.class);
        homePageController.addButtonClickListener(this);

        settingsPageController = (SettingsPageController) guiceControllerFactory.call(SettingsPageController.class);

        searchController = (SearchController)guiceControllerFactory.call(SearchController.class);
        searchController.addButtonClickListener(this);

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userLoginLbl.setText(userInfo.getLogin());
        userNameLbl.setText(userInfo.getLogin());
        dataTransfer.setCookie(userInfo.getCookies());

        projectsDropDownList.setItems(dataModel.getProjectNames());
        projectsDropDownList.setOnAction(event -> {
            String selectedItem = projectsDropDownList.getSelectionModel().getSelectedItem();
            if(selectedItem!=null){
                dataModel.setActiveProject(selectedItem);
                overviewCalendarController.drawCalendar();
                kanbanBoardPageController.drawKanban();
            }


        });

        logout.bind(userInfo.logoutProperty());


    }

     @FXML
     private void createTask(MouseEvent event){
        if(dataModel.getData().isEmpty()){
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
        }else if(!dataModel.getData().isEmpty()){
            if(dataModel.getStatusNamesList().size()==0){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Внимание!");
                alert.setHeaderText("Нужно добавить этап в доску Канбан!");
                ButtonType buttonTypeOk = new ButtonType("ОК", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(buttonTypeOk);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeOk){
                    alert.close();
                } else {
                    alert.close();
                }
            }else{
                EditTaskDialog dialog = new EditTaskDialog("Создание задачи");
                UserTask result = dialog.showDialog(null,dataModel.getStatusNamesList(),false);
                if(result!=null){
                    result.setId(dataTransfer.add(UserTask.class, result, dataModel.getProject(dataModel.getActiveProject()).getId()));
                    dataModel.getProject(dataModel.getActiveProject()).addTask(result);
                    kanbanBoardPageController.addTask(result);
                }
            }
        }
     }

    @Override
    public void handle(ActionEvent actionEvent) {
        if(actionEvent.getSource() instanceof HomePageController){
            showCalendarPage(CalendarPageController.getTodayDate());
        }else if(actionEvent.getSource() instanceof CalendarController c){
            showCalendarPage(c.getActiveDate());
        }else if(actionEvent.getSource() instanceof SearchController s){
            showCalendarPage(s.getActiveDate());
        }
    }
    private void showCalendarPage(String date){
        if(dataModel.getActiveProject()!=null){
            showNewPage(contentList.get(Content.CALENDAR_PAGE.location),false);
            calendarPageController.showTasksOnActiveDay(date);
        }
    }
    @FXML
    private void callSearchHandler(MouseEvent event){
        String searchInputText = searchInput.getText();
        if(!searchInputText.isEmpty()){
            showNewPage(contentList.get(Content.SEARCH_RESULT.location),false);
            searchController.trySearch(searchInputText);
        }


    }
    public boolean isLogout() {
        return logout.get();
    }
    @FXML
    private void showInfo(MouseEvent event){
        String info="";

        HashMap<String,Long> score = new HashMap<>();

        dataModel.getProject(dataModel.getActiveProject()).getAllTasks().forEach(System.out::println);

        dataModel.getProject(dataModel.getActiveProject()).getStatusNamesList().forEach(status->{
            score.put(status, dataModel.getProject(dataModel.getActiveProject()).getAllTasks().stream().filter(userTask -> userTask.getTaskStatus().toString().equals(status)).count());
        });


        for (Map.Entry<String, Long> entry : score.entrySet()) {

           info =  info.concat(entry.getKey() + " : " + "("+entry.getValue()+")\n");

        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Информация!");
        alert.setHeaderText("Итого по задачам в проекте " + dataModel.getActiveProject());
        alert.setContentText(info);
        ButtonType buttonTypeOk = new ButtonType("ОК", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOk){
            alert.close();
        } else {
            alert.close();
        }

        System.out.println(score.entrySet());
    }



}

