/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.model;

import application.model.database.ConnectDB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Aj
 */
public class EventsList {
    public static ObservableList<Events> list;
    public static boolean refresh = false;
    public static void initializeList(){
        try {
            if(list == null){
                ObservableList<Events> listtemp = getAllEvents();
                list =listtemp;
            }
            else
                refreshList();
        } catch (NullPointerException n) {
            System.err.println("here too");
        } catch (SQLException ex) {
            Logger.getLogger(EventsList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ObservableList<Events> getAllEvents() throws SQLException {
        ObservableList<Events> list1 = FXCollections.observableArrayList();
        Statement stmt = ConnectDB.conn.createStatement();
        ResultSet rs = null;

        
        try {

            rs = stmt.executeQuery("select * from events order by date;");

            while (rs.next()) {
                if(rs.getInt("user_id")==CurrentUser.currentUser().getUserID()){
                    Events tempEvent = convertRowToEvents(rs);
                    list1.add(tempEvent);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("list"+list);
        return list1;
    }

    private static Events convertRowToEvents(ResultSet myRs) throws SQLException {

        String eventType = myRs.getString("event_type");
        String eventName = myRs.getString("event_name");
        DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate date = LocalDate.parse(myRs.getString("date"), dt);
        System.out.println(date);
        DateTimeFormatter tt = DateTimeFormatter.ofPattern("H:mm:ss");
        LocalTime time = LocalTime.parse(myRs.getString("time"),tt);
        System.out.println(time);
        String recurrence = myRs.getString("recurrence");
        boolean notify = myRs.getBoolean("notify");
        String otherInfo = myRs.getString("other_info");

        Events tempEvent = new Events(eventType, eventName, date, time, recurrence, notify, otherInfo);

        return tempEvent;
    }
    
    public static void refreshList() throws SQLException{
        list.clear();
        ObservableList<Events> listtemp = getAllEvents();
        list =listtemp;
        refresh = true;
    }

}
