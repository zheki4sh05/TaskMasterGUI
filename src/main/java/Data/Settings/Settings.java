package Data.Settings;

import Data.Settings.ISettingsSupplier;
import com.google.inject.Singleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@Singleton
public class Settings implements ISettingsSupplier {


    public boolean isSaveMe() {
        return saveMe;
    }

    public void setSaveMe(boolean saveMe) {
        this.saveMe = saveMe;
    }

    private boolean saveMe=false;

    public boolean isGeneration() {
        return generation;
    }

    private boolean generation=false;

    public ObservableList<String> getKanbans() {
        return kanbans;
    }

    public void setKanbans(ObservableList<String> kanbans) {
        this.kanbans = kanbans;
    }

    private ObservableList<String> kanbans = FXCollections.observableArrayList();
    public void addKanban(String name){
        kanbans.add(name);
    }
    public void deleteKanban(String string){
        kanbans.remove(string);
    }
    public void setGeneration(boolean make){
        generation = make;
    }
}
