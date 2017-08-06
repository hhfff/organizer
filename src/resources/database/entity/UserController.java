package resources.database.entity;

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
}
