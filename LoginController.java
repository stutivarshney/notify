/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import application.model.CurrentUser;
import application.model.EventsList;
import application.model.Users;
import application.model.database.Validator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 */
public class LoginController implements Initializable {

    private Stage s;
    @FXML
    private JFXTextField userField;
    @FXML
    private JFXPasswordField passField;
    @FXML
    private Hyperlink newAccount;
    @FXML
    private JFXButton submitButton;
    @FXML
    private JFXTextField userForm;
    @FXML
    private JFXTextField fnameForm;
    @FXML
    private JFXTextField lnameForm;
    @FXML
    private JFXPasswordField passForm;
    @FXML
    private JFXPasswordField cpForm;
    @FXML
    private JFXButton submitForm;
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML // handler for exiting the application login screen
    
    // Prompts user with warning
    public void cancelHandle(ActionEvent cancel) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm exit?");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("You will not recieve any event notification if you exit without logging in.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        } else {
            userField.clear();
            passField.clear();
        }
    }

    @FXML // handler for exiting create new user page
    // shows warning before exiting screen
    public void cancelForm(ActionEvent cancel) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm exit?");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("You will not recieve any event notification if you exit without logging in.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }
    }

    @FXML // handler for submit button in login page
    public void submitHandle(ActionEvent submit) {
        application.model.Users user = new Users();
        user.setUsername(userField.getText());
        user.setPassword(passField.getText());
        boolean valid = Validator.validate(user);
        if (valid) {
            submitButton.getScene().getWindow().hide();
            try {
                MainApp mainApp = new MainApp();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException n) {
            }
        } else {
            userField.clear();
            passField.clear();
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Oops.. Something went wrong.");
            alert.setHeaderText("Wrong credentials");
            alert.setContentText("Invalid username or password. Please try again.");
            alert.showAndWait();
        }
    }

    @FXML // handler for create new user button on sign up page
    public void createNew(ActionEvent e) {
        AnchorPane root = null;
        try {
            root = (AnchorPane) FXMLLoader.load(getClass().getResource("view/CreateAccount.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene base = new Scene(root, 350, 500);
        s = new Stage();
        s.setScene(base);
        s.show();
        s.setResizable(false);
        s.setOnCloseRequest(ec -> {
            ec.consume();
            ActionEvent close = new ActionEvent();
            cancelForm(close);
        });
        newAccount.getScene().getWindow().hide();
    }

    @FXML 
    public void loadIt(KeyEvent key) {
        if (key.getText().contains(" ")) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Oops.. Something went wrong.");
            alert.setHeaderText("Wrong credentials");
            alert.setContentText("Blank spaces are not allowed in username or password.");
            alert.showAndWait();
        }
        if ("".equals(userField.getText()) || userField.getText() == null || passField.getText() == null || "".equals(passField.getText())) {
            submitButton.disableProperty().setValue(Boolean.TRUE);
        } else {
            submitButton.disableProperty().setValue(Boolean.FALSE);
        }
    }

    @FXML //enables or disables submit button by checking validity of credentials entered
    public void loadItForm(KeyEvent key) {
        if (key.getText().contains(" ")) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Oops.. Something went wrong.");
            alert.setHeaderText("Wrong credentials");
            alert.setContentText("Blank spaces are not allowed in username or password.");
            alert.showAndWait();
        }
        if ("".equals(passForm.getText()) || cpForm.getText() == null || "".equals(cpForm.getText()) || "".equals(userForm.getText()) || userForm.getText() == null || passForm.getText() == null) {
            submitForm.disableProperty().setValue(Boolean.TRUE);
        } else {
            submitForm.disableProperty().setValue(Boolean.FALSE);
        }

        if ((passForm.getText().equals(cpForm.getText())) && !(passForm.getText() == null) && !(passForm.getText().equals(""))) {
            submitForm.disableProperty().setValue(Boolean.FALSE);
        } else {
            submitForm.disableProperty().setValue(Boolean.TRUE);
        }

    }

    @FXML //adds new user to the database
    public void addUserNew(ActionEvent e) {
        Users u = new Users();
        u.setUsername(userForm.getText());
        u.setPassword(passForm.getText());
        u.setfName(fnameForm.getText());
        u.setlName(lnameForm.getText());

        boolean flag = application.model.database.ModifyUsers.addUsers(u);
        if (flag) {
            submitForm.getScene().getWindow().hide();
            new CurrentUser(u);
            EventsList.initializeList();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Sign up successful.");
            alert.setContentText("Welcome to notify!!");
            alert.showAndWait();
            try {
                new MainApp();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            userForm.clear();
            passForm.clear();
            cpForm.clear();
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Oops.. Something went wrong.");
            alert.setHeaderText("User already exists.");
            alert.setContentText("UserName already taken. Please use another username.");
            alert.showAndWait();

        }
    }

}
