package scene.Task.UI;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import resources.database.UserAccess;
import scene.Task.Priority;
import scene.Task.Repeat;
import scene.Task.TaskControllerKt;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

/**
 * Created by hhf on 6/29/17.
 */
public class AddTaskController implements Initializable{
    @FXML
    private JFXTextField taskNameTextField;

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
    private HBox priorityButton;

    @FXML
    private JFXComboBox<String> priorityComboBox;

    @FXML
    private HBox repeatButton;

    @FXML
    private JFXComboBox<String> repeatComboBox;

    @FXML
    private JFXTextField locationTextField;

    @FXML
    private JFXTextArea descriptionTextArea;

    private boolean canSave=false;
    private String event=null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<String> priority= FXCollections.observableArrayList(Priority.NONE.name(),Priority.LOW.name(),Priority.MEDIUM.name(),Priority.HIGH.name());
        priorityComboBox.setItems(priority);
        priorityComboBox.getSelectionModel().selectFirst();

        ObservableList<String> repeat=FXCollections.observableArrayList(Repeat.NONE.name(),Repeat.DAILY.name(),Repeat.WEEKLY.name());
        repeatComboBox.setItems(repeat);
        repeatComboBox.getSelectionModel().selectFirst();

        //validate
        RequiredFieldValidator emptyField=new RequiredFieldValidator();
        emptyField.setMessage("Required");

        taskNameTextField.getValidators().add(emptyField);
        taskNameTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    if(taskNameTextField.validate()){
                       canSave=true;
                    }else{
                        canSave=false;

                    }
                }


            }
        });
        startTimeTF.valueProperty().addListener(new ChangeListener<LocalTime>() {
            @Override
            public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                if(newValue!=oldValue&&newValue!=null) {
                    if(startDateTF.getValue()==null){
                        canSave=false;
                        Alert alert=new Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                        alert.setHeaderText("The start date textfield need to be filled");

                        alert.show();
                        startTimeTF.setValue(null);
                    }else{canSave=true;}

                }

            }
        });

        endTimeTF.valueProperty().addListener(new ChangeListener<LocalTime>() {
            @Override
            public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                if(newValue!=oldValue&&newValue!=null){
                    if(endDateTF.getValue()==null){
                        canSave=false;
                        Alert alert=new Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                        alert.setHeaderText("The end date textfield need to be filled");
                        endTimeTF.setValue(null);
                        alert.show();
                    }else{canSave=true;}

                }

            }
        });
        startTimeTF.setIs24HourView(true);
        endTimeTF.setIs24HourView(true);


        /*endButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("dateTimePicker.fxml"));
                try {
                    AnchorPane ap=loader.load();
                    PriorityPickerController rpc=loader.getController();
                    //rpc.
                    Stage stage=new Stage(StageStyle.UNDECORATED);
                    Point2D pos=floatUIPos(event,endTimeTF);
                    stage.setX(pos.getX());
                    stage.setY(pos.getY());

                    stage.setScene(new Scene(ap));
                    stage.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            if(newValue==false) stage.close();
                        }
                    });
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        /*startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("dateTimePicker.fxml"));
                try {
                    AnchorPane ap=loader.load();
                    PriorityPickerController rpc=loader.getController();
                    //rpc.
                    Stage stage=new Stage(StageStyle.UNDECORATED);
                    Point2D pos=floatUIPos(event,startTimeTF);
                    stage.setX(pos.getX());
                    stage.setY(pos.getY());

                    stage.setScene(new Scene(ap));
                    stage.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            if(newValue==false) stage.close();
                        }
                    });
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        /*priorityButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                FXMLLoader loader=new FXMLLoader(getClass().getResource("PriorityPicker.fxml"));
                try {
                    AnchorPane ap=loader.load();
                    PriorityPickerController rpc=loader.getController();
                    //rpc.
                    Stage stage=new Stage(StageStyle.UNDECORATED);
                    Point2D pos=floatUIPos(event,priorityTextField);
                    stage.setX(pos.getX());
                    stage.setY(pos.getY());
                    stage.setScene(new Scene(ap));
                    stage.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            if(newValue==false) stage.close();
                        }
                    });
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        /*repeatButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("RepeatPicker.fxml"));
                try {
                    AnchorPane ap=loader.load();
                    RepeatPickerController rpc=loader.getController();
                    //rpc.
                    Stage stage=new Stage(StageStyle.UNDECORATED);
                    stage.setScene(new Scene(ap));
                    Point2D pos=floatUIPos(event,repeatTextField);
                    stage.setX(pos.getX());
                    stage.setY(pos.getY());
                    stage.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            if(newValue==false) stage.close();
                        }
                    });
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });*/
    }


    @FXML
    void cancel(ActionEvent event) {
        Stage stage=(Stage)((Node)event.getTarget()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void save(ActionEvent event) {
        validate();
        if(canSave) {

            String task = taskNameTextField.getText();

            LocalDate sdate = startDateTF.getValue();
            String start = null;
            if (sdate != null) start = sdate.format(TaskControllerKt.getYearFormatter());


            LocalTime stime = startTimeTF.getValue();
            String startTime = null;
            if (stime != null) startTime = stime.format(TaskControllerKt.getTimeFormatter());


            LocalDate edate = endDateTF.getValue();
            String end = null;
            if (edate != null) end = edate.format(TaskControllerKt.getYearFormatter());


            LocalTime etime = endTimeTF.getValue();
            String endTime = null;
            if (etime != null) endTime = etime.format(TaskControllerKt.getTimeFormatter());

            int priority = priorityComboBox.getSelectionModel().getSelectedIndex();
            int repeat = repeatComboBox.getSelectionModel().getSelectedIndex();


            String location = locationTextField.getText();
            String description = descriptionTextArea.getText();


            TaskControllerKt.addTask(task, start, startTime, end, endTime, description, priority, repeat, location, this.event, new UserAccess().getUser().getUserID());
            cancel(event);
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Add task successful");
            alert.show();
        }
    }

    private Point2D floatUIPos(MouseEvent event, JFXTimePicker field){
        Window window=((Node)(event.getTarget())).getScene().getWindow();
        Point2D winPos=new Point2D(window.getX(),window.getY());
        Scene scene=((Node)(event.getTarget())).getScene();
        Point2D scenPos=new Point2D(scene.getX(),scene.getY());
        Point2D nodPos=field.localToScene(0,0);
        return new Point2D(winPos.getX()+scenPos.getX()+nodPos.getX(),winPos.getY()+scenPos.getY()+nodPos.getY()+field.getHeight());
    }

    public void setEvent(String event) {
        this.event = event;
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
                            alert.setHeaderText(" End time must after the start time");
                            alert.show();
                        }

                    }
                }

            }
        }






    }
}
