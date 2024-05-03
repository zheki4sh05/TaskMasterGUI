package Data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserTask {


    private int id;

    public LocalDate getDateTo() {
        return dateTo;
    }

    private LocalDate dateTo;
    private StringBuilder taskName;
    private StringBuilder taskDesc;
    private StringBuilder taskStatus;

    private Boolean isImportant=false;
    private StringProperty propertyName=new SimpleStringProperty();

    private ObservableList<Subtask> subtasksList = FXCollections.observableArrayList();


    public UserTask(LocalDate dateTo, StringBuilder taskName, StringBuilder taskDesc, StringBuilder taskStatus) {
        this.dateTo = dateTo;
        this.taskName = taskName;
        propertyName.setValue(taskName.toString());
        this.taskDesc = taskDesc;
        this.taskStatus = taskStatus;
    }


    public StringBuilder getTaskName() {
        return taskName;
    }

    public StringBuilder getTaskDesc() {
        return taskDesc;
    }

    public StringBuilder getTaskStatus() {
        return taskStatus;
    }

    public StringProperty getPropertyName(){
        return this.propertyName;
    }
    public void setName(StringProperty name){
        this.propertyName = name;
    }

    @Override
    public String toString() {
        return "UserTask{" +
                "dateTo=" + dateTo +
                ", taskName=" + taskName +
                ", taskDesc=" + taskDesc +
                ", taskStatus=" + taskStatus +
                ", propertyName=" + propertyName +
                '}';
    }
    public Boolean getImportant() {
        return isImportant;
    }

    public void setImportance(boolean b) {
        this.isImportant = b;
    }
    public void deleteSubtask(Subtask subtask){
        subtasksList.remove(subtask);
    }
    public void addSubtask(Subtask subtask){
        subtasksList.add(subtask);
    }
    public ObservableList<Subtask> getSubtasksList() {
        return subtasksList;
    }

    public void setSubTasksList(ObservableList<Subtask> subtasksList) {
        this.subtasksList = subtasksList;
    }

    public void setStatus(String newValue) {
        this.taskStatus = new StringBuilder(newValue);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
