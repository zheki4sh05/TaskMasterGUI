package UIComponents.KanbanBoard;

public class BoardColumnData {
    private String name;

    public String getOldName() {
        return oldName;
    }

    private String oldName;
    private int order;

    public BoardColumnData(String name, int order) {
        this.name = name;
        this.order = order;
    }
    public BoardColumnData(String name, int order,String oldName) {
        this.name = name;
        this.order = order;
        this.oldName = oldName;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "BoardColumnData{" +
                "name='" + name + '\'' +
                ", order=" + order +
                '}';
    }
}
