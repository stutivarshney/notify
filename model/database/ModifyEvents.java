package application.model.database;

import application.model.CurrentUser;
import application.model.Events;
import application.model.EventsList;
import java.sql.PreparedStatement;
import java.time.format.DateTimeFormatter;

public class ModifyEvents {
        static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        static DateTimeFormatter tmt = DateTimeFormatter.ofPattern("HH:mm:ss");
    static String formattedDate = null; 
    static String formattedTime = null;
	static PreparedStatement stmt = null;
	
	public static void addEvents(Events eve){
		
		try{
			
			stmt = ConnectDB.conn.prepareStatement("insert into events"
						+"(user_id,event_type,event_name,date,time,recurrence,notify,other_info)"
						+"values(?,?,?,?,?,?,?,?)");
			formattedDate = eve.getDate().format(fmt);
                        formattedTime = eve.getTime().format(tmt);
                        stmt.setInt(1, CurrentUser.currentUser().getUserID());
			stmt.setString(2,eve.getEventType());
			stmt.setString(3,eve.getEventName());
			stmt.setString(4,formattedDate);
                        stmt.setString(5, formattedTime);
			stmt.setString(6,eve.getRecurrence());
			stmt.setBoolean(7,eve.getNotify());
			stmt.setString(8,eve.getOtherInfo());
			
			stmt.executeUpdate();
                        EventsList.refreshList();
			
		}
		catch(Exception e){
		}
	}
	
	public static void updateEvents(Events eve){
		try{
			stmt = ConnectDB.conn.prepareStatement("update events SET "
					+ "date = ?,time = ?,notify = ? "
					+ "WHERE(user_id = ? AND event_name =?);");
			
                        formattedDate = eve.getDate().format(fmt);
                        formattedTime = eve.getTime().format(tmt);
			
			stmt.setString(1,formattedDate);
                        stmt.setString(2,formattedTime);
                        stmt.setBoolean(3, eve.getNotify());
			stmt.setInt(4, CurrentUser.currentUser().getUserID());
                        stmt.setString(5,eve.getEventName());
                        
                        stmt.executeUpdate();
			EventsList.refreshList();		
		}
		catch(Exception ex){
		}
	}
        
        public static void deleteEvents(Events eve){
            try{
                stmt = ConnectDB.conn.prepareStatement("delete from events WHERE "
                        +"(user_id = ? AND event_type = ? AND event_name = ? AND date = ?);");
                formattedDate = eve.getDate().format(fmt);
                stmt.setInt(1, CurrentUser.currentUser().getUserID());
                stmt.setString(2, eve.getEventType());
                stmt.setString(3,eve.getEventName());
                stmt.setString(4, formattedDate);
                
                stmt.executeUpdate();
                EventsList.refreshList();
            }
            catch(Exception ex ){
            }
        }
}
