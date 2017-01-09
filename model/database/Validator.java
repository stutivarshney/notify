/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.model.database;

import application.model.CurrentUser;
import application.model.EventsList;
import application.model.Users;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Aj
 */
public class Validator {
    
    public static boolean validate(Users user) {
        try {
            Statement s = ConnectDB.conn.createStatement();
            ResultSet rs = s.executeQuery("select * from users;");
            while (rs.next()) {
                if (user.getUsername().equalsIgnoreCase(rs.getString(2)) && user.getPassword().equalsIgnoreCase(rs.getString(3))) {
                    user.setUserID(rs.getInt(1));
                    user.setfName(rs.getString(4));
                    user.setlName(rs.getString(5));
                    new CurrentUser(user);
                    EventsList.initializeList();
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e +"here");
        }
        return false;
    }
}
