package scene.Task.UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import scene.Task.entity.Task;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by hhf on 8/7/17.
 */
public class TaskRemind implements Initializable {
    @FXML
    private Label taskLabel;

    @FXML
    void close(ActionEvent event) {
        Stage stage=(Stage)((Node)event.getTarget()).getScene().getWindow();
        stage.close();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void setTask(Task t){
        taskLabel.setText("The task: "+t.getTask_name()+" has start");

    }
}
