/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import application.model.NotifyBackgroundActivity;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;

/**
 *
 */
public class MainApp {

    private Stage appStage;
    private final Scene root;
    private boolean firstTime = true;
    private TrayIcon trayIcon;
    private AnchorPane base;

    
    // launches application
    public MainApp() throws IOException {

        Platform.setImplicitExit(false);
        if (!SystemTray.isSupported()) {
            Platform.exit();
        }
        
        Worker w= new  NotifyBackgroundActivity();
        Thread t = new Thread((Runnable)w);
        t.setDaemon(true);
        t.start();
        
        appStage = new Stage();
        appStage.centerOnScreen();
        appStage.setOnCloseRequest(e -> {

        });
        
        try {
            base = (AnchorPane) FXMLLoader.load(getClass().getResource("view/MainApplication.fxml"));
        } catch (NullPointerException nul) {
            System.out.println("lol2");
        }
        root = new Scene(base);
        appStage.setScene(root);
        appStage.setMinHeight(600);
        appStage.setMinWidth(900);
        createTrayIcon(appStage);
        appStage.show();
    }
    
    //start application as a service in system tray
    public final void createTrayIcon(final Stage stage) {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = new BufferedImage(128, 128, BufferedImage.TYPE_4BYTE_ABGR);
        try {
            image = ImageIO.read(MainApp.class.getResource("resources/fgh.png"));
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (NullPointerException n) {
            System.out.println("lol3");
        }

        stage.setOnCloseRequest((WindowEvent t) -> {
            t.consume();
            hide(stage);
        });

        // create a action listener to listen for default action executed on the tray icon
        final ActionListener closeListener = (java.awt.event.ActionEvent e) -> {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm exit?");
                alert.setHeaderText("Are you sure you want to exit?");
                alert.setContentText("You will not recieve any event notification.");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    Platform.exit();
                    System.exit(0);
                } else {
                }
            });
        };

        ActionListener showListener = (java.awt.event.ActionEvent e) -> {
            Platform.runLater(() -> {
                if (!stage.isShowing()) {
                    stage.show();
                } else if (stage.isIconified()) {
                    stage.setIconified(false);
                } else {
                    hide(stage);
                }
            });
        };
        // create a popup menu
        PopupMenu popup = new PopupMenu();

        MenuItem showItem = new MenuItem("Show");
        showItem.addActionListener(showListener);
        popup.add(showItem);

        MenuItem closeItem = new MenuItem("Exit");
        closeItem.addActionListener(closeListener);
        popup.add(closeItem);

        // construct a TrayIcon
        // set the TrayIcon properties
        int w = new TrayIcon(image).getSize().width;
        trayIcon = new TrayIcon(image.getScaledInstance(w, -1, Image.SCALE_SMOOTH), "Notify", popup);

        trayIcon.addActionListener(showListener);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println(e);
        }
    }
    
    // display tray popup when application minimized
    public void showProgramIsMinimizedMsg() {
        if (firstTime) {
            trayIcon.displayMessage("Notify",
                    "Running in Background",
                    TrayIcon.MessageType.INFO);
            firstTime = false;
        }
    }

    private void hide(final Stage stage) {
        Platform.runLater(() -> {
            if (SystemTray.isSupported()) {
                stage.hide();
                showProgramIsMinimizedMsg();
            } else {
                System.exit(0);
            }
        });
    }

}
