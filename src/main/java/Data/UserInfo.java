package Data;

import com.google.inject.Singleton;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.List;

@Singleton
public class UserInfo implements UserInfoSupplier{
    public void setId(int id) {
        this.id = id;
    }

    int id;
    public void setCookieHeaders(List<String> cookieHeaders) {
        this.cookieHeaders = cookieHeaders;
    }

    private List<String> cookieHeaders;
    private Boolean isLogin=false;
    public void setLogin(Boolean login) {
        isLogin = login;
    }


    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private  String login;
    private  String password;

    public String getLogin() {
        return login;
    }


    public String getPassword() {
        return password;
    }

    public List<String> getCookies() {
        return this.cookieHeaders;
    }

    @Override
    public int getId() {
        return this.id;
    }


    public boolean isLogoutProperty() {
        return logoutProperty.get();
    }

    public BooleanProperty logoutProperty() {
        return logoutProperty;
    }
    public void setLogoutProperty(boolean logoutProperty) {
        this.logoutProperty.set(logoutProperty);
    }

    private BooleanProperty logoutProperty = new SimpleBooleanProperty(false);

}
