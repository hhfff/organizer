package scene.Task.entity;

import resources.database.UserAccess;
import scene.Task.TaskControllerKt;

import java.time.LocalDate;
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

       //ystem.out.println(allReminder.size());
    }
    public  void loadDataBase(){
        Calendar today = TaskControllerKt.getStartofToday();
        ArrayList<Task> databaseTask= TaskControllerKt.getTaskByUser(UserAccess.getUser().getUserID(),TaskControllerKt.getYearDateFormat().format(today.getTime()));
        //System.out.println(databaseTask.size());
        for(Task t:databaseTask){
            add(t,".000");
        }

    }

    public void add(Task t,String add){
        for(Reminder r:allReminder){
            if(t.getId()==r.getTask().getId()){
                r.stop();
                allReminder.remove(r);

                System.out.println(r.getTask().getId()+" remove");
                break;

            }
        }

        if(checkTask(t,add)){
            Reminder r=new Reminder(t,this);
            allReminder.add(r);
            System.out.println(t.getId()+" add");
        }


    }

    private boolean checkTask(Task task,String add){
        //check date

        DateTimeFormatter dtf=TaskControllerKt.getYearFormatter();
        LocalDate sDate=LocalDate.parse(task.getSdate(),dtf);
        LocalDate today=LocalDate.now();

        if(sDate.isBefore(today)){
            return false;
        }
        if(task.getStime()!=null){

            LocalTime stime=LocalTime.parse(task.getStime()+add,DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

            LocalTime now=LocalTime.now();
            if(stime.isBefore(now)){
                return false;

            }
        }else{return false;}
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
