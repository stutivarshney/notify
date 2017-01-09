package application;

import application.model.CurrentUser;
import application.model.Events;
import application.model.EventsList;
import application.model.database.ConnectDB;
import application.model.database.ModifyEvents;
import static application.model.database.ModifyEvents.addEvents;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController implements Initializable {

    @FXML
    private JFXButton logoutButton;
    @FXML
    private JFXButton dashboardButton;
    @FXML
    private JFXButton addEventButton;
    @FXML
    private JFXButton manageEventsButton;
    @FXML
    private StackPane mainStackPane;
    @FXML
    private AnchorPane manageEventsPane;
    @FXML
    private TableView<Events> eventTable;
    @FXML
    private TableColumn<Events, String> eventNameTable;
    @FXML
    private TableColumn<Events, String> eventTypeTable;
    @FXML
    private TableColumn<Events, LocalDate> eventDateTable;
    @FXML
    private TableColumn<Events, LocalTime> eventTimeTable;
    @FXML
    private TableColumn<Events, Boolean> eventNotificationTable;
    @FXML
    private TableColumn<Events, String> eventReccurencyTable;
    @FXML
    private TableColumn<Events, String> eventOtherInfoTable;
    @FXML
    private Button deleteButtonTable;
    @FXML
    private AnchorPane addEventPane;
    @FXML
    private JFXDatePicker eventDatePicker;
    @FXML
    private JFXDatePicker eventTimePicker;
    @FXML
    private JFXTextArea addEventInfoArea;
    @FXML
    private JFXTextField eNameTextField;
    @FXML
    private ComboBox<String> eventTypeComboBox;
    @FXML
    private ComboBox<String> recurrenceComboBox;
    @FXML
    private AnchorPane dashboardPane;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private JFXButton editAccountButton;
    @FXML
    private JFXToggleButton notificatioToggleButton;
    @FXML
    private JFXButton addEventButton1;
    @FXML
    private Button refreshButtonTable;
    @FXML
    private TableColumn<Events, String> upcomingList;
    @FXML
    private TableView<Events> upcomingTable;

    @FXML
    private void logoutHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm logout?");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("You will not recieve any event notification if you logout");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Platform.setImplicitExit(true);
            CurrentUser.removeCurrentUser();
            logoutButton.getScene().getWindow().hide();

            if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();
                TrayIcon[] t = tray.getTrayIcons();
                tray.remove(t[0]);
            }

           
            Stage ns = new Stage();
            AnchorPane root = new AnchorPane();
            try {
                root = (AnchorPane) FXMLLoader.load(getClass().getResource(""
                        + "view/Login.fxml"));
            } catch (NullPointerException e) {
                System.out.println("Login.fxml");
                Platform.exit();
            } catch (IOException ex) {
                System.out.println("Login.fxml io");
                Platform.exit();
            }
            Scene scene = new Scene(root, 700, 400);
            ns.setTitle("Welcome to Notify");
            ns.setScene(scene);
            ns.setResizable(false);
            ns.show();
            ns.setOnCloseRequest(e -> {
                e.consume();
                Alert alertexit = new Alert(Alert.AlertType.CONFIRMATION);
                alertexit.setTitle("Confirm exit?");
                alertexit.setHeaderText("Are you sure you want to exit?");
                alertexit.setContentText("You will not recieve any event notific"
                        + "ation if you exit without logging in.");

                Optional<ButtonType> resultexit = alertexit.showAndWait();
                if (resultexit.get() == ButtonType.OK) {
                    Platform.exit();
                }
            });

        }

    }

    @FXML
    private void dashboardClick(ActionEvent event) {
        dashboardPane.toFront();
        refreshUserDetails();
    }

    @FXML
    private void addEventClick(ActionEvent event) {
        addEventPane.toFront();
    }

    @FXML
    private void manageEventClick(ActionEvent event) {
        manageEventsPane.toFront();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshUserDetails();

        eventTypeComboBox.getItems().removeAll(eventTypeComboBox.getItems());
        eventTypeComboBox.getItems().addAll("Personal", "Business", "General");

        eventDatePicker.setValue(LocalDate.now());
        eventTimePicker.setTime(LocalTime.now());

        recurrenceComboBox.getItems().addAll("None", "Monthly", "Yearly", "Daily");
        eventDatePicker.editableProperty().set(false);
        eventTimePicker.editableProperty().set(false);
        addEventButton.disableProperty().set(true);

        upcomingList.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        eventNameTable.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        eventTypeTable.setCellValueFactory(new PropertyValueFactory<>("eventType"));
        eventDateTable.setCellValueFactory(new PropertyValueFactory<>("date"));
        eventTimeTable.setCellValueFactory(new PropertyValueFactory<>("time"));
        System.out.println(eventTimeTable);
        eventNotificationTable.setCellValueFactory(new PropertyValueFactory<>("notify"));
        eventOtherInfoTable.setCellValueFactory(new PropertyValueFactory<>("otherInfo"));
        eventReccurencyTable.setCellValueFactory(new PropertyValueFactory<>("recurrence"));
        
        EventsList.initializeList();
        eventTable.setItems(EventsList.list);
        upcomingTable.setItems(EventsList.list);
    }

    public void refreshUserDetails() {
        usernameLabel.setText(CurrentUser.currentUser().getUsername());
        firstNameLabel.setText(CurrentUser.currentUser().getfName());
        lastNameLabel.setText(CurrentUser.currentUser().getlName());
    }

    @FXML
    private void editAccount(ActionEvent event) {
        Stage editAcc = new Stage();
        editAcc.centerOnScreen();
        editAcc.setResizable(false);

        editAcc.initModality(Modality.APPLICATION_MODAL);
        AnchorPane base = null;
        try {
            base = FXMLLoader.load(getClass().getResource("view/ModifyAccount.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene root = new Scene(base);
        editAcc.setScene(root);
        editAcc.initStyle(StageStyle.UNDECORATED);
        editAcc.setTitle("Modify Account");

        editAcc.showAndWait();

        refreshUserDetails();
    }

    @FXML
    private void notificationStatusHandler(MouseEvent event) {
        if (notificatioToggleButton.isSelected()) {
            eventTimePicker.disableProperty().set(false);
        } else {
            eventTimePicker.disableProperty().set(true);
        }
    }

    @FXML
    private void clearEventButton(ActionEvent event) {
        eNameTextField.clear();
        eventTypeComboBox.getSelectionModel().clearSelection();
        recurrenceComboBox.getSelectionModel().clearSelection();
        eventDatePicker.setValue(LocalDate.now());
        eventTimePicker.setTime(LocalTime.now());
        notificatioToggleButton.selectedProperty().set(false);
        eventTimePicker.disableProperty().set(true);
        addEventInfoArea.clear();
    }

    @FXML
    private void eventButtonSetup(ActionEvent event) {
        if (("").equals(eNameTextField.getText()) || eNameTextField.getText() == null) {
            addEventButton.disableProperty().set(true);
        } else if (eventTypeComboBox.getSelectionModel().isEmpty()) {
            addEventButton.disableProperty().set(true);
        } else if (recurrenceComboBox.getSelectionModel().isEmpty()) {
            addEventButton.disableProperty().set(true);
        } else {
            addEventButton.disableProperty().set(false);
        }
    }

    @FXML
    private void addEventButtonSetup(KeyEvent event) {
        eventButtonSetup(new ActionEvent());
    }

    @FXML
    private void addEventToDB(ActionEvent event) throws SQLException {
        java.sql.Statement stmt1 = ConnectDB.conn.createStatement();
        ResultSet rs = stmt1.executeQuery("select * from events");
        String name = eNameTextField.getText(),
                type = eventTypeComboBox.getSelectionModel().getSelectedItem();
        DateTimeFormatter fmtt = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String date = eventDatePicker.getValue().format(fmtt);
        while (rs.next()) {
            if (rs.getInt(1) == CurrentUser.currentUser().getUserID() && rs.getString(3).equalsIgnoreCase(name) && rs.getString(2).equals(type) && rs.getString(4).equals(date)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Oops...");
                alert.setHeaderText("Event already exists");
                alert.setContentText("Please try again with a different name or type.");
                alert.showAndWait();
                return;
            }
        }
        Events e = new Events();
        e.setEventName(eNameTextField.getText());
        e.setEventType(eventTypeComboBox.getSelectionModel().getSelectedItem());
        e.setDate(eventDatePicker.getValue());
        e.setOtherInfo(addEventInfoArea.getText());
        e.setRecurrence(recurrenceComboBox.getSelectionModel().getSelectedItem());
        if (notificatioToggleButton.isSelected()) {
            e.setNotify(true);
            e.setTime(eventTimePicker.getTime());
        } else {
            e.setNotify(false);
        }
        try {
            addEvents(e);
        } catch (Exception err) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Done!");
        alert.setHeaderText("Event added.");
        alert.setContentText("Event added successfully.");
        alert.showAndWait();
        clearEventButton(event);
        eventTable.getItems().clear();
        eventTable.getItems().addAll(EventsList.list);
        upcomingTable.getItems().clear();
        upcomingTable.getItems().addAll(EventsList.list);
    }

    @FXML
    private void tableMouseClick(MouseEvent event) {
        
        Events eqqq = eventTable.getSelectionModel().getSelectedItem();
        if (eqqq == null) {
            deleteButtonTable.disableProperty().set(true);
        } else {
            deleteButtonTable.disableProperty().set(false);
        }
    }

    @FXML
    private void deleteFromTableAction(ActionEvent event) {
        Events e = eventTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm delete?");
        alert.setHeaderText("Are you sure you want to delete the event?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            ModifyEvents.deleteEvents(e);
            eventTable.getItems().clear();
            eventTable.getItems().addAll(EventsList.list);
            upcomingTable.getItems().clear();
            upcomingTable.getItems().addAll(EventsList.list);
            deleteButtonTable.disableProperty().set(true);
        } 
//        else {
//        }

    }

    @FXML
    private void refreshButtonAction(ActionEvent event) {
        EventsList.initializeList();
        eventTable.getItems().clear();
        eventTable.getItems().addAll(EventsList.list);
        upcomingTable.getItems().clear();
        upcomingTable.getItems().addAll(EventsList.list);
        deleteButtonTable.disableProperty().set(true);
    }
    

}
