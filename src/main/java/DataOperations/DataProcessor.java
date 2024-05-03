package DataOperations;
import Data.UserProject;
import DataOperations.Interfaces.IDataParserToJSON;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;

import java.io.BufferedReader;
import java.util.List;

public class DataProcessor implements IDataParserToJSON {
    @Override
    public JSONArray parse(BufferedReader reader) {
        return null;
    }
    @Override
    public JSONArray convertFrom(ObservableList<UserProject> list) {
        return null;
    }

    @Override
    public List<UserProject> convertFrom(JSONArray arr) {
        return null;
    }

}
