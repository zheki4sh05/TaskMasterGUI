package UIComponents.HomePageComponents;

import Data.UserProject;
import Data.UserTask;
import Utils.NodeLoader;
import com.example.taskmaster.Content;
import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class EditProjectBar extends AnchorPane {
    public UserProject getUserProject() {
        return userProject;
    }

    private UserProject userProject;
    public EditProjectBar(UserProject userProject){
        this.userProject = userProject;
        getChildren().add(NodeLoader.getNode(Content.PROJECT_EDIT_BAR.location, null));
        selectItems();

    }
    private void selectItems(){
        Label prName = (Label)lookup("#projectNameLbl");
        prName.setText(this.userProject.getName());
        ObservableList<String> list = this.userProject.getStatusNamesList();
        System.out.println(list);
        MFXListView<String> nodeList = (MFXListView<String>)lookup("#projectStatusList");
        nodeList.setItems(list);
    }

}
