package Data;

import com.example.taskmaster.DataModel;
import com.google.inject.ImplementedBy;
import javafx.beans.property.BooleanProperty;

import java.util.List;
@ImplementedBy(UserInfo.class)
public interface UserInfoSupplier {
     void setCookieHeaders(List<String> cookieHeaders);
     void setLogin(Boolean login);
     void setLogin(String login);

     void setPassword(String password);


     String getLogin();

     String getPassword();
     List<String> getCookies();

    int getId();

     void setId(int id);

     boolean isLogoutProperty();

     BooleanProperty logoutProperty() ;
     void setLogoutProperty(boolean logoutProperty) ;
}
