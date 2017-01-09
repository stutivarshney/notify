/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.model;

/**
 *
 * @author Aj
 */
public class CurrentUser {
    private static Users cu = new Users();
                         
    public CurrentUser(Users u){
        cu.setUserID(u.getUserID());
        cu.setUsername(u.getUsername());
        cu.setPassword(u.getPassword());
        cu.setfName(u.getfName());
        cu.setlName(u.getlName());
    }
    
    public static Users currentUser(){
        return cu;
    }
    
    public static void removeCurrentUser(){
        cu = new Users();
    }
}
