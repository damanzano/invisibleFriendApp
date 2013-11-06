/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.icesi.invisiblefriend.services;

import co.edu.icesi.invisiblefriend.entities.EsAmigoDePK;
import co.edu.icesi.invisiblefriend.controllers.EsAmigoDeJpaController;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.PathSegment;
import javax.naming.InitialContext;
import co.edu.icesi.invisiblefriend.entities.EsAmigoDe;
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
@Path("esamigode")
public class EsAmigoDeRESTFacade {

    private EsAmigoDePK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;numeroEsAmigo=numeroEsAmigoValue;numJuegoEsAmigo=numJuegoEsAmigoValue;numeroAmigoDe=numeroAmigoDeValue;numJuegoAmigoDe=numJuegoAmigoDeValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        co.edu.icesi.invisiblefriend.entities.EsAmigoDePK key = new co.edu.icesi.invisiblefriend.entities.EsAmigoDePK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> numeroEsAmigo = map.get("numeroEsAmigo");
        if (numeroEsAmigo != null && !numeroEsAmigo.isEmpty()) {
            key.setNumeroEsAmigo(numeroEsAmigo.get(0));
        }
        java.util.List<String> numJuegoEsAmigo = map.get("numJuegoEsAmigo");
        if (numJuegoEsAmigo != null && !numJuegoEsAmigo.isEmpty()) {
            key.setNumJuegoEsAmigo(numJuegoEsAmigo.get(0));
        }
        java.util.List<String> numeroAmigoDe = map.get("numeroAmigoDe");
        if (numeroAmigoDe != null && !numeroAmigoDe.isEmpty()) {
            key.setNumeroAmigoDe(numeroAmigoDe.get(0));
        }
        java.util.List<String> numJuegoAmigoDe = map.get("numJuegoAmigoDe");
        if (numJuegoAmigoDe != null && !numJuegoAmigoDe.isEmpty()) {
            key.setNumJuegoAmigoDe(numJuegoAmigoDe.get(0));
        }
        return key;
    }

    private EntityManagerFactory getEntityManagerFactory() throws NamingException {
        return Persistence.createEntityManagerFactory("invisibleFriendAppPU");
    }

    private EsAmigoDeJpaController getJpaController() {
        try {
            return new EsAmigoDeJpaController(getEntityManagerFactory());
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public EsAmigoDeRESTFacade() {
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public Response create(EsAmigoDe entity) {
        try {
            getJpaController().create(entity);
            return Response.created(URI.create(entity.getEsAmigoDePK().getNumeroEsAmigo() + "," + entity.getEsAmigoDePK().getNumJuegoEsAmigo() + "," + entity.getEsAmigoDePK().getNumeroAmigoDe() + "," + entity.getEsAmigoDePK().getNumJuegoAmigoDe().toString())).build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes({"application/xml", "application/json"})
    public Response edit(EsAmigoDe entity) {
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
            co.edu.icesi.invisiblefriend.entities.EsAmigoDePK key = getPrimaryKey(id);
            getJpaController().destroy(key);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public EsAmigoDe find(@PathParam("id") PathSegment id) {
        co.edu.icesi.invisiblefriend.entities.EsAmigoDePK key = getPrimaryKey(id);
        return getJpaController().findEsAmigoDe(key);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<EsAmigoDe> findAll() {
        return getJpaController().findEsAmigoDeEntities();
    }

    @GET
    @Path("{max}/{first}")
    @Produces({"application/xml", "application/json"})
    public List<EsAmigoDe> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findEsAmigoDeEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String count() {
        return String.valueOf(getJpaController().getEsAmigoDeCount());
    }
    
}
