package application.model;

import java.io.Serializable;
import java.time.*;

public class Events implements Serializable{
	
	
	private String eventType,eventName,recurrence,otherInfo;
        private LocalDate date;
        private LocalTime time;
        private boolean notify;
	
	public Events() {
		// TODO Auto-generated constructor stub
		
		eventType="";
		eventName = "";
		recurrence = "";
                date = LocalDate.now();
                time = LocalTime.now();
		notify = true;
		otherInfo = "";
		
	}
	
    /**
     *
     * @param et
     * @param en
     * @param date
     * @param time
     * @param recurrence
     * @param notify
     * @param oi
     */
    public Events(String et,String en,LocalDate date,LocalTime time,String recurrence,boolean notify,String oi ){
		eventType = et;
		eventName = en;
		this.date = date;
                this.time = time;
		this.recurrence = recurrence;
		this.notify = notify;
		otherInfo = oi;
	}
	
	
	public void setEventType(String eventType){
		this.eventType = eventType;
	}
	
	
	public String getEventType(){
		return eventType;
	}
	
	public void setEventName(String eventName){
		this.eventName = eventName;
	}
	
	public String getEventName(){
		return eventName;
	}
	
	public void setDate(LocalDate date){
		this.date = date;
	}
	
	public LocalDate getDate(){
		return date;
	}
        
        public void setTime(LocalTime time){
                this.time = time;
        }
        
        public LocalTime getTime(){
                return time;
        }
	
	public void setRecurrence(String recurrence){
		this.recurrence = recurrence;
	}
	
	public String getRecurrence(){
		return recurrence;
	}
	
	public void setNotify(boolean notify){
		this.notify = notify;
	}
	
	public boolean getNotify(){
		return notify;
	}
	
	public void setOtherInfo(String otherInfo){
		this.otherInfo = otherInfo;
	}
	
	public String getOtherInfo(){
		return otherInfo;
	}
	
}
