package application.model.database;

import application.model.CurrentUser;
import application.model.Users;
import static application.model.database.ConnectDB.conn;
import java.sql.*;

public class ModifyUsers {

    static Statement s = null;
    static PreparedStatement stmt = null;
    static ResultSet rs = null;

    public static boolean addUsers(Users u) {

        try {
            if (checkUsername(u)) {
                stmt = ConnectDB.conn.prepareStatement("insert into users"
                        +"(username,password,f_name,l_name) "
                        + "values(?,?,?,?)");
                stmt.setString(1, u.getUsername());
                stmt.setString(2, u.getPassword());
                stmt.setString(3, u.getfName());
                stmt.setString(4, u.getlName());

                stmt.executeUpdate();
                ResultSet rs = stmt.executeQuery("select * from users");
                rs.last();
                u.setUserID(rs.getInt(1));
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static void removeUsers(Users u) {

        try {

            
            stmt = ConnectDB.conn.prepareStatement("delete from users "
                    +"WHERE user_id = ?);");
            stmt.setInt(1,CurrentUser.currentUser().getUserID());
            stmt.executeUpdate();

        } catch (Exception e) {
        }
    }

    
    public static void modifyUsers(Users u){
        try{
            stmt=conn.prepareStatement("update users SET f_name = ?,l_name = ?,password = ? WHERE(user_id = ? AND username = ?);");
            stmt.setString(1, u.getfName());
            stmt.setString(2, u.getlName());
            stmt.setString(3, u.getPassword());
            stmt.setInt(4, CurrentUser.currentUser().getUserID());
            stmt.setString(5, u.getUsername());
            
            stmt.executeUpdate();
        }
        catch(Exception ex){
        }
    }

    private static boolean checkUsername(Users u) {
        try {
            s = ConnectDB.conn.createStatement();
            rs = s.executeQuery("select * from users;");

            while (rs.next()) {
                if (u.getUsername().equals(rs.getString(2))) {
                    System.out.println("USERNAME TAKEN");
                    return false;
                }

            }
        } catch (Exception ex) {
        }
        return true;
    }
}
