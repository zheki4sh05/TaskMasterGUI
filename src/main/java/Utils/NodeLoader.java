package Utils;

import Data.DTO.SettingsDTO;
import com.example.taskmaster.Controllers.IChildPage;
import com.example.taskmaster.TaskMasterApplication;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class  NodeLoader {
    private NodeLoader(){};
    public static Node getNode(String path, IChildPage controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(TaskMasterApplication.class.getResource(path));
        fxmlLoader.setController(controller);
        Node n = null;
        try {
            n = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return n;
    }
    public static void saveSettings(SettingsDTO settingsDTO){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("settings.json"), settingsDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  SettingsDTO loadSettings(){
        SettingsDTO settingsDTO=null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            settingsDTO = objectMapper.readValue(new File("settings.json"), SettingsDTO.class);
            return settingsDTO;
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    }
