package resources.database.entity;


import resources.database.DB;
import resources.database.UserAccess;
import scene.Task.entity.Task;
import scene.main.UI.MainSceneController;

import javax.sql.rowset.CachedRowSet;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Liu Woon Kit on 12/6/2017.
 */
public class User {
    private String userID, name;
    //private String userType;
    private GregorianCalendar birthDate;
    private String email;
    private int hpNumber;
    private char gender;

    public User() {

    }

    public User(String userID, String name, Date birthDate, String email, int hpNumber, char gender) {
        this.userID = userID;
        this.name = name;
        this.birthDate = new GregorianCalendar();
        this.birthDate.setTime(birthDate);
        this.email = email;
        this.hpNumber = hpNumber;
        this.gender = gender;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GregorianCalendar getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(GregorianCalendar birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getHpNumber() {
        return hpNumber;
    }

    public void setHpNumber(int hpNumber) {
        this.hpNumber = hpNumber;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getFullGender(){
        switch(gender){
            case 'm':return "Male";
            case 'f':return "Female";
            default:return "Alien";
        }
    }

    public ArrayList<Task> getTask(){
        ArrayList<Task> taskArr=new ArrayList<>();


        return taskArr;


    }

    @Override
    public String toString() {
        return userID+"\t"+name;
    }

    public static User getUserInfomation(String userID){
        CachedRowSet rs=DB.read("select * from User where userID='"+userID+"'");
        User user=new User();
        try {
            while(rs.next()){
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setUserID(rs.getString("userID"));
                Date date=rs.getDate("birthDate");
                GregorianCalendar gc=new GregorianCalendar();
                gc.setTime(date);
                user.setBirthDate(gc);
                String gender=rs.getString("gender");
                char c=gender.charAt(0);
                user.setGender(c);
                user.setHpNumber(rs.getInt("hpNumber"));


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;

    }

    public static String[] getAllUser(){
        List<String> data=new ArrayList<>();
        CachedRowSet rs=DB.read("select userID,name from User");
        try {
            while(rs.next()){
                data.add(rs.getString("userID")+" "+rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data.toArray(new String[data.size()]);
    }

    public static boolean verifyUser(String userID, String password) {
        CachedRowSet rs = DB.read("SELECT userID, name, birthDate, email, hpNumber, gender FROM User WHERE userID='" + userID + "' && password='" + password + "'");

        if(rs==null||rs.size() == 0)
            return false;

        try {
            if(rs.next()) {
                new UserAccess(new User(rs.getString("userID"), rs.getString("name"), rs.getDate("birthDate"), rs.getString("email"), rs.getInt("hpNumber"), (rs.getString("gender")).charAt(0)));
            }
        } catch (SQLException e) {e.printStackTrace();}
        MainSceneController.getReminders().loadDataBase();

        return true;
    }
    public static ArrayList<User> getEventMember(int id){
        ArrayList<User> arr=new ArrayList<>(  );
        CachedRowSet rs=DB.read( "select * from User u inner join UserEvents ue on u.userID=ue.userID where ue.eventID="+id );

        try {
            while(rs.next()){
                User user=new User();
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setUserID(rs.getString("userID"));
                Date date=rs.getDate("birthDate");
                GregorianCalendar gc=new GregorianCalendar();
                gc.setTime(date);
                user.setBirthDate(gc);
                String gender=rs.getString("gender");
                char c=gender.charAt(0);
                user.setGender(c);
                user.setHpNumber(rs.getInt("hpNumber"));
                arr.add( user );


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arr;

    }


//task member relate
    public static ArrayList<User> retrieveTaskMember(int taskID ){
        CachedRowSet result=DB.read("SELECT u.userID,u.name FROM User u inner join taskUser tu on u.userID=tu.userID where tu.taskID="+taskID);
        ArrayList<User> data=new ArrayList<User>();
        try {
            while (result.next()){
                User user=new User();
                user.userID=result.getString("userID");
                user.name=result.getString("name");
                data.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
    public static void deleteTaskMember(int taskId,String userID){
        DB.update(String.format("delete from taskUser where taskID=%d and userID='%s'",taskId,userID));
    }
    public static void addTaskMember(int taskId,String userID){
        DB.update(String.format("insert into taskUser values('%s',%d)",userID,taskId));
    }
    public static String getCreatorName(String creatorID){

        CachedRowSet result=DB.read("SELECT DISTINCT u.name from User u " +
                "inner join task t ON " +
                "t.creatorID=u.userID " +
                "where t.creatorID='"+creatorID+"'");
        String name="";

        try {
            while(result.next()){
                name= result.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }
}