package Data;

public class ProjectData {
    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private final String name;
    private final String color;
    public ProjectData(String name, String color){
        this.name =name;
        this.color=color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getId() {
        return id;
    }
}
