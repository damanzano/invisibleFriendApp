/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.icesi.invisiblefriend.services;

import co.edu.icesi.invisiblefriend.controllers.PersonasJpaController;
import co.edu.icesi.invisiblefriend.entities.Personas;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import java.net.URI;
import java.util.List;
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
@Path("personas")
public class PersonasRESTFacade {

    private EntityManagerFactory getEntityManagerFactory() throws NamingException {
        return Persistence.createEntityManagerFactory("invisibleFriendAppPU");
    }

    private PersonasJpaController getJpaController() {
        try {
            return new PersonasJpaController(getEntityManagerFactory());
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public PersonasRESTFacade() {
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public Response create(Personas entity) {
        try {
            getJpaController().create(entity);
            return Response.created(URI.create(entity.getNumeroId().toString())).build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes({"application/xml", "application/json"})
    public Response edit(Personas entity) {
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
    public Personas find(@PathParam("id") String id) {
        return getJpaController().findPersonas(id);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<Personas> findAll() {
        return getJpaController().findPersonasEntities();
    }

    @GET
    @Path("{max}/{first}")
    @Produces({"application/xml", "application/json"})
    public List<Personas> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findPersonasEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String count() {
        return String.valueOf(getJpaController().getPersonasCount());
    }
    
}
