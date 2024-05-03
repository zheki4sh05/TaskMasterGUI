package DataOperations;

import java.io.*;
import java.net.*;

import Data.DTO.ProjectDTO;
import Data.ProjectData;
import Data.UserProject;
import DataOperations.Interfaces.IAuthUser;
import DataOperations.Interfaces.IMakeConnection;
import Utils.Server;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.net.http.HttpClient;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class ClientConnection implements IMakeConnection, IAuthUser {
    private int client_id=0;
    private int statusCode;
    private List<String> cookies;
    private String message;

        public String getUrl() {
            return url;
        }
        private String url;
        @Override
    public String loadUserData(List<String> cookies) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Server.URL.value+Server.PROJECTS.value))
                    .header("Cookie", cookies.get(0))
                    .GET()
                    .build();
            CompletableFuture<HttpResponse<String>> response = HttpClient.newBuilder()
                    .build()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> httpResponse  = response.get();
            message = httpResponse.body();
            statusCode  = httpResponse.statusCode();
            if(statusCode == 200)
                cookies= httpResponse.headers().map().get("set-cookie");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            message = "Сервер не отвечает!";
            statusCode = 503;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return message;
    }




    private String  createRequestStr(String login,String password){
            return "?login=" + login + "&password=" + password;
    }
    private HttpRequest.BodyPublisher createBodyPublisher(String login,String password){
        return HttpRequest.BodyPublishers.ofString(createRequestStr(login,password));
    }
    public void makeAuth(String type,String login,String password){
        HttpRequest.BodyPublisher body = createBodyPublisher(login,password);
        String requestStr = createRequestStr(login,password);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Server.URL.value+type+requestStr))
                    .POST(body)
                    .build();
            CompletableFuture<HttpResponse<String>> response = HttpClient.newBuilder()
                    .build()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> httpResponse  = response.get();
            message = httpResponse.body();
            statusCode  = httpResponse.statusCode();
            if(statusCode == 200){

                cookies= httpResponse.headers().map().get("set-cookie");
                client_id = Integer.parseInt(message);

            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            message = "Сервер не отвечает!";
            statusCode = 503;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public int login(String login, String password) {
            makeAuth(Server.LOGIN.value,login,password);
            return client_id;
    }


    @Override
    public int registration(String login,String password) {
            makeAuth(Server.REGISTRATION.value, login,password);
            return client_id;
    }

    @Override
    public boolean logout() {
        return false;
    }

    @Override
    public int getStatusCode() {
        return this.statusCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public List<String> getCookies() {
        return this.cookies;
    }


}

