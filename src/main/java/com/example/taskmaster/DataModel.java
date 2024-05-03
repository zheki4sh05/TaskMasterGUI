package com.example.taskmaster;

import Data.ProjectData;
import Data.UserProject;
import Data.UserTask;
import DataOperations.Interfaces.IDataModelSupplier;
import DataOperations.Interfaces.IDataProvider;
import UIComponents.KanbanBoard.BoardColumn;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.time.LocalDate;
import java.util.*;

@Singleton
public class DataModel implements IDataModelSupplier, IDataProvider<String,UserProject> {
    private String activeProject;
    private ObservableMap<String,UserProject> list=FXCollections.observableHashMap();

    @Override
    public ObservableList<String> getProjectNames() {
        return projectNames;
    }

    @Override
    public void updateProject(String projectName, ProjectData newUserProject) {
        projectNames.remove(projectName);
        projectNames.add(newUserProject.getName());
        UserProject currentUserProject =  getProject(projectName);
        currentUserProject.updateBy(newUserProject);
        list.remove(projectName);
        list.put(currentUserProject.getName(),currentUserProject);
        setActiveProject(newUserProject.getName());
    }

    private ObservableList<String> projectNames = FXCollections.observableArrayList();
    @Override
    public ObservableMap<String,UserProject> getData() {
        return list;
    }

    @Override
    public void addProject(UserProject userProject) {
        list.put(userProject.getName(),userProject);
        projectNames.add(userProject.getName());
    }

    @Override
    public String getActiveProject() {
        return activeProject;
    }

    @Override
    public void setActiveProject(String projectName) {
        this.activeProject = projectName;
    }
    @Override
    public UserProject getProject(String name){
        return list.get(name);
    }

    @Override
    public void addTask(UserTask userTask, String projectName) {
        list.get(projectName).addTask(userTask);
    }

    @Override
    public void addStatusName(String name) {
        list.get(activeProject).addStatusName(name);
    }

    @Override
    public ObservableList<String> getStatusNamesList() {
        if(!list.isEmpty()){
            System.out.println("activeProject "+activeProject);
            return list.get(getActiveProject()).getStatusNamesList();
        }else{
           return FXCollections.observableArrayList();
        }
    }

    @Override
    public void setData(Map<String,UserProject> list) {
        this.list = FXCollections.observableMap(list);
        activeProject = list.keySet().iterator().next();
        projectNames.setAll(list.keySet());
    }
    @Override
    public int getAllTasksCountByDate(String date){
        int count=0;
        Iterator<Map.Entry<String, UserProject>> iterator = list.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, UserProject> entry = iterator.next();
            if(!entry.getValue().isTaskCollectionByDateEmpty(date)){
                count+=entry.getValue().getTasksListByDate(date).size();
            }
        }
        return count;

    }
    @Override
    public void deleteProject(String name){
        list.remove(name);
        projectNames.remove(name);
    }


}
