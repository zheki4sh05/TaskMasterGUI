package DataOperations;

import Data.DTO.ProjectDTO;
import Data.Subtask;
import Data.UserProject;
import Data.UserTask;
import DataOperations.Interfaces.IDataProvider;
import com.google.inject.Inject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataProvider {
    private IDataProvider<String,UserProject> dataProvider;
    @Inject
    public DataProvider(IDataProvider<String,UserProject> dataProvider){
        this.dataProvider=dataProvider;
    }
    public void initData(List<ProjectDTO> list){
        Map<String,UserProject> projectMap = FXCollections.observableHashMap();

        list.forEach(item->{
            UserProject userProject = new UserProject(item.getName().replace("_"," "),item.getColor().replace("%23","#"));
            userProject.setId(item.getId());
            item.getStatusList().forEach(statusDTO -> {
                userProject.addStatusName(statusDTO.getName().replace("_"," "));
            });

            item.getTaskDTOList().forEach(task->{
                UserTask userTask = new UserTask(
                        LocalDate.parse(task.getDateto()),
                        new StringBuilder(task.getName().replace("_"," ")),
                        new StringBuilder(task.getDesc().replace("_"," ")),
                        new StringBuilder(task.getStatus().replace("_"," "))
                );
                task.getSubTaskDTOS().forEach(subTaskDTO -> {
                    Subtask subtask = new Subtask(subTaskDTO.getName().replace("_"," "),
                            new SimpleBooleanProperty(subTaskDTO.isDone()));
                    subtask.setId(subTaskDTO.getId());
                    userTask.addSubtask(subtask);
                });
                userTask.setId(task.getId());
                userProject.addTask(userTask);
            });
            projectMap.put(userProject.getName(),userProject);
        });
        dataProvider.setData(projectMap);
    }
}
