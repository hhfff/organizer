package scene.Task.UI;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import org.controlsfx.control.textfield.TextFields;
import resources.database.UserAccess;
import resources.database.entity.UserController;
import scene.Task.TaskControllerKt;
import scene.Task.entity.Task;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by hhf on 8/5/17.
 */

public class AddUserController implements Initializable{
    private boolean canSave=false;
    private Task task;
    private TaskDetailsController taskDetailsController;

    @FXML
    private JFXTextField adminNoTF;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        RequiredFieldValidator emptyField=new RequiredFieldValidator();
        emptyField.setMessage("Required");

        adminNoTF.getValidators().add(emptyField);
        adminNoTF.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    if(adminNoTF.validate()){
                        canSave=true;
                    }else{
                        canSave=false;

                    }
                }


            }
        });
        String[] suggestions= UserController.getAllUser();
        TextFields.bindAutoCompletion(adminNoTF,suggestions);


    }
    @FXML
    void add(ActionEvent event) {

        if(canSave) {
            String text=adminNoTF.getText();
            String[] data = text.split(" ");
            if(!UserAccess.getUser().getUserID().equals(data)){
                TaskControllerKt.addUser(task,data[0]);
                taskDetailsController.updateMemberList();

            }
            else{
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Same as task creator");
                alert.show();
            }


        }

    }

    public void setTaskDetailsController(TaskDetailsController taskDetailsController) {
        this.taskDetailsController = taskDetailsController;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
