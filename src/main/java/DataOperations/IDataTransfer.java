package DataOperations;

import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(DataTransfer.class)
public interface IDataTransfer {
    <T> void save(Class<T> tClass, Object o);
    <T> int add(Class<T> tClass, Object o,int id);
    <T> void delete(Class<T> tClass, Object o);
    void setCookie(List<String> list);
    List<String> getCookies();
    boolean checkIfUserLoginExists(String login);
}
