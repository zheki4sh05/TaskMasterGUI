package Data.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class StatusDTO {
    private String name;

    public String getOldName() {
        return oldName;
    }

    @JsonIgnore
    private String oldName;

    public StatusDTO() {
    }

    public int getId() {
        return id;
    }

    private int id;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StatusDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public StatusDTO(int id, String name,String oldName) {
        this.id = id;
        this.name = name;
        this.oldName=oldName;
    }
}
