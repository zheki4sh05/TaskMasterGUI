package Data.DTO;

import java.util.ArrayList;
import java.util.List;

public class ProjectDTO {
    public ProjectDTO() {
    }

    public int getId() {
        return id;
    }
    private int id;
    private String name;
    private String color;
    private List<TaskDTO> taskDTOList;

    public List<StatusDTO> getStatusList() {
        return statusList;
    }

    private List<StatusDTO> statusList;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public ProjectDTO(Long id, String name, String color, List<TaskDTO> taskDTOS, List<StatusDTO> stList) {
        this.id = Math.toIntExact(id);
        this.name = name;
        this.color = color;
        this.taskDTOList = taskDTOS;
        this.statusList = stList;
    }
    public List<TaskDTO> getTaskDTOList() {
        return taskDTOList;
    }
}
