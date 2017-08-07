package scene.Task.entity;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import scene.Task.UI.TaskDetailsController;
import scene.Task.UI.TaskRemind;

import java.io.IOException;
import java.util.Timer;

/**
 * Created by hhf on 8/7/17.
 */
public class Reminder {

    private Timeline timer;
    private long minutetoMilli=60*1000;
    private long secToMillis=1000;
    private long before=10;
    public Reminder(){

       /* timer=new Timer();
        Calendar cal= TaskControllerKt.getStartofToday();
        cal.set(Calendar.HOUR_OF_DAY,5);
        cal.set(Calendar.MINUTE,54);





        timer.schedule(reminderTask,cal.getTimeInMillis()-System.currentTimeMillis());*/

    }
    public Reminder(Task task,TodayReminder todayReminder){

        timer=new Timeline(new KeyFrame(Duration.millis(task.getStartDateTime().getTimeInMillis()-System.currentTimeMillis()),event -> {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("../../Task/UI/TaskRemind.fxml"));
            try {
                AnchorPane ap = loader.load();
                TaskRemind tdc = loader.getController();
                tdc.setTask(task);
                Stage stage=new Stage();
                stage.setTitle("Reminder");
                stage.setScene(new Scene(ap));
                stage.show();
                todayReminder.getAllReminder().remove(this);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        timer.play();






    }
    public void stop(){
        timer.stop();
    }
    public void display(Task t){


    }
}
