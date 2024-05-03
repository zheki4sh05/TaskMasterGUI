module com.example.taskmaster {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires com.google.guice;
    requires json.simple;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;


    opens com.example.taskmaster.Controllers to javafx.fxml, com.google.guice;
    opens DataOperations.ModulesConfigurators to com.google.guice;
    opens SmallCalendarPicker to javafx.fxml;
    opens UIComponents.Calendar to javafx.fxml, com.google.guice;
    opens UIComponents.KanbanBoard to javafx.fxml, com.google.guice;
    exports com.example.taskmaster;
    exports com.example.taskmaster.Controllers;
    exports DataOperations to  com.google.guice;
    exports Data to com.google.guice;
    exports UIComponents.Calendar to com.google.guice;
    exports UIComponents.KanbanBoard to com.google.guice;
    opens DataOperations to javafx.fxml;
    exports Utils to com.google.guice;
    opens Utils to com.google.guice, javafx.fxml;
    opens Data.DTO to com.fasterxml.jackson.databind;
    opens com.example.taskmaster.Controllers.Interfaces to com.google.guice, javafx.fxml;
    exports Data.Settings to com.google.guice;
    opens Data.Settings to com.google.guice, javafx.fxml;

}