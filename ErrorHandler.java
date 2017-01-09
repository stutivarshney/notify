package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

// this class deals with error i.e if user tries to relaunch application w/o exiting previous launch
public class ErrorHandler {
	private Stage window;

	@FXML
	private Button errClose;

	@FXML
	public void onClose(ActionEvent e){
		window =  (Stage)errClose.getScene().getWindow();
		window.close();
		Platform.exit();
	}
}
