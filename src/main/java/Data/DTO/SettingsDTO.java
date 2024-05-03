package Data.DTO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class SettingsDTO {
    private boolean generation;
    private boolean saveMe;

    public List<String> getKanbans() {
        return kanbans;
    }

    public void setKanbans(List<String> kanbans) {
        this.kanbans = kanbans;
    }

    private List<String> kanbans;

    public SettingsDTO() {
    }

    public boolean isGeneration() {
        return generation;
    }

    public void setGeneration(boolean generation) {
        this.generation = generation;
    }

    public boolean isSaveMe() {
        return saveMe;
    }

    public void setSaveMe(boolean saveMe) {
        this.saveMe = saveMe;
    }

}
