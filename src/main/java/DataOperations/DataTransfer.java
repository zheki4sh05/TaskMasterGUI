package DataOperations;

import Data.*;
import Data.DTO.ProjectDTO;
import Data.DTO.StatusDTO;
import DataOperations.Interfaces.IMakeConnection;
import Utils.Server;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.json.simple.JSONArray;

import java.io.BufferedReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Singleton
public class DataTransfer implements IDataTransfer {
    private int newId;
    public int getProject_id() {
        return project_id;
    }
    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }
    private int project_id;
    public int getUser_id() {
        return user_id;
    }
    private int user_id;

    public List<String> getCookies() {
        return cookies;
    }
    private List<String> cookies = new ArrayList<>();
    private HttpClient httpClient = HttpClient.newBuilder().build();
    private ObjectMapper objectMapper = new ObjectMapper();

    public DataTransfer() throws URISyntaxException{}

    @Override
    public <T> void save(Class<T> tClass, Object o) {
        if(tClass.isInstance(o)){
            if(tClass ==ProjectData.class){
                ProjectData pr = (ProjectData)o;
               makeRequest("?method=update&type=project"+"&id="+pr.getId()+"&name="+pr.getName().replace(" ","_") + "&color=" + pr.getColor().replace("#","%23"),0);
            }else if(tClass == UserTaskWrapper.class){
                UserTaskWrapper task = (UserTaskWrapper)o;
                System.out.println(task);
                System.out.println(task.getUserTask());
                makeRequest("?method=update&type=task"+"&id="+task.getUserTask().getId()+"&name="+task.getUserTask().getTaskName().toString().replace(" ","_") + "&date=" + task.getUserTask().getDateTo().toString()+"&desc="+task.getUserTask().getTaskDesc().toString().replace(" ","_")+"&status="+task.getUserTask().getTaskStatus().toString().replace(" ","_")+"&link="+task.getProjectId(),0);
            }
            else if(tClass == Subtask.class){
                Subtask task = (Subtask) o;
                makeRequest("?method=update&type=subtask"+"&id="+task.getId()+"&name="+task.getName().replace(" ","_") + "&checked=" + (task.isChecked() ? 1 : 0),0);
            }
            else if (tClass== StatusDTO.class) {
                StatusDTO status = (StatusDTO) o;
                makeRequest("?method=update&type=status"+"&id="+status.getId()+"&name="+status.getName().replace(" ","_")+"&oldName="+status.getOldName().replace(" ","_"),0);
            }else if (tClass==UserUpdate.class){
                UserUpdate newData = (UserUpdate) o;
                makeRequest("?method=update&type=user"+"&id="+newData.getId()+"&login="+newData.getLogin(),0);
            }
        }
    }

    @Override
    public <T> int add(Class<T> tClass, Object o, int id) {
        if(tClass.isInstance(o)){
            if(tClass ==UserProject.class){
                UserProject pr = (UserProject)o;
                System.out.println("id" +id);
                makeRequest("?method=add&type=project"+"&link="+id+"&name="+pr.getName().replace(" ","_") + "&color=" + pr.getColor().replace("#","%23"),1);
            }else if(tClass == UserTask.class){
                UserTask task = (UserTask)o;
                System.out.println(task);

                makeRequest("?method=add&type=task"+"&link="+id+"&name="+task.getTaskName().toString().replace(" ","_") + "&date=" + task.getDateTo().toString()+"&desc="+task.getTaskDesc().toString().replace(" ","_")+"&status="+task.getTaskStatus().toString().replace(" ","_"),1);
            }
            else if(tClass == Subtask.class){
                Subtask task = (Subtask) o;
                makeRequest("?method=add&type=subtask"+"&link="+id+"&name="+task.getName().replace(" ","_") + "&checked=" + (task.isChecked() ? 1 : 0),1);
            } else if (tClass==String.class) {
                String status = (String) o;
                makeRequest("?method=add&type=status"+"&link="+id+"&name="+status.replace(" ","_"),1);
            }
        }
        return newId;
    }

    @Override
    public <T> void delete(Class<T> tClass, Object o) {
        if(tClass.isInstance(o)){
            if(tClass ==UserProject.class){
                UserProject pr = (UserProject)o;
                makeRequest("?method=delete&type=project"+"&id="+pr.getId(),0);
            }else if(tClass == UserTask.class){
                UserTask task = (UserTask)o;
                makeRequest("?method=delete&type=task"+"&id="+task.getId(),0);
            }
            else if(tClass == Subtask.class){
                Subtask task = (Subtask) o;
                makeRequest("?method=delete&type=subtask"+"&id="+task.getId(),0);
            } else if (tClass== StatusDTO.class) {
                StatusDTO status = (StatusDTO) o;
                makeRequest("?method=delete&type=status"+"&id="+status.getId()+"&name="+status.getName().replace(" ","_"),0);
            }
        }
    }

    @Override
    public void setCookie(List<String> list) {
        this.cookies = list;
    }

    private void makeRequest(String row,int type){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Server.URL.value+Server.PROJECTS.value+row))
                    .header("Cookie", cookies.get(0))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            CompletableFuture<HttpResponse<String>> response = HttpClient.newBuilder()
                    .build()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString());
            if(type==1 && response.get().statusCode()==200){
                newId = Integer.parseInt(response.get().body());
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean checkIfUserLoginExists(String login){
        return makeRequest("?login="+login)==1;
    }
    private int makeRequest(String row){
        int result=1;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Server.URL.value+Server.REGISTRATION.value+row))
                    .header("Cookie", cookies.get(0))
                    .GET()
                    .build();
            CompletableFuture<HttpResponse<String>> response = HttpClient.newBuilder()
                    .build()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString());
            if(response.get().statusCode()==200){
                result = Integer.parseInt(response.get().body());
            }
            return result;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
