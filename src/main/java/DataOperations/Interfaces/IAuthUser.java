package DataOperations.Interfaces;

import java.util.List;

public interface IAuthUser {
    int login(String login, String password);
    int registration(String login,String password);
    boolean logout();
    int getStatusCode();

    String getMessage();

    List<String> getCookies();
}
