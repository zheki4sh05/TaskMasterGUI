package com.example.taskmaster.Controllers;

import Data.DTO.ProjectDTO;
import Data.DTO.SettingsDTO;
import Data.UserInfoSupplier;
import DataOperations.DataProvider;
import DataOperations.Interfaces.IDataParserToJSON;
import DataOperations.Interfaces.IMakeConnection;
import Data.Settings.ISettingsSupplier;
import Utils.NodeLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoadingPageController implements Initializable {
    private Runnable onControllerInitialized;
    @Inject
    private UserInfoSupplier userInfo;
    @Inject
    private DataProvider dataProvider;
    @Inject
    private IMakeConnection clientConnection;
    @FXML
    private ProgressBar bar;
    private  BufferedReader bf;
    @Inject
    private ISettingsSupplier settingsSupplier;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (onControllerInitialized != null) {
            onControllerInitialized.run();
        }

    }

    public void setOnControllerInitialized(Runnable onControllerInitialized) {
        this.onControllerInitialized = onControllerInitialized;
    }
    public void loading(){
        ProgressIndicator progressIndicator = new ProgressIndicator();
        // Создаем полосу прогресса

        // Создаем задачу, которая будет выполняться в фоновом потоке
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObjectMapper objectMapper  = new ObjectMapper();
                List<ProjectDTO> projectDTOList=null;
                try {
                    projectDTOList = objectMapper.readValue(clientConnection.loadUserData(
                            userInfo.getCookies()), new TypeReference<List<ProjectDTO>>(){});

                    mapData(projectDTOList);
                   loadSettings();
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        };

        progressIndicator.progressProperty().bind(task.progressProperty());

        bar.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(event -> {
            closeStage();

        });
        new Thread(task).start();


    }
    private void closeStage(){
        ((Stage)bar.getScene().getWindow()).close();
    }
    private void mapData(List<ProjectDTO> list){
        if(list.size()!=0){
            dataProvider.initData(list);
        }

    }
    private void loadSettings(){
        SettingsDTO settingsDTO= NodeLoader.loadSettings();

        if(settingsDTO!=null){
            settingsSupplier.setSaveMe(settingsDTO.isSaveMe());
            settingsSupplier.setGeneration(settingsDTO.isGeneration());
            settingsSupplier.setKanbans(FXCollections.observableArrayList(settingsDTO.getKanbans()));
        }else{
            settingsSupplier.setSaveMe(false);
            settingsSupplier.setGeneration(false);

            settingsDTO = new SettingsDTO();
            settingsDTO.setSaveMe(settingsSupplier.isSaveMe());
            settingsDTO.setGeneration(settingsSupplier.isGeneration());
            settingsDTO.setKanbans(settingsSupplier.getKanbans());
            NodeLoader.saveSettings(settingsDTO);
        }
    }
    public void initUser(List<String> cookies){
        userInfo.setCookieHeaders(cookies);
        try (BufferedReader reader = new BufferedReader(new FileReader("user.txt"))) {
            userInfo.setId(Integer.parseInt(reader.readLine()));
            userInfo.setLogin(reader.readLine());
            userInfo.setPassword(reader.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }




    }

}
