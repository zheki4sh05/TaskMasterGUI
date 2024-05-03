package Data.DTO;

public class SubTaskDTO {
    public SubTaskDTO() {
    }

    public int getId() {
        return id;
    }

    private int id;

    public void setName(String name) {
        this.name = name;
    }

    private  String name;

    public void setDone(boolean done) {
        isDone = done;
    }

    private  boolean isDone;
    public SubTaskDTO(int id,String name, boolean isDone) {
        this.id = id;
        this.name = name;
        this.isDone = isDone;
    }

    public String getName() {
        return name;
    }

    public boolean isDone() {
        return isDone;
    }
}
