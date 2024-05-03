package Utils;

public enum Server {



    URL("http://localhost:8080"),
    LOGIN("/login"),
    REGISTRATION("/registration"),
    LOGOUT("/logout"),
    PROJECTS("/projects");
    public final String value;
    Server(String value) {
            this.value = value;
        }

}
