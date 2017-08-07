package scene.Task.UI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import resources.database.UserAccess;
import scene.Task.TaskControllerKt;
import scene.Task.entity.Task;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by hhf on 7/4/17.
 */
public class TaskMainController implements Initializable {
    UserAccess user=new UserAccess();
    @FXML
    private JFXComboBox<String> dateSelect;
    @FXML
    JFXCheckBox personalCheckBox;

    @FXML
    JFXCheckBox eventCheckBox;

    @FXML
    private VBox taskContainer;

    @FXML
    private AnchorPane taskDetailsContainer;
    @FXML

    private JFXButton addTaskButton;
    private ArrayList<Task> taskArr;

    private String selectCombobox="All";

    private TaskMainController tmc=this;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        updateTaskListContontainer();

        //add add icon
        //addTaskButton.heightProperty().;
        Image plusImage=new Image(getClass().getResourceAsStream("/resources/images/icon/add-square-button.png"));
        ImageView plusImageView=new ImageView(plusImage);
        //plusImageView.fitHeightProperty().bind(addTaskButton.heightProperty());
        //plusImageView.fitWidthProperty().bind(addTaskButton.widthProperty());
        //plusImageView.setFitHeight(36);
        //plusImageView.setFitWidth(36);
        addTaskButton.setStyle("-fx-padding: 0px;");
        addTaskButton.setGraphic(plusImageView);
        //addTaskButton.setStyle("-fx-background-radius: 5em;");

        ObservableList<String> comboBoxString = FXCollections.observableArrayList();
        comboBoxString.add("All");
        comboBoxString.add("Today");
        comboBoxString.add("3 Days");
        comboBoxString.add("1 week");
        dateSelect.setItems(comboBoxString);
        dateSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {


            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                    switch (newValue) {
                        case "Today":
                            selectCombobox="Today";
                            break;
                        case "3 Days":
                            selectCombobox="3 Days";
                            break;
                        case "1 week":
                            selectCombobox="1 week";
                            break;
                        default:
                            selectCombobox="All";
                            break;
                    }
                    updateTaskListContontainer();
                }
        });
        dateSelect.getSelectionModel().selectFirst();
    }

    @FXML
    void addTask(ActionEvent event) {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("AddTask.fxml"));

        try {
            Parent addTaskScene=loader.load();

            Stage stage=new Stage(StageStyle.UNDECORATED);
            stage.initOwner(((Node)(event.getTarget())).getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);

            stage.setTitle("ADD TASK");
            stage.setScene(new Scene(addTaskScene));
            stage.showAndWait();

            updateTaskListContontainer();



        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void updateTaskListContontainer() {
        if(taskArr!=null) taskArr.clear();
        taskContainer.getChildren().clear();
        String id=user.getUser().getUserID();
        switch(selectCombobox){
            case "Today":
                Calendar today = TaskControllerKt.getStartofToday();
                String date = TaskControllerKt.getYearDateFormat().format(today.getTime());
                taskArr=TaskControllerKt.getTaskByUser(id,date);
                break;
            case "3 Days":
                Calendar threeToday = TaskControllerKt.getStartofToday();
                String threeDate = TaskControllerKt.getYearDateFormat().format(threeToday.getTime());
                threeToday.add(Calendar.DATE,2);
                String threeDate2 = TaskControllerKt.getYearDateFormat().format(threeToday.getTime());
                taskArr=TaskControllerKt.getTaskByUser(id,threeDate,threeDate2);
                break;
            case "1 week":
                Calendar weekToday = TaskControllerKt.getStartofToday();
                String weekDate = TaskControllerKt.getYearDateFormat().format(weekToday.getTime());
                weekToday.add(Calendar.DATE,6);
                String weekDate2 = TaskControllerKt.getYearDateFormat().format(weekToday.getTime());
                taskArr=TaskControllerKt.getTaskByUser(id,weekDate,weekDate2);
                break;
            default:
                taskArr = TaskControllerKt.getTaskByUser(id);
                break;
        }
        if (!taskArr.isEmpty()){
            Collections.sort(taskArr);
            String checkDate=taskArr.get(0).getSdate();
            boolean isFirstLoop=true;//firstLoop
            for(int i=0;i<taskArr.size();i++){
                Task t=taskArr.get(i);
                FXMLLoader loader=new FXMLLoader(getClass().getResource("TaskBar.fxml"));
                try {
                    Parent node=loader.load();
                    TaskBarController tc=loader.getController();
                    if(isFirstLoop) {

                        tc.setTaskInfo(t, true);
                        isFirstLoop=false;
                    }else{
                        if (t.getSdate() != null && t.getSdate().equals(checkDate)) {
                            tc.setTaskInfo(t, false);
                        } else {
                            tc.setTaskInfo(t, true);
                            checkDate = t.getSdate();
                        }
                    }
                    node.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            MouseButton btn=event.getButton();
                            if(btn==MouseButton.PRIMARY) {
                                FXMLLoader taskDetailSceneLoader = new FXMLLoader(getClass().getResource("TaskDetails.fxml"));
                                try {
                                    AnchorPane ap = taskDetailSceneLoader.load();
                                    TaskDetailsController tdc = taskDetailSceneLoader.getController();
                                    tdc.setTask(tc.getTask(),tmc);
                                    taskDetailsContainer.getChildren().setAll(ap);

                                } catch (IOException e) {
                                   e.printStackTrace();
                                }
                            }
                            else if(btn==MouseButton.SECONDARY){
                                 Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Comfirmation");
                                 alert.setHeaderText("Are you sure to delete");
                                Optional<ButtonType> rs=alert.showAndWait();
                               if(rs.get()==ButtonType.OK) {
                                   //delete timer if exist


                                TaskControllerKt.deleteTaskMain(tc.getTask());
                                taskDetailsContainer.getChildren().clear();
                                updateTaskListContontainer();
                            }
                        }

                    }
                });
                taskContainer.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        }

    }
    private void retriveTaskList(String start,String end){


    }

    private void displayTask(){


    }

    public AnchorPane getTaskDetailsContainer() {
        return taskDetailsContainer;
    }
}
