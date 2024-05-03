package SmallCalendarPicker;

import DataOperations.Interfaces.IDataModelSupplier;
import com.example.taskmaster.Controllers.IChildPage;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.*;

public class CalendarPickerController implements IChildPage, Initializable {
    @Inject
    private IDataModelSupplier dataModel;

    private ZonedDateTime dateFocus;
    private ZonedDateTime today;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane CalendarActivity;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendarActivity();
    }

    @FXML
    private void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        CalendarActivity.getChildren().clear();
        drawCalendarActivity();
    }

    @FXML
    private void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        CalendarActivity.getChildren().clear();
        drawCalendarActivity();
    }

    private void drawCalendarActivity(){

        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double CalendarActivityWidth = CalendarActivity.getPrefWidth();
        double CalendarActivityHeight = CalendarActivity.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = CalendarActivity.getHgap();
        double spacingV = CalendarActivity.getVgap();

        //List of activities for a given month
        //Map<Integer, List<CalendarActivity>> CalendarActivityMap = getCalendarActivityActivitiesMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();
        //Check for leap year
        if(dateFocus.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1,0,0,0,0,dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                Button button = new Button();
                double rectangleWidth =(CalendarActivityWidth/7) - strokeWidth - spacingH;
                button.setPrefWidth(40);
                double rectangleHeight = (CalendarActivityHeight/6) - strokeWidth - spacingV;
                button.setPrefHeight(40);

                button.setStyle("-fx-background-radius:100;-fx-background-color: white;-fx-border-color:rgb(82, 95, 110);-fx-border-radius:100;");
                // stackPane.getChildren().add(rectangle);
                //button.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(100), BorderWidths.DEFAULT)));

                int calculatedDate = (j+1)+(7*i);
                if(calculatedDate > dateOffset){
                    int currentDate = calculatedDate - dateOffset;
                    if(currentDate <= monthMaxDate){
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        //stackPane.getChildren().add(date);
                        button.setText(date.getText());
                        // List<CalendarActivity> CalendarActivityActivities = CalendarActivityMap.get(currentDate);
                        // if(CalendarActivityActivities != null){
                        //createCalendarActivity(CalendarActivityActivities, rectangleHeight, rectangleWidth, stackPane);
                        //}
                        CalendarActivity.getChildren().add(button);
                    }
                    if(today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate){
                        button.setStyle("-fx-background-radius:100; -fx-background-color: rgb(82, 95, 110); -fx-text-fill:white;");

                    }
                }

            }
        }
    }

}