/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.icesi.invisiblefriend.services;

import co.edu.icesi.invisiblefriend.entities.ParticipantesPK;
import co.edu.icesi.invisiblefriend.controllers.ParticipantesJpaController;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.PathSegment;
import co.edu.icesi.invisiblefriend.entities.Participantes;
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
@Path("participantes")
public class ParticipantesRESTFacade {

    private ParticipantesPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;numeroJuego=numeroJuegoValue;numeroPersona=numeroPersonaValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        co.edu.icesi.invisiblefriend.entities.ParticipantesPK key = new co.edu.icesi.invisiblefriend.entities.ParticipantesPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> numeroJuego = map.get("numeroJuego");
        if (numeroJuego != null && !numeroJuego.isEmpty()) {
            key.setNumeroJuego(numeroJuego.get(0));
        }
        java.util.List<String> numeroPersona = map.get("numeroPersona");
        if (numeroPersona != null && !numeroPersona.isEmpty()) {
            key.setNumeroPersona(numeroPersona.get(0));
        }
        return key;
    }

    private EntityManagerFactory getEntityManagerFactory() throws NamingException {
        return Persistence.createEntityManagerFactory("invisibleFriendAppPU");
    }

    private ParticipantesJpaController getJpaController() {
        try {
            return new ParticipantesJpaController(getEntityManagerFactory());
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ParticipantesRESTFacade() {
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public Response create(Participantes entity) {
        try {
            getJpaController().create(entity);
            return Response.created(URI.create(entity.getParticipantesPK().getNumeroJuego() + "," + entity.getParticipantesPK().getNumeroPersona().toString())).build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes({"application/xml", "application/json"})
    public Response edit(Participantes entity) {
        try {
            getJpaController().edit(entity);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") PathSegment id) {
        try {
            co.edu.icesi.invisiblefriend.entities.ParticipantesPK key = getPrimaryKey(id);
            getJpaController().destroy(key);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Participantes find(@PathParam("id") PathSegment id) {
        co.edu.icesi.invisiblefriend.entities.ParticipantesPK key = getPrimaryKey(id);
        return getJpaController().findParticipantes(key);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<Participantes> findAll() {
        return getJpaController().findParticipantesEntities();
    }

    @GET
    @Path("{max}/{first}")
    @Produces({"application/xml", "application/json"})
    public List<Participantes> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findParticipantesEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String count() {
        return String.valueOf(getJpaController().getParticipantesCount());
    }
    
}
