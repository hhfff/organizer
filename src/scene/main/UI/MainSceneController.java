package scene.main.UI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import scene.Task.entity.TodayReminder;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Liu Woon Kit on 12/6/2017.
 */
public class MainSceneController implements Initializable{
    private static TodayReminder reminders=new TodayReminder();

    public static TodayReminder getReminders() {
        return reminders;
    }

    public static void setReminders(TodayReminder reminders) {
        MainSceneController.reminders = reminders;
    }

    @FXML
    private AnchorPane content;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        Platform.runLater(() -> homeClick());
    }

    @FXML
    void homeClick() {
        try {content.getChildren().setAll((AnchorPane)FXMLLoader.load(getClass().getResource("../../home/Home.fxml")));} catch (IOException e) {}
    }

    @FXML
    void calendarClick(ActionEvent event) {
        try {content.getChildren().setAll((AnchorPane)FXMLLoader.load(getClass().getResource("../../calendar/UI/CalendarScene.fxml")));} catch (IOException e) {}
    }

    @FXML
    void eventClick(ActionEvent event) {
        try {content.getChildren().setAll((AnchorPane)FXMLLoader.load(getClass().getResource("../../event/UI/EventMain.fxml")));} catch (IOException e) {System.out.println(e);}
    }

    @FXML
    void taskClick(ActionEvent event) {
        try {content.getChildren().setAll((AnchorPane)FXMLLoader.load(getClass().getResource("../../Task/UI/TaskMain.fxml")));} catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void noteClick(ActionEvent event) {
        try {content.getChildren().setAll((AnchorPane)FXMLLoader.load(getClass().getResource("../../note/note.fxml")));} catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void surveyClick(ActionEvent event) {
        try {content.getChildren().setAll((AnchorPane)FXMLLoader.load(getClass().getResource("../../survey/Survey.fxml")));} catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void reportClick(ActionEvent event) {
        if(true){//UserAccess.getUser().getUserType == "staff") {
            try {
                content.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../../report/Report.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void profileClick() {
        try {content.getChildren().setAll((AnchorPane)FXMLLoader.load(getClass().getResource("../../profile/UI/Profile.fxml")));} catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logoutClick(ActionEvent event) {
        reminders.stopAll();

        Stage stage = (Stage) content.getScene().getWindow();
        stage.setUserData(null);
        try {stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../login/UI/LoginScene.fxml"))));} catch (IOException e) {}
    }
}