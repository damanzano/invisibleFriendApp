/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.icesi.invisiblefriend.login;

import co.edu.icesi.invisiblefriend.entities.Player;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 *
 * @author David Andrés Manzano Herrera <damanzano>
 */
public class LoginInfo {

    private final Player currentPlayer;
    private final String loginUrl;
    private final String logoutUrl;
    private final boolean registrationNeeded;
    private final String registrationUrl;

    public LoginInfo(Player currentPlayer, String loginUrl, String logoutUrl, boolean registrationNeeded, String registrationUrl) {
        this.currentPlayer = currentPlayer;
        this.loginUrl = loginUrl;
        this.logoutUrl = logoutUrl;
        this.registrationNeeded=registrationNeeded;
        this.registrationUrl=registrationUrl;
    }
    
    public LoginInfo(Player currentPlayer, String loginUrl, String logoutUrl) {
        this(currentPlayer, loginUrl, logoutUrl, false,"");
    }
    
    public LoginInfo(boolean registrationNeeded, String registrationUrl){
        this(null,null, null, registrationNeeded, registrationUrl);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public boolean isRegistrationNeeded() {
        return registrationNeeded;
    }

    public String getRegistrationUrl() {
        return registrationUrl;
    }
    
}
