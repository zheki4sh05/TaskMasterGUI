package com.example.taskmaster.Controllers;

import Data.UserInfo;
import Data.UserInfoSupplier;
import DataOperations.Interfaces.IAuthUser;
import Utils.RegConf;
import com.example.taskmaster.Content;
import com.example.taskmaster.GuiceControllerFactory;
import com.example.taskmaster.TaskMasterApplication;
import com.google.inject.Inject;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthorizationPageController implements Initializable {
    private String errorMsg;
    @FXML
    private Label regResult;
    @Inject
    private UserInfoSupplier userInfo;
    @Inject
    private IAuthUser authHandler;
    @FXML
    private MFXButton loginActionBtn;
    @FXML
    private TextField loginAuth;
    @FXML
    private Label authResult;
    @FXML
    private PasswordField passAuth;

    @FXML
    private TextField loginReg;
    @FXML
    private PasswordField passReg1;
    @FXML
    private PasswordField passReg2;
    @FXML
    private MFXButton regActionBtn;

    public boolean isLogin() {
        return isLogin;
    }

    private boolean isLogin;

    public boolean isReg() {
        return isReg;
    }

    private boolean isReg;
    @FXML
    private TabPane regTabPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    private void startAuthAction(MouseEvent event)  {
        errorMsg="";
        authResult.setText("");
        if(!tryAuth()){
            authResult.setText(authHandler.getMessage());
        }else{
            isLogin = true;
            authResult.setText("");
            userInfo.setLogin(loginAuth.getText());
            userInfo.setPassword(RegConf.createHash(passAuth.getText()));
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        }
    }
    @FXML
    private void onRegAction(MouseEvent event){
        errorMsg="";
        regResult.setText("");
        boolean result = tryReg();
        regResult.setText(errorMsg);
        if(result){
            isReg = true;
            regResult.setText("");
            regTabPane.getSelectionModel().selectFirst();
//            userInfo.setLogin(loginReg.getText());
//            userInfo.setPassword(passReg1.getText());
//            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        }
    }
    public boolean  tryAuth(){
       int id =  authHandler.login(loginAuth.getText(), RegConf.createHash(passAuth.getText()));
        if(authHandler.getStatusCode()==401 || authHandler.getStatusCode()==503){
            return false;
        }else {
            userInfo.setCookieHeaders(authHandler.getCookies());
            userInfo.setId(id);
            return true;
        }

    }

    private boolean tryReg(){
        if(loginReg.getText().length()< RegConf.LOGIN_MIN){
           errorMsg = errorMsg.concat("Логин слишком короткий! (Минимальная длина логина: "+RegConf.LOGIN_MIN+" )");
        }else if(passReg1.getText().length()<RegConf.LOGIN_MIN || passReg2.getText().length()<RegConf.LOGIN_MIN){
            errorMsg = errorMsg.concat("Пароль слишком короткий! (Минимальная длина пароля: "+RegConf.LOGIN_MIN+" )");
        }else if(!passReg1.getText().equals(passReg2.getText())){
            errorMsg = errorMsg.concat("Пароли не совпадают!");
        }else{
           int id =  authHandler.registration(loginReg.getText(),RegConf.createHash(passReg1.getText()));
            if(authHandler.getStatusCode()==401){
                errorMsg = authHandler.getMessage();
                regResult.setText(errorMsg);
                return false;
            }else {
//                userInfo.setId(id);
//                userInfo.setCookieHeaders(authHandler.getCookies());
                return true;
            }
        }
        return false;

    }
}
