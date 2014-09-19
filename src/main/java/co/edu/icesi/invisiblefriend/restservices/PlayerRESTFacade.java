/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.icesi.invisiblefriend.restservices;

import co.edu.icesi.invisiblefriend.controllers.PlayerJpaController;
import co.edu.icesi.invisiblefriend.entities.Player;
import co.edu.icesi.invisiblefriend.login.LoginInfo;
import java.net.URI;
import java.util.List;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author David Andrés Manzano Herrera <damanzano>
 */
@Path("/players")
public class PlayerRESTFacade {

    private EntityManagerFactory getEntityManagerFactory() throws NamingException {
        return Persistence.createEntityManagerFactory("invisibleFriendAppPU");
    }

    private PlayerJpaController getJpaController() {
        try {
            return new PlayerJpaController(getEntityManagerFactory());
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @GET
    @Produces({"application/json"})
    public List<Player> findAll() {
        return getJpaController().find();
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public Player find(@PathParam("id") String id) {
        return getJpaController().find(id);
    }

    @POST
    @Consumes({"application/json"})
    public Response create(Player entity) {
        try {
            getJpaController().create(entity);
            return Response.created(URI.create(entity.getKey().toString())).build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }
    
    @GET
    @Path("/login")
    @Produces({"application/json"})
    public LoginInfo Login() {
            return getJpaController().verifyCredentials();
    }

    @PUT
    @Consumes({"application/json"})
    public Response edit(Player entity) {
        try {
            getJpaController().edit(entity);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") String id) {
        try {
            getJpaController().destroy(id);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @GET
    @Path("count")
    @Produces({"application/json"})
    public String count() {
        return String.valueOf(getJpaController().getCount());
    }

}
