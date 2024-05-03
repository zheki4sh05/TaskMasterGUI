package com.example.taskmaster;
import Data.DTO.SettingsDTO;
import Data.UserInfo;
import DataOperations.ClientConnection;
import DataOperations.Interfaces.IAuthUser;
import DataOperations.ModulesConfigurators.ClientConnectionModule;
import DataOperations.ModulesConfigurators.DataOperationsModule;
import DataOperations.ModulesConfigurators.MainControllerModule;
import Utils.NodeLoader;
import Utils.Server;
import com.example.taskmaster.Controllers.AuthorizationPageController;
import com.example.taskmaster.Controllers.LoadingPageController;
import com.example.taskmaster.Controllers.MainController;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class TaskMasterApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        boolean isLogin = false;
        boolean isReg = false;
          Injector injector = Guice.createInjector(new MainControllerModule(),new ClientConnectionModule(), new DataOperationsModule());
        boolean isSaveMe = isAutoSave();
        IAuthUser authUser=null;
        while(true){
            if(isSaveMe){
                String login=null;
                String pwd=null;
                try (BufferedReader reader = new BufferedReader(new FileReader("user.txt"))) {
                    reader.readLine();
                    login = reader.readLine();
                    pwd = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                authUser = new ClientConnection();
                if( login!=null && pwd!=null){
                    authUser.login(login,pwd);
                    if(authUser.getStatusCode()==200){
                        isLogin=true;
                    }else{
                        isLogin = false;
                    }
                }else{
                    isSaveMe = false;
                }

            }else{
                AuthorizationPageController authController = injector.getInstance(AuthorizationPageController.class);
                FXMLLoader fxmlLoader1 = new FXMLLoader(TaskMasterApplication.class.getResource(Content.AUTH_PAGE.location));
                fxmlLoader1.setController(authController);
                Scene auth_scene = new Scene(fxmlLoader1.load());

                Stage auth_stage = new Stage();
                auth_stage.setTitle("Авторизация");
                auth_stage.setScene(auth_scene);
                auth_stage.setResizable(false);
                auth_stage.showAndWait();

                isLogin  = authController.isLogin();
                isReg = authController.isReg();

            }
            if(isLogin || isReg){
                if(isLogin){
                    if(isSaveMe){
                        showLoadingPage(injector,authUser.getCookies());
                    }else{
                        showLoadingPage(injector, null);
                    }


                }

                MainController mainController = injector.getInstance(MainController.class);
                GuiceControllerFactory guiceControllerFactory = new GuiceControllerFactory(injector);
                mainController.setControllerFactory(guiceControllerFactory);
                FXMLLoader fxmlLoader = new FXMLLoader(TaskMasterApplication.class.getResource(mainController.getMainControllerFXMLPath()));
                fxmlLoader.setController(mainController);
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                mainController.initStages();
                stage.setTitle("Планировщик задач");
                stage.setScene(scene);
                stage.show();

                if(!mainController.isLogout()){
                    isSaveMe=false;
                    break;
                }
            }
        }



    }
    private boolean isAutoSave(){
        SettingsDTO settingsDTO = NodeLoader.loadSettings();
        if(settingsDTO==null){
            return false;
        }else{
            return settingsDTO.isSaveMe();
        }

    }
    private void showLoadingPage(Injector injector, List<String> cookies){
        LoadingPageController loadingPageController = injector.getInstance(LoadingPageController.class);
        FXMLLoader fxmlLoader2 = new FXMLLoader(TaskMasterApplication.class.getResource(Content.LOADING_PAGE.location));
        fxmlLoader2.setController(loadingPageController);
        if(cookies!=null)
            loadingPageController.initUser(cookies);
        loadingPageController.setOnControllerInitialized(loadingPageController::loading);
        Scene loading_scene = null;
        try {
            loading_scene = new Scene(fxmlLoader2.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage loading_stage = new Stage();
        loading_stage.setTitle("Загрузка...");
        loading_stage.setScene(loading_scene);
        loading_stage.showAndWait();

    }


    public static void main(String[] args) {
        launch();
    }
}