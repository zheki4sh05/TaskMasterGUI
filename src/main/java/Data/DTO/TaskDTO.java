package Data.DTO;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskDTO {
    public TaskDTO() {
    }

    public int getId() {
        return id;
    }

    private int id;
    private String name;
    private String desc;
    private String status;
    private String datefrom;
    private String dateto;
    private List<SubTaskDTO> subTaskDTOS = new ArrayList<>();
    public TaskDTO(int id,String name, String desc, String status, String datefrom,String dateto, List<SubTaskDTO> subTaskDTOList) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.status = status;
        this.datefrom = datefrom;
        this.dateto = dateto;
        this.subTaskDTOS = subTaskDTOList;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public List<SubTaskDTO> getSubTaskDTOS() {
        return subTaskDTOS;
    }

    public void setSubTaskDTOS(List<SubTaskDTO> subTaskDTOS) {
        this.subTaskDTOS = subTaskDTOS;
    }

    public String getDatefrom() {
        return datefrom;
    }

    public void setDatefrom(String datefrom) {
        this.datefrom = datefrom;
    }

    public String getDateto() {
        return dateto;
    }

    public void setDateto(String dateto) {
        this.dateto = dateto;
    }

}
