package Data;

public class UserTaskWrapper {
    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

   private  int projectId;

    public UserTask getUserTask() {
        return userTask;
    }

    private final UserTask userTask;

    public UserTaskWrapper(int projectId, UserTask userTask) {
        this.projectId = projectId;
        this.userTask = userTask;
    }

    @Override
    public String toString() {
        return "UserTaskWrapper{" +
                "projectId=" + projectId +
                ", userTask=" + userTask +
                '}';
    }
}
