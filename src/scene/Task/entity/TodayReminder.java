package scene.Task.entity;

import resources.database.UserAccess;
import scene.Task.TaskControllerKt;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by hhf on 8/7/17.
 */
public class TodayReminder {
    Calendar today = TaskControllerKt.getStartofToday();

    private ArrayList<Reminder> allReminder= new ArrayList<>();
    public TodayReminder(){
        //get all today task
        Calendar today = TaskControllerKt.getStartofToday();
        ArrayList<Task> databaseTask= TaskControllerKt.getTaskByUser(UserAccess.getUser().getUserID(),TaskControllerKt.getYearDateFormat().format(today.getTime()));
        System.out.println("database time "+databaseTask.size());
        //System.out.println(databaseTask.size());
       for(Task t:databaseTask){
           add(t,".000");
       }
       //ystem.out.println(allReminder.size());
    }

    public void add(Task t,String add){

        if(checkTask(t,add)){
            Reminder r=new Reminder(t,this);
            allReminder.add(r);
            System.out.print(allReminder.size());
        }


    }

    private boolean checkTask(Task task,String add){
        //check date
        Calendar cal=TaskControllerKt.getCalendarByDate(task.getSdate());
        Calendar startofday=TaskControllerKt.getStartofToday();
        if(cal.before(startofday)){
            return false;
        }
        if(task.getStime()!=null){

            LocalTime stime=LocalTime.parse(task.getStime()+add,DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

            LocalTime now=LocalTime.now();
            if(stime.isBefore(now)){
                return false;

            }
        }
        return true;

    }
    public void stopAll(){
        for(Reminder r:allReminder){
            r.stop();

        }
        allReminder.clear();
        System.out.print(allReminder.size());
    }

    public ArrayList<Reminder> getAllReminder() {
        return allReminder;
    }
}
