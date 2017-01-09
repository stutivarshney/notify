/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.model;

import application.Notification;
import java.time.LocalDate;
import java.time.LocalTime;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 *
 * @author Aj
 */
public class NotifyBackgroundActivity extends Task<Void> {

    public static boolean stop = false;
    public static boolean dismiss = false;
    public static ObservableList<Events> tempEventList;

    @Override
    public void run() {
        tempEventList = observableArrayList();
        tempEventList.addAll(EventsList.list);
        while (true) {
            if (stop == true) {
                cancel();
            }
            if (EventsList.refresh == true) {
                tempEventList.clear();
                tempEventList.addAll(EventsList.list);
                EventsList.refresh = false;
            }
            tempEventList.stream().forEach((e) -> {
                if (e.getNotify() == true) {
                    //System.err.println("1");
                    if (e.getDate().equals(LocalDate.now()) && e.getTime().equals(LocalTime.now())) {
                        dismiss = Notification.generateNotification(e);

                    }

                }
            });

        }
    }
   
    @Override
    protected Void call() throws Exception {
        return null;
    }

}
