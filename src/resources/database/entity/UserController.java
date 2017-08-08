package resources.database.entity;

import scene.Task.entity.Task;

import java.util.ArrayList;

/**
 * Created by hhf on 8/5/17.
 */
public class UserController {
    public static User getUserInformation(String id){
            return User.getUserInfomation(id);
    }
    public static String[] getAllUser(){return User.getAllUser();}
    public static ArrayList<User> getEventMember(int eventID){return User.getEventMember( eventID );}


    public static ArrayList<User> retrieveTaskMember(int taskID ){return User.retrieveTaskMember(taskID);}
    public static void deleteTaskMember(int taskId,String userID){
         User.deleteTaskMember(taskId,userID);
    }
    public static void addTaskMember(Task task, String userID){

       User.addTaskMember(task.getId(),userID);
    }
    public static String getCreatorName(String creatorID){
        return User.getCreatorName(creatorID);
    }
}
