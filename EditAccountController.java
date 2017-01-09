/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import application.model.CurrentUser;
import application.model.database.ModifyUsers;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 */
public class EditAccountController implements Initializable {

    @FXML
    private JFXButton confirmButton;
    @FXML
    private JFXTextField firstNameTextField;
    @FXML
    private JFXTextField lastNameTextField;
    @FXML
    private JFXPasswordField cpTextField;
    @FXML
    private JFXPasswordField passwordTextField;
    @FXML
    private JFXTextField userNameTextField;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXToggleButton passwordToggle;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        firstNameTextField.setText(CurrentUser.currentUser().getfName());
        lastNameTextField.setText(CurrentUser.currentUser().getlName());
        passwordTextField.setText(CurrentUser.currentUser().getPassword());
        cpTextField.setText(CurrentUser.currentUser().getPassword());
        userNameTextField.setText(CurrentUser.currentUser().getUsername());
        cpTextField.disableProperty().set(true);
        passwordTextField.disableProperty().set(true);  
        
        //
        
    }    

    @FXML //action for confirm button edit account
    private void confirmAction(ActionEvent event) {
        CurrentUser.currentUser().setfName(firstNameTextField.getText());
        CurrentUser.currentUser().setlName(lastNameTextField.getText());
        if(passwordToggle.isSelected())
            CurrentUser.currentUser().setPassword(passwordTextField.getText());
        ModifyUsers.modifyUsers(CurrentUser.currentUser());
        confirmButton.getScene().getWindow().hide();
    }

    @FXML
    private void handlingKeys(KeyEvent event) {
        if(passwordTextField.getText() == null ? cpTextField.getText() != null : !passwordTextField.getText().equals(cpTextField.getText())){
            confirmButton.disableProperty().set(true);
        }
        else
            confirmButton.disableProperty().set(false);
    }

    @FXML
    private void cancelAction(ActionEvent event) {
        cancelButton.getScene().getWindow().hide();
    }

    @FXML
    private void toggleAction(MouseEvent event) {
        if(passwordToggle.isSelected()){
            passwordTextField.disableProperty().set(false);
            cpTextField.disableProperty().set(false);
        }
        else{
            passwordTextField.disableProperty().set(true);
            cpTextField.disableProperty().set(true);
        }
    }
    
    
}
