package DataOperations.Interfaces;

import org.json.simple.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public interface IMakeConnection {
    String loadUserData(List<String> cookies);

}
