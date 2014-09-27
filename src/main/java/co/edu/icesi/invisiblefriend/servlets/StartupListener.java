/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.icesi.invisiblefriend.servlets;

import co.edu.icesi.invisiblefriend.controllers.GameJpaController;
import co.edu.icesi.invisiblefriend.controllers.PlayerJpaController;
import co.edu.icesi.invisiblefriend.entities.Game;
import co.edu.icesi.invisiblefriend.entities.Player;
import com.google.appengine.api.users.User;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author David Andrés Manzano Herrera <damanzano>
 */
public class StartupListener implements ServletContextListener{

    public void contextInitialized(ServletContextEvent sce) {
        // Create data to populate de database;

        // Get the JpaControllers
        GameJpaController gameContrl = getGameJpaController();
        PlayerJpaController playerContrl = getPlayerJpaController();

        // Create player
        Player player = new Player();
        User googleUser = new User("damh24@gmail.com", "gmail.com");
        player.setGoogleUser(googleUser);
        player.setName("David Andrés");
        player.setLastname("Manzano Herrera");
        player.setGender("M");
        player.setLocation("Oficina de Desarrollo de Sistemas");

        try {
            // Persists player
            playerContrl.create(player);
            
            // Verify if the object was created
            String playerCount= playerContrl.getCount().toString();
            Logger.getLogger(StartupListener.class.getName()).log(Level.INFO, "Player count: {0}", playerCount);
            
            //
        } catch (Exception ex) {
            Logger.getLogger(StartupListener.class.getName()).log(Level.SEVERE, null, ex);
        }

        Game game = new Game();
        game.setCreationDate(new Date());
        game.setDescription("(Desarollo) Amigo secreto Navidad");
        game.setCreatedBy(player.getKey());
        game.setStartDate(new Date());
        game.setEndDate(new Date());

        try {
            // Persists game
            gameContrl.create(game);
            
            // Verify if the object was created
            String gameCount= gameContrl.getCount().toString();
            Logger.getLogger(StartupListener.class.getName()).log(Level.INFO, "Game count: {0}", gameCount);
            
            //
        } catch (Exception ex) {
            Logger.getLogger(StartupListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private EntityManagerFactory getEntityManagerFactory() throws NamingException {
        return Persistence.createEntityManagerFactory("invisibleFriendAppPU");
    }

    private GameJpaController getGameJpaController() {

        try {
            return new GameJpaController(getEntityManagerFactory());
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }

    private PlayerJpaController getPlayerJpaController() {
        try {
            return new PlayerJpaController(getEntityManagerFactory());
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
