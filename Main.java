package application;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Main extends Application {
    
    Stage extended;

    @Override
    public void init() throws InterruptedException {
        Thread.sleep(500);
        application.model.database.ConnectDB.makeConnection();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        extended = primaryStage;
        final File file = new File("flag");
        final RandomAccessFile raf = new RandomAccessFile(file, "rw");
        final FileLock fl = raf.getChannel().tryLock();

        StackPane rooterr = new StackPane();
        if (fl == null) {
            try {
                rooterr = (StackPane) FXMLLoader.load(getClass().getResource(""
                       +"view/ErrorPopup.fxml"));
            } catch (NullPointerException n) {
                System.out.println("error fxml");
                Platform.exit();
            } catch (IOException ex) {
                System.out.println("error fxml io");
                Platform.exit();
            }
            Scene scene = new Scene(rooterr);
            primaryStage.setTitle("Application Already Running");
            //primaryStage.setWidth(500);
            //primaryStage.setHeight(380);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } else {
            AnchorPane root = new AnchorPane();
            try {
                root = (AnchorPane) FXMLLoader.load(getClass().getResource(""
                        +"view/Login.fxml"));
            } catch (NullPointerException e) {
                System.out.println("Login.fxml");
                Platform.exit();
            } catch (IOException ex) {
                System.out.println("Login.fxml io");
                Platform.exit();
            }
            Scene scene = new Scene(root, 700, 400);
            primaryStage.setTitle("Welcome to Notify");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            primaryStage.setOnCloseRequest(e -> {
                e.consume();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm exit?");
                alert.setHeaderText("Are you sure you want to exit?");
                alert.setContentText("You will not recieve any event notific"
                        +"ation if you exit without logging in.");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Platform.exit();
                }
            });
        }
        
    }

    @SuppressWarnings("restriction")
    public static void main(String[] args) {
        launch(args);
    }
}
