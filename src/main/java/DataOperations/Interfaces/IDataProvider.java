package DataOperations.Interfaces;

import Data.UserProject;
import DataOperations.DataProvider;
import com.example.taskmaster.DataModel;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.Map;

@ImplementedBy(DataModel.class)
@FunctionalInterface
public interface IDataProvider<T,k> {
    void setData(Map<T,k> list);
}
