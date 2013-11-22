/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.icesi.invisiblefriend.services;

import co.edu.icesi.invisiblefriend.controllers.JuegoJpaController;
import co.edu.icesi.invisiblefriend.entities.Juego;
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
 * @author David Andrés Maznzano Herrera <damanzano>
 */
@Path("juegos")
public class JuegoRESTFacade {
    private EntityManagerFactory getEntityManagerFactory() throws NamingException {
        //return (EntityManagerFactory) new InitialContext().lookup("java:comp/env/persistence-factory");
        return Persistence.createEntityManagerFactory("invisibleFriendAppPU");
        
    }
    
    private JuegoJpaController getJpaController() {
        try {
            return new JuegoJpaController(getEntityManagerFactory());
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public JuegoRESTFacade() {
    }
    
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response create(Juego entity) {
        try {
            getJpaController().create(entity);
            return Response.created(URI.create(entity.getNumeroId().toString())).build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes({"application/xml", "application/json"})
    public Response edit(Juego entity) {
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
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Juego find(@PathParam("id") String id) {
        return getJpaController().findJuego(id);
    }

    @GET
    @Produces({"application/json", "application/xml"})
    public List<Juego> findAll() {
        return getJpaController().findJuegoEntities();
    }
    
    @GET
    @Path("{max}/{first}")
    @Produces({"application/xml", "application/json"})
    public List<Juego> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findJuegoEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces({"application/json"})
    public String count() {
        return String.valueOf(getJpaController().getJuegoCount());
    }
}
