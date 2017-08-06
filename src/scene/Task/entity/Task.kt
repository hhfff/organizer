package scene.Task.entity

import javafx.scene.control.Alert
import resources.database.DB
import resources.database.entity.User
import scene.Task.*
import java.util.*
import javax.sql.rowset.CachedRowSet
import kotlin.collections.ArrayList

/**
 * Created by hhf on 6/28/17.
 */

fun getTaskByEvent(eventID:Int): ArrayList<Task> {
    println(eventID);
    val rs=DB.read("select * from task where eventID='$eventID' and complete=0")
    return processResultSet(rs);
}

fun getTaskByUser(userID:String):ArrayList<Task>{
    val rs=DB.read("select * from task where creatorID='$userID' and complete=0")
    //retrive personal first
    val data= processResultSet(rs);
    val ors=DB.read("SELECT * FROM task t inner join taskUser tu on t.id=tu.taskID where tu.userID='$userID'")
    data.addAll(processResultSet(ors))
    return data
}
fun getTaskByUser(userID:String,today:String):ArrayList<Task>{
    val result=DB.read("select * from task where creatorID='$userID' and start ='$today' and complete=0")
    val data= processResultSet(result);

    val ors=DB.read("SELECT * FROM task t inner join taskUser tu on t.id=tu.taskID where tu.userID='$userID' and t.start ='$today' and complete=0")
    data.addAll(processResultSet(ors))
    return data
}
fun getTaskByUser(userID:String,startDate:String,endDate:String):ArrayList<Task>{


    val result=DB.read("select * from task where creatorID='$userID' and start between '$startDate' and '$endDate' and complete=0")
    val data= processResultSet(result);
    val ors=DB.read("SELECT * FROM task t inner join taskUser tu on t.id=tu.taskID where tu.userID='$userID' and start between '$startDate' and '$endDate' and complete=0")
    data.addAll(processResultSet(ors))
    return data
}
private fun processResultSet(result:CachedRowSet):ArrayList<Task>{
    val tastArr=ArrayList<Task>();
    try {
        while (result.next()) {
            val id = result.getInt("id")
            val taskName: String? = result.getString("name")
            val start = result.getString("start")
            val stime = result.getString("startTime")
            val end = result.getString("end")
            val etime = result.getString("endTime")
            val desc: String? = result.getString("taskDesc")


            val repeat = intToRepeat(result.getInt("repeatType"))
            val priority = intToPriority(result.getInt("priority"))

            val location: String? = result.getString("location")
            val eventId: String? = result.getString("eventID")
            val creatorID = result.getString("creatorID")
            tastArr.add(Task(id,
                    taskName,
                    start, stime,
                    end, etime,
                    desc,
                    priority,
                    repeat,
                    location,
                    eventId,
                    creatorID))
        }
        //get from data base
    }catch (e:NullPointerException){
        val alert=Alert(Alert.AlertType.ERROR)
        alert.title = "Error"
        alert.headerText="Server not responding"

        alert.showAndWait()

    }

    return tastArr


}
fun deleteMember(taskId:Int,userId: String){
    DB.update("delete from taskUser where taskID=$taskId and userID='$userId'")
}


fun deleteTask(t:Task){
    val id=t.id
    DB.update("delete  from task where id=$id")

}

fun retrieveTaskMember(taskID:Int):ArrayList<User>{
    val result=DB.read("SELECT u.userID,u.name FROM User u inner join taskUser tu on u.userID=tu.userID where tu.taskID=$taskID")
    val data=ArrayList<User>()
    while (result.next()){
        val user=User()
        user.userID=result.getString("userID")
        user.name=result.getString("name")
        data.add(user);
    }
    return data
}

fun addUser(task: Task,userID:String){


    DB.update("insert into taskUser values('$userID',${task.id}) ")


}
fun getCreator(creatorID: String):String{

    val result=DB.read("SELECT DISTINCT u.name from User u " +
            "inner join task t ON " +
            "t.creatorID=u.userID " +
            "where t.creatorID='$creatorID'")
    var name:String=""
    try{
        while(result.next()){
            name= result.getString("name")
        }
    }catch (e:NullPointerException){}
    return name;


}

fun setComplete(task: Task){

    var query=""
    if(task.repeat==Repeat.NONE)  query="update task set complete=1 where id=${task.id}"
    else if(task.repeat==Repeat.DAILY){
        val cal=scene.Task.getCalendarByDate(task.sdate+"")
        cal.add(Calendar.DATE,1)
        query="update task set start='${yearDateFormat.format(cal.time)}' where id=${task.id}"

    }else if(task.repeat==Repeat.WEEKLY){
        val cal=scene.Task.getCalendarByDate(task.sdate+"")
        cal.add(Calendar.DATE,7)
        query="update task set start='${yearDateFormat.format(cal.time)}' where id=${task.id}"

    }

    if(!query.equals("")) DB.update(query)
}
fun changeTask(task: Task){
    task.change()


}

class Task(var id:Int?,var task_name:String?, var sdate:String?,var stime:String?, var edate:String?,var etime:String?,var description:String?, var priority: Priority, var repeat: Repeat, var location:String?, var event:String?, var creator:String):Comparable<Task>{
    constructor():this(null,null,null,null, null, null,null,Priority.NONE,Repeat.NONE,null,null,"")

    fun save():Unit{
        var queryCol="INSERT INTO task("
        var queryVal="VALUES("
        if(task_name!=null){
            queryCol+="name,"
            queryVal+="'$task_name',"
        }
        if(sdate!=null){
            queryCol+="start,"
            queryVal+="'$sdate',"
        }
        if(stime!=null){
            queryCol+="startTime,"
            queryVal+="'$stime',"
        }
        if(edate!=null){
            queryCol+="end,"
            queryVal+="'$edate',"
        }
        if(etime!=null){
            queryCol+="endTime,"
            queryVal+="'$etime',"
        }
        if(description!=null){
            queryCol+="taskDesc,"
            queryVal+="'$description',"

        }
        if(location!=null){
            queryCol+="location,"
            queryVal+="'$location',"
        }

        if(event!=null){
            queryCol+="eventID,"
            queryVal+="'$event',"
        }
        queryCol+="priority,repeatType,creatorID"
        queryVal+="${priority.ordinal},${repeat.ordinal},'$creator'"

        queryCol+=")"
        queryVal+=")"


        DB.update(queryCol+queryVal)
    }
    fun change(){
        var queryCol="update task set "
        if(task_name!=null){
            queryCol+="name='$task_name',"

        }
        if(sdate!=null){
            queryCol+="start='$sdate',"

        }
        if(stime!=null){
            queryCol+="startTime='$stime',"

        }
        if(edate!=null){
            queryCol+="end='$edate',"

        }
        if(etime!=null){
            queryCol+="endTime='$etime',"

        }
        if(description!=null){
            queryCol+="taskDesc='$description',"

        }
        if(location!=null){
            queryCol+="location='$location',"

        }

        //queryCol.substring(0,queryCol.length-2)
        queryCol+="priority=${priority.index},repeatType=${repeat.index}"

        queryCol+=" where id=$id"



        DB.update(queryCol)

    }

    override fun compareTo(other: Task): Int {
        if(sdate!=null&&other.sdate!=null) {
            val bd=sdate+""
            val date=scene.Task.getCalendarByDate(bd)

            val od=other.sdate+""
            val otherDate=scene.Task.getCalendarByDate(od)

            if (otherDate.after(date)) return -1
            else if (otherDate.before(date)) return 1
            else {
                if (other.priority.ordinal > priority.ordinal) return 1
                else if (other.priority.ordinal < priority.ordinal) return -1
                else return 0
            }
        }
        return 1
    }



}