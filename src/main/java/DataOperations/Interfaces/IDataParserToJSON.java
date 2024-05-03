package DataOperations.Interfaces;

import Data.UserProject;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;

import java.io.BufferedReader;
import java.util.List;

public interface IDataParserToJSON {
    JSONArray parse(BufferedReader reader);
    JSONArray convertFrom(ObservableList<UserProject> list);
    List<UserProject> convertFrom(JSONArray arr);
}
