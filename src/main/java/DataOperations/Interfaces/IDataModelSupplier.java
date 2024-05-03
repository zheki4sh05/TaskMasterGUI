package DataOperations.Interfaces;

import Data.ProjectData;
import Data.UserProject;
import Data.UserTask;
import DataOperations.DataProvider;
import com.example.taskmaster.DataModel;
import com.google.inject.ImplementedBy;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.List;
import java.util.Map;

@ImplementedBy(DataModel.class)
public interface IDataModelSupplier {
    ObservableMap<String,UserProject> getData();
    void addProject(UserProject userProject);
    String getActiveProject();
    void setActiveProject(String projectName);
    UserProject getProject(String name);
    void addTask(UserTask userTask,String projectName);
    void addStatusName(String name);
    ObservableList<String> getStatusNamesList();
    int getAllTasksCountByDate(String date);
    void deleteProject(String name);
    ObservableList<String> getProjectNames();
    void updateProject(String projectName, ProjectData newUserProject);
}
