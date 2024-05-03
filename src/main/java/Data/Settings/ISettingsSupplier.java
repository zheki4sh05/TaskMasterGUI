package Data.Settings;

import com.google.inject.ImplementedBy;
import javafx.collections.ObservableList;
@ImplementedBy(Settings.class)
public interface ISettingsSupplier {
    boolean isSaveMe();
    void setSaveMe(boolean saveMe);
     void addKanban(String name);
    public void deleteKanban(String string);
    ObservableList<String> getKanbans();
    void setGeneration(boolean b);
    boolean isGeneration();
    void setKanbans(ObservableList<String> kanbans);

}
