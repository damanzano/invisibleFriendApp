/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.icesi.invisiblefriend.controllers;

import co.edu.icesi.controllers.invisiblefriend.exceptions.PreexistingEntityException;
import co.edu.icesi.invisiblefriend.entities.Player;
import co.edu.icesi.invisiblefriend.login.LoginInfo;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author David Andrés Manzano Herrera <damanzano>
 */
public class PlayerJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public PlayerJpaController(EntityManagerFactory entityManagerFactory) {
        this.emf = entityManagerFactory;
    }

    public List<Player> find() {
        return find(true, -1, -1);
    }

    public List<Player> find(int maxResults, int firstResult) {
        return find(false, maxResults, firstResult);
    }

    private List<Player> find(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Player.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Player find(String key) {

        EntityManager em = getEntityManager();
        try {
            return em.find(Player.class, key);
        } finally {
            em.close();
        }
    }

    public void create(Player entity) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOne(entity.getKey()) != null) {
                throw new PreexistingEntityException("Player " + entity + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Player entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void destroy(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Object getCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Player> rt = cq.from(Player.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    private Player findOne(Key key) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Player.class, key);
        } finally {
            em.close();
        }
    }
    
    private Player findByGoogleUser(User googleUser){
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Player> q = em.createNamedQuery("Player.findByGoogleUser", Player.class);
            q.setParameter("googleUser", googleUser);
            Player player = q.getSingleResult();
            return player;
        } catch (NoResultException ex) {
            throw ex;
        } finally {
            em.close();
        }
    }

    public LoginInfo verifyCredentials() {
        UserService userService = UserServiceFactory.getUserService();
        User currentUser = userService.getCurrentUser();

        // verify if the user is authenticated
        if (currentUser != null) {
            // Look for a player that match with the current user
            Player player=null;
            try{
                player= findByGoogleUser(currentUser);
                return new LoginInfo(player, userService.createLoginURL("/"), userService.createLogoutURL("/"));
            }catch(NoResultException ex){
                // The user is authenticated but not registrated
                player = new Player();
                player.setGoogleUser(currentUser);
                return new LoginInfo(player,userService.createLoginURL("/"), userService.createLogoutURL("/"), true, "");
            }
        }
        
        // The user is not authenticated, return a message for registration in Google Accounts
        return new LoginInfo(true, "/");
    }
}
