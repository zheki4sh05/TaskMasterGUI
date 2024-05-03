package com.example.taskmaster.Controllers.Interfaces;

import Data.DTO.SettingsDTO;

import Data.UserInfoSupplier;
import Data.UserUpdate;
import DataOperations.IDataTransfer;
import Data.Settings.ISettingsSupplier;
import Utils.NodeLoader;
import com.example.taskmaster.Controllers.IChildPage;
import com.google.inject.Inject;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SettingsPageController implements IChildPage, Initializable {
    private BooleanProperty isEdited = new SimpleBooleanProperty(false);
    @Inject
    private ISettingsSupplier settings;

    @FXML
    private Button addNamebtn;

    @FXML
    private CheckBox saveMeCheckBox;

    @FXML
    private Button deleteNamebtn;

    @FXML
    private ListView<String> kanbanList;

    @FXML
    private MFXButton logoutBtn;

    @FXML
    private TextField nameTextFiled;
    @FXML
    private CheckBox autoGeneration;
    @FXML
    private Button saveSettingsBtn;
    @FXML
    private Button saveLoginBtn;
    @FXML
    private Button resetLoginBtn;
    @FXML
    private TextField changeLoginTield;
    @Inject
    private UserInfoSupplier userInfoSupplier;
    @Inject
    private IDataTransfer dataTransfer;
    @FXML
    private Label authResult;
    @FXML
    private MFXButton closeAppBtn;
    @FXML
   private void addNameHandler(MouseEvent event) {
        String name = nameTextFiled.getText();
        settings.addKanban(name);
        nameTextFiled.setText("");
        isEdited.set(true);
    }

    @FXML
    private void deleteNameHandler(MouseEvent event) {
        if(!kanbanList.getSelectionModel().isEmpty()){
            String name = kanbanList.getSelectionModel().getSelectedItem();
           settings.deleteKanban(name);
            isEdited.set(true);
        }

    }

    @FXML
    void logoutHandler(MouseEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Внимание!");
        alert.setHeaderText("Вы дейтсвительно хотите выйти? ");
        alert.setContentText("Все пользовательские настройки удалятся!");
        ButtonType buttonTypeOk = new ButtonType("ОК", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOk){
            alert.close();

            userInfoSupplier.setLogoutProperty(true);
            File userfile = new File("user.txt");
            if (userfile.delete()) {
                System.out.println("File user deleted successfully");
            } else {
                System.out.println("Failed user to delete the file");
            }

            File settingsJson = new File("settings.json");
            if (settingsJson.delete()) {
                System.out.println("File settings deleted successfully");
            } else {
                System.out.println("Failed settings to delete the file");
            }

            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();


        } else {
            alert.close();
        }





    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        autoGeneration.setSelected(settings.isGeneration());

        saveMeCheckBox.setSelected(settings.isSaveMe());

        kanbanList.setItems(settings.getKanbans());

        saveSettingsBtn.disableProperty().bind(isEdited.not());

        changeLoginTield.setText(userInfoSupplier.getLogin());

        saveMeCheckBox.setVisible(!settings.isSaveMe());

    }
    @FXML
    private void changeSelection(MouseEvent event){
        settings.setSaveMe(saveMeCheckBox.isSelected());
        if(saveMeCheckBox.isSelected()){
            try (PrintWriter writer = new PrintWriter(new FileWriter("user.txt"))) {
                writer.println(userInfoSupplier.getId());
                writer.println(userInfoSupplier.getLogin());
                writer.println(userInfoSupplier.getPassword());
            } catch (IOException e) {
                e.printStackTrace();
            }
            isEdited.set(true);
        }else{
            File userfile = new File("user.txt");
            userfile.delete();
            isEdited.set(true);
        }

    }
    @FXML
    private void autoGenerationSelection(MouseEvent event){
        if(settings.getKanbans().size()!=0){
            settings.setGeneration(true);
            isEdited.set(true);
        }else{
            autoGeneration.setSelected(false);
        }
    }
    @FXML
    private void saveSettingsHandler(MouseEvent event){

        SettingsDTO settingsDTO = new SettingsDTO();
        settingsDTO.setSaveMe(settings.isSaveMe());
        settingsDTO.setGeneration(settings.isGeneration());
        settingsDTO.setKanbans(settings.getKanbans());

        saveByLoader(settingsDTO);

        isEdited.set(false);
    }

    private void saveByLoader(SettingsDTO settingsDTO){
        NodeLoader.saveSettings(settingsDTO);
    }

    @FXML
    private void onResetLoginHandler(MouseEvent mouseEvent){
        changeLoginTield.setText(userInfoSupplier.getLogin());
        authResult.setText("");
        authResult.setStyle("-fx-text-fill: green;");
    }
    @FXML
    private void  onSaveLoginHandler(MouseEvent mouseEvent){
        String login = changeLoginTield.getText();

        if(dataTransfer.checkIfUserLoginExists(login)){
            authResult.setStyle("-fx-text-fill: red;");
            authResult.setText("Пользователь с таким логином уже существует!");
        }else{

            UserUpdate userUpdate = new UserUpdate();
            userUpdate.setId(userInfoSupplier.getId());
            userUpdate.setLogin(login);
            userInfoSupplier.setLogin(login);
            dataTransfer.save(UserUpdate.class,userUpdate);
            authResult.setText("Сохранено!");
            authResult.setStyle("-fx-text-fill: green;");
        }



    }

    @FXML
    private void closeHandler(MouseEvent event){
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        userInfoSupplier.setLogoutProperty(false);
    }





}
