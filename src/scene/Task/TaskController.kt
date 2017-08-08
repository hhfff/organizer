package scene.Task

import javafx.fxml.FXMLLoader
import javafx.stage.Stage
import resources.database.entity.User
import resources.database.entity.UserController
import scene.Task.entity.Task
import scene.Task.entity.deleteTask
import scene.Task.entity.getTaskByUser
import sun.plugin2.liveconnect.JavaClass
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*



/**
 * Created by hhf on 6/29/17.
 */
//save task to database


//priority--------
enum class Priority(val index:Int){
    NONE(0){
        override fun toStr(): String="None"
        override fun getColor(): String ="#3498db"

    },
    LOW(1){
        override fun toStr(): String="Low"
        override fun getColor(): String ="#2ecc71"
    },
    MEDIUM(2){
        override fun toStr(): String="Medium"
        override fun getColor(): String ="#f1c40f"
    },
    HIGH(3){
        override fun toStr(): String="High"
        override fun getColor(): String ="#e74c3c"
    };
    abstract fun toStr():String
    abstract fun getColor():String
}
fun intToPriority(value:Int):Priority{
    when(value){
        1->return Priority.LOW
        2->return Priority.MEDIUM
        3->return Priority.HIGH
    }
    return Priority.NONE
}
fun priorityToInt(priority: Priority)=priority.ordinal

//repeat--------
enum class Repeat(val index:Int){
    NONE(0){
        override fun toStr(): String="None"
    },
    DAILY(1){
        override fun toStr(): String="Daily"
    },
    WEEKLY(2){
        override fun toStr(): String="Weekly"
    };
    abstract fun toStr():String
}
fun repeatToInt(repeat: Repeat)=repeat.ordinal
fun intToRepeat(value:Int):Repeat{
    when(value){
        1-> return Repeat.DAILY
        2->return Repeat.WEEKLY
    }
    return Repeat.NONE

}
//------------
//task
fun addTask(task_name:String, start: String?,startTime:String?, end: String?,endTime:String?, description:String?, priorityInt: Int,repeatInt: Int, location:String,event:String?,user:String){

    val task= Task(-1,task_name,
            start,
            startTime,
            end,
            endTime,
            description,
            intToPriority(priorityInt),
            intToRepeat(repeatInt),
            location,
            event,
            user)
    task.save()

}
fun changeTask(task: Task){
    scene.Task.entity.changeTask(task)
}
fun setComplete(task:Task){
    scene.Task.entity.setComplete(task)

}
fun deleteTaskMain(t:Task){
    deleteTask(t)

}
fun getTaskByUser(userId:String):ArrayList<Task>{
    return getTaskByUser(userId)

}
fun getTaskByUser(userId:String,today:String):ArrayList<Task>{
    return getTaskByUser(userId,today)

}
fun getTaskByUser(userId:String,start: String,end:String):ArrayList<Task>{
    return getTaskByUser(userId,start,end)

}

//event task
fun getTaskByEvent(eventID:Int): ArrayList<Task> {
    return scene.Task.entity.getTaskByEvent(eventID);
}



//----------task memeber
fun getCreator(creatorID:String):String{
    return UserController.getCreatorName(creatorID);

}
fun retrieveTaskMember(taskID:Int):ArrayList<User>{
    return UserController.retrieveTaskMember(taskID)

}
fun addmember(task: Task,userID:String){
    UserController.addTaskMember(task,userID)
}
fun deleteMember(taskId:Int,userId: String){
    UserController.deleteTaskMember(taskId,userId);

}

//calendar relate
fun getCalendarByDate(date:String):Calendar{
    val data=date.split("-")
    val cal=Calendar.getInstance()
    cal.set(Integer.parseInt(data[0]),
            Integer.parseInt(data[1])-1,
            Integer.parseInt(data[2]),0,0,0)
    return cal
}
fun getStartofToday():Calendar{
    val today=Calendar.getInstance()
    today.set(Calendar.HOUR_OF_DAY,0)
    today.set(Calendar.MINUTE, 0)
    today.set(Calendar.SECOND, 0)
    return today
}
fun getDayEnd(date:Calendar):Calendar{
    date.set(Calendar.HOUR_OF_DAY,23)
    date.set(Calendar.MINUTE, 59)
    date.set(Calendar.SECOND, 59)
    return date
}
internal var yearDateFormat = SimpleDateFormat("yyyy-MM-dd")
internal var dateFormatYear = SimpleDateFormat("dd-MM-yyyy")
var YearFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
var formatterYear = DateTimeFormatter.ofPattern("dd-MM-yyyy")
var timeFormatter=DateTimeFormatter.ofPattern("HH:mm")
var datetimeFromatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")


