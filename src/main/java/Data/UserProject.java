package Data;

import UIComponents.KanbanBoard.BoardColumn;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.time.LocalDate;
import java.util.*;

public class UserProject {
    private int id;
    private int todoTasksInt=0;
    private int doneTasksInt=0;
    private int taskCountInt=0;
    private double progressDouble=0.0;

    private final SimpleIntegerProperty todoTasks=new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty doneTasks=new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty taskCount=new SimpleIntegerProperty(0);
    private final DoubleProperty progressProp=new SimpleDoubleProperty(0.0);
    private String name=null;

    public String getColor() {
        return color;
    }

    private String color;

    private final Map<String,ObservableList<UserTask>> tasksListByDate = new HashMap<>();
    private final ObservableList<String> statusNamesList = FXCollections.observableArrayList();


    public ObservableList<String> getStatusNamesList() {
        return statusNamesList;

    }
    private ObservableMap<StringProperty,IntegerProperty> stNamesList  = FXCollections.observableHashMap();

    public void addStatusName(String name){
        doneTasksInt=0;
        doneTasks.set(doneTasksInt);
        statusNamesList.add(name);
    }
    public void replaceTaskFromDate(UserTask oldValue,UserTask newValue){
        if(!tasksListByDate.containsKey(newValue.getDateTo().toString())){
            tasksListByDate.put(newValue.getDateTo().toString(),FXCollections.observableArrayList());
        }
        tasksListByDate.get(oldValue.getDateTo().toString()).remove(oldValue);
        tasksListByDate.get(newValue.getDateTo().toString()).add(newValue);
    }
    public ObservableList<UserTask> getTasksListByDate(String date){

        System.out.println(tasksListByDate.keySet());
        System.out.println("tasksListByDate.get(date) "+ tasksListByDate.get(date));
        return tasksListByDate.get(date);
    }
    public UserProject(String name,String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }
//    public UserTask findTask(int index){
//        return new UserTask(new Date(),null,null);
//    }
    public void deleteTask(UserTask task) {
        tasksListByDate.get(task.getDateTo().toString()).remove(task);
        taskCountInt -= 1;
        taskCount.set(taskCountInt);
        if (statusNamesList.size() > 1) {
            if (task.getTaskStatus().toString().equals(statusNamesList.get(statusNamesList.size() - 1))) {
                doneTasksInt -= 1;
                doneTasks.set(doneTasksInt);
//            }else if(task.getTaskStatus().toString().equals(statusNamesList.get(0))){
//                todoTasksInt-=1;
//                todoTasks.set(todoTasksInt);
//            }
            }
        }
        if(taskCountInt!=0)
            progressDouble =(double) doneTasksInt/taskCountInt;
        progressProp.set(progressDouble);

    }

        public void addTask (UserTask task){

            if (!tasksListByDate.containsKey(task.getDateTo().toString())) {
                tasksListByDate.put(task.getDateTo().toString(), FXCollections.observableArrayList());

            }
            tasksListByDate.get(task.getDateTo().toString()).add(task);

//        System.out.println("task.getDateTo().toString() "+task.getDateTo().toString());
//        tasksListByDate.put(task.getDateTo().toString(),FXCollections.observableArrayList(task));

            taskCountInt += 1;
            taskCount.set(taskCountInt);
            if (statusNamesList.size() > 1) {
                if (task.getTaskStatus().toString().equals(statusNamesList.get(statusNamesList.size() - 1))) {
                    doneTasksInt += 1;
                    doneTasks.set(doneTasksInt);
//            }else if(task.getTaskStatus().toString().equals(statusNamesList.get(0))){
//                todoTasksInt+=1;
//                todoTasks.set(todoTasksInt);
//                System.out.println("todoTasks "+todoTasks.get());
//            }
                }
            }
            if(taskCountInt!=0)
                progressDouble =(double) doneTasksInt/taskCountInt;
            progressProp.set(progressDouble);
            System.out.println("progressDouble "+progressDouble);
        }


    public List<UserTask> getAllTasks() {
        List<UserTask> list = new ArrayList<>();
        Iterator<Map.Entry<String, ObservableList<UserTask>>> iterator = tasksListByDate.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, ObservableList<UserTask>> entry = iterator.next();
            entry.getValue().forEach(item->{
                list.add(item);
            });
        }
        return list;
    }
    public boolean isTaskCollectionEmpty(){
        return tasksListByDate.isEmpty();
    }
    public boolean isTaskCollectionByDateEmpty(String date){
        if(!tasksListByDate.containsKey(date)){
            return true;
        }else if(tasksListByDate.get(date).size()==0){
            return true;
        }else{
            return false;
        }
    }

    public void updateBy(ProjectData newUserProject) {
        this.name = newUserProject.getName();
        this.color = newUserProject.getColor();
    }
    public int getDoneTasks() {
        return doneTasks.get();
    }

    public SimpleIntegerProperty doneTasksProperty() {
        return doneTasks;
    }

    public int getTaskCount() {
        return taskCount.get();
    }

    public SimpleIntegerProperty taskCountProperty() {
        return taskCount;
    }
    public int getTodoTasks() {
        return todoTasks.get();
    }

    public SimpleIntegerProperty todoTasksProperty() {
        return todoTasks;
    }
    public double getProgress() {
        return progressProp.get();
    }


    public DoubleProperty progressProperty() {
        return progressProp;
    }

    public void deleteStatus(String title) {
        statusNamesList.remove(title);
        Iterator<Map.Entry<String, ObservableList<UserTask>>> iterator = tasksListByDate.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, ObservableList<UserTask>> entry = iterator.next();
           entry.getValue().removeIf(item-> item.getTaskStatus().toString().equals(title));
        }
    }

    public void changeStatusName(String oldValue, String newValue, int order) {

        if(!oldValue.equals(newValue)) {
            getAllTasks().forEach(task -> {
                if (task.getTaskStatus().toString().equals(oldValue)) {
                    task.setStatus(newValue);
                }
            });
            statusNamesList.forEach(e -> {
                System.out.println(e);
            });
            statusNamesList.remove(oldValue);
            statusNamesList.add(order,newValue);
        }else{
            String replacedElem = statusNamesList.get(order);
            int oldOrder = statusNamesList.indexOf(oldValue);
            statusNamesList.set(order,oldValue);
            statusNamesList.set(oldOrder,replacedElem);
        }


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
