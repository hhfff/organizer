package scene.Task.UI;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import resources.database.UserAccess;
import resources.database.entity.User;
import scene.Task.Priority;
import scene.Task.Repeat;
import scene.Task.TaskControllerKt;
import scene.Task.entity.Reminder;
import scene.Task.entity.Task;
import scene.main.UI.MainSceneController;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by hhf on 7/5/17.
 */
public class TaskDetailsController implements Initializable{
    private Task task;

    @FXML
    private AnchorPane taskInfoPane;

    @FXML
    private AnchorPane memberPane;


    @FXML
    private JFXTextField taskTaskField;

    @FXML
    private AnchorPane startButton;

    @FXML
    private AnchorPane startdateTF;

    @FXML
    private JFXDatePicker startDateTF;

    @FXML
    private JFXTimePicker startTimeTF;

    @FXML
    private AnchorPane endButton;

    @FXML
    private JFXDatePicker endDateTF;

    @FXML
    private JFXTimePicker endTimeTF;
    @FXML
    private JFXComboBox<String> repeatComboBox;

    @FXML
    private JFXComboBox<String> priorityComboBox;

    private TaskMainController tc;

    @FXML
    private JFXButton completeButton;



    @FXML
    private JFXTextArea descTextArea;

    @FXML
    private JFXTextField locationTextField;

    @FXML
    private JFXButton changeButton;
    private Task tempTask;

    @FXML
    private JFXButton addUserButton;

    @FXML
    private Label creatorLabel;
    private boolean canComplete=true;
    private boolean memberViewClickable=false;


    @FXML
    private JFXListView<User> memberListView;
    private ObservableList<User> obUserList;
    private boolean canSave=false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startTimeTF.setIs24HourView(true);
        endTimeTF.setIs24HourView(true);

        ObservableList<String> priority= FXCollections.observableArrayList(Priority.NONE.name(),Priority.LOW.name(),Priority.MEDIUM.name(),Priority.HIGH.name());
        priorityComboBox.setItems(priority);
        priorityComboBox.getSelectionModel().selectFirst();

        ObservableList<String> repeat=FXCollections.observableArrayList(Repeat.NONE.name(),Repeat.DAILY.name(),Repeat.WEEKLY.name());
        repeatComboBox.setItems(repeat);
        repeatComboBox.getSelectionModel().selectFirst();

        Image plusImage=new Image(getClass().getResourceAsStream("/resources/images/icon/add-square-button.png"));
        ImageView plusImageView=new ImageView(plusImage);

        addUserButton.setStyle("-fx-padding: 0px;");
        addUserButton.setGraphic(plusImageView);
        RequiredFieldValidator emptyField=new RequiredFieldValidator();
        emptyField.setMessage("Required");

        taskTaskField.getValidators().add(emptyField);
        taskTaskField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    if(taskTaskField.validate()){
                        canSave=true;
                    }else{
                        canSave=false;

                    }
                }


            }
        });





    }
    public void setTask(Task task,TaskMainController tc){
        this.task=task;
        this.tc=tc;

        //set taskinformation
        taskTaskField.setText(task.getTask_name());
        if(task.getSdate()!=null) startDateTF.setValue(convertDate(task.getSdate()));

        if(task.getStime()!=null)startTimeTF.setValue(LocalTime.parse(task.getStime()));
        if(task.getEdate()!=null)endDateTF.setValue(convertDate(task.getEdate()));
        if(task.getEtime()!=null)endTimeTF.setValue(LocalTime.parse(task.getEtime()));

        priorityComboBox.getSelectionModel().select(task.getPriority().name());
        repeatComboBox.getSelectionModel().select(task.getRepeat().name());
        //repeatTextField.setText(task.getRepeat().name());
        if(task.getDescription()!=null&&task.getDescription()!=""){
            descTextArea.setText(task.getDescription());
        }
        if(task.getLocation()!=null&&task.getLocation()!=""){
            locationTextField.setText(task.getLocation());
        }

        creatorLabel.setText("Creator: "+TaskControllerKt.getCreator(task.getCreator()));


        //set editable if is creator
        if(UserAccess.getUser().getUserID().equals(task.getCreator())){



            tempTask=new Task();
            tempTask.setId(task.getId());
            tempTask.setRepeat(task.getRepeat());
            tempTask.setPriority(task.getPriority());

            addUserButton.setVisible(true);

            completeButton.setVisible(true);
            memberViewClickable=true;


            //trace field change
            taskTaskField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if(newValue!=oldValue&&!oldValue.equals("")){
                        tempTask.setTask_name(newValue);
                        changeButton.setVisible(true);
                        canComplete=false;
                    }
                }
            });

            descTextArea.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if(newValue!=oldValue){
                        tempTask.setDescription(newValue);
                        changeButton.setVisible(true);
                        canComplete=false;
                    }

                }
            });
            locationTextField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if(newValue!=oldValue){
                        tempTask.setLocation(newValue);
                        changeButton.setVisible(true);
                        canComplete=false;
                    }

                }
            });
            startDateTF.valueProperty().addListener(new ChangeListener<LocalDate>() {
                @Override
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                    if(newValue!=oldValue)  {
                        String date=newValue.format(TaskControllerKt.getYearFormatter());
                        tempTask.setSdate(date);

                        changeButton.setVisible(true);
                        canComplete=false;
                    }

                }
            });
            endDateTF.valueProperty().addListener(new ChangeListener<LocalDate>() {
                @Override
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                    if(newValue!=oldValue){
                        String date=newValue.format(TaskControllerKt.getYearFormatter());
                        tempTask.setEdate(date);
                        changeButton.setVisible(true);
                        canComplete=false;
                    }
                }
            });
            startTimeTF.valueProperty().addListener(new ChangeListener<LocalTime>() {
                @Override
                public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                    if(newValue!=oldValue) {
                        if(startDateTF.getValue()==null){
                            canSave=false;
                            Alert alert=new Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                            alert.setHeaderText("The start date textfield need to be filled");

                            alert.show();
                            startTimeTF.setValue(null);
                        }else{
                            canSave=true;
                            String date=newValue.format(TaskControllerKt.getTimeFormatter());
                            tempTask.setStime(date);

                            changeButton.setVisible(true);
                            canComplete=false;

                        }

                    }

                }
            });
            endTimeTF.valueProperty().addListener(new ChangeListener<LocalTime>() {
                @Override
                public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                    if(newValue!=oldValue){
                        if(endDateTF.getValue()==null){
                            canSave=false;
                            Alert alert=new Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                            alert.setHeaderText("The end date textfield need to be filled");
                            endTimeTF.setValue(null);
                            alert.show();
                        }else{
                            canSave=true;
                            String date=newValue.format(TaskControllerKt.getTimeFormatter());
                            tempTask.setEtime(date);
                            changeButton.setVisible(true);
                            canComplete=false;


                        }

                    }

                }
            });


            priorityComboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if(newValue!=oldValue){
                        tempTask.setPriority(TaskControllerKt.intToPriority(newValue.intValue()));
                        changeButton.setVisible(true);
                        canComplete=false;
                    }

                }
            });
            repeatComboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if(newValue!=oldValue){
                        tempTask.setRepeat(TaskControllerKt.intToRepeat(newValue.intValue()));
                        changeButton.setVisible(true);
                        canComplete=false;


                    }
                }
            });

        }else{
            //diable the content
            taskTaskField.setDisable(true);
            startDateTF.setDisable(true);
            startTimeTF.setDisable(true);
            endTimeTF.setDisable(true);
            endDateTF.setDisable(true);
            priorityComboBox.setDisable(true);
            repeatComboBox.setDisable(true);
            descTextArea.setDisable(true);
            locationTextField.setDisable(true);

            taskTaskField.setStyle("-fx-opacity: 1");
            startDateTF.setStyle("-fx-opacity: 1");
            startTimeTF.setStyle("-fx-opacity: 1");
            endTimeTF.setStyle("-fx-opacity: 1");
            endDateTF.setStyle("-fx-opacity: 1");
            priorityComboBox.getEditor().setStyle("-fx-opacity: 1");
            repeatComboBox.getEditor().setStyle("-fx-opacity: 1");
            descTextArea.setStyle("-fx-opacity: 1");
            locationTextField.setStyle("-fx-opacity: 1");

        }
        updateMemberList();



    }

    private LocalDate convertDate(String date){
        return LocalDate.parse(date,TaskControllerKt.getYearFormatter());

    }

    //complete the task
    @FXML
    void complete(ActionEvent event) {
        if(canComplete) {
            TaskControllerKt.setComplete(task);
            tc.getTaskDetailsContainer().getChildren().clear();
            tc.updateTaskListContontainer();

            int id=task.getId();
            ArrayList<Reminder> arr= MainSceneController.getReminders().getAllReminder();
            for(Reminder r:arr){
                int rid=r.getTask().getId();
                if(rid==id){
                    r.stop();
                    arr.remove(r);
                    System.out.print(arr.size());
                    break;
                }
            }

        }else{
            Alert aLert=new Alert(Alert.AlertType.INFORMATION);
            aLert.setTitle("Warning");
            aLert.setHeaderText("Please save the change first");
            aLert.show();
        }
    }
    @FXML
    void changeTask(ActionEvent event) {
        validate();
        if(canSave) {
            TaskControllerKt.changeTask(tempTask);
            tc.updateTaskListContontainer();
            changeButton.setVisible(false);
            canComplete = true;
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("save the change successful");
            alert.show();
        }
    }
    @FXML
    void addUser(ActionEvent event) {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("AddUser.fxml"));
        try {
            Parent addTaskScene=loader.load();
            AddUserController ctl=loader.getController();
            ctl.setTask(task);
            ctl.setTaskDetailsController(this);

            Stage stage=new Stage();
            stage.initOwner(((Node)(event.getTarget())).getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);

            stage.setTitle("ADD member");
            stage.setScene(new Scene(addTaskScene));
            stage.showAndWait();
            //refresh memebr lkist


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void updateMemberList(){
        ArrayList<User> userList=TaskControllerKt.retrieveTaskMember(task.getId());
        obUserList=FXCollections.observableList(userList);
        memberListView.setItems(obUserList);

        //set right click function
        if(!obUserList.isEmpty()){
            //delete user
            ContextMenu menu=new ContextMenu();

            MenuItem show=new MenuItem("Show Detail");
            show.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        ObservableList<User> list = memberListView.getSelectionModel().getSelectedItems();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDetails.fxml"));

                        try {
                            Parent userInfo = loader.load();
                            UserDetailsController controller = loader.getController();
                            controller.setUserInfomation(list.get(0).getUserID());

                            Stage stage = new Stage();
                            //stage.initOwner(((Node) (event.getTarget())).getScene().getWindow());
                            //stage.initModality(Modality.WINDOW_MODAL);

                            stage.setTitle("User Infomation");
                            stage.setScene(new Scene(userInfo));
                            stage.showAndWait();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }catch (NullPointerException e){}



                }
            });
            menu.getItems().add(show);
            if(memberViewClickable){
                MenuItem delete=new MenuItem("Delete User");
                delete.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setHeaderText("Comfirm to delete user");
                            Optional<ButtonType> rs = alert.showAndWait();
                            if (rs.get() == ButtonType.OK) {
                                ObservableList<User> list = memberListView.getSelectionModel().getSelectedItems();
                                TaskControllerKt.deleteMember(task.getId(), list.get(0).getUserID());
                                updateMemberList();
                            }
                        }catch (NullPointerException e){}
                    }
                });
                menu.getItems().add(delete);

            }
            memberListView.setContextMenu(menu);
        }



    }
    private void validate(){
        /*startDateTF.setStyle("-fx-border-color: Black");
        endDateTF.setStyle("-fx-border-color: Black");
        endTimeTF.setStyle("-fx-border-color: Black");
        startTimeTF.setStyle("-fx-border-color: Black");*/
        canSave=true;
        if(endDateTF.getValue()!=null){
            if(startDateTF.getValue()!=null){
                LocalDate sdate=startDateTF.getValue();
                String sdateStr = sdate.format(TaskControllerKt.getYearFormatter());
                Calendar sCal=TaskControllerKt.getCalendarByDate(sdateStr);

                LocalDate edate=endDateTF.getValue();
                String edateStr = edate.format(TaskControllerKt.getYearFormatter());
                Calendar eCal=TaskControllerKt.getCalendarByDate(edateStr);





                if(eCal.before(sCal)){
                    canSave=false;
                    /*startDateTF.setStyle("-fx-border-color: Red");
                    endDateTF.setStyle("-fx-border-color: Red");
                    endTimeTF.setStyle("-fx-border-color: Red");
                    startTimeTF.setStyle("-fx-border-color: Red");*/
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(" End date  must after the start date ");
                    alert.show();

                } else if(!eCal.after(sCal)){
                    if(startTimeTF.getValue()!=null&&endTimeTF.getValue()!=null){
                        LocalTime slc=startTimeTF.getValue();
                        LocalTime elc=endTimeTF.getValue();
                        if(elc.isBefore(slc)){
                            canSave=false;
                            Alert alert=new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText(" End  time must after the start time");
                            alert.show();
                        }

                    }
                }

            }
        }

    }




}
