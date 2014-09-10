/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.icesi.invisiblefriend.services;

import co.edu.icesi.invisiblefriend.controllers.PersonasJpaController;
import co.edu.icesi.invisiblefriend.entities.Personas;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.core.util.Base64;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author David Andrés Maznzano Herrera <damanzano>
 */
@Path("personas")
public class PersonasRESTFacade {

    @Context
    ServletContext context;

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
    public Response login(Personas entity) {
        try {
            getJpaController().login(entity);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.noContent().build();
        }
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
            ex.printStackTrace();
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

    @GET
    @Path("/upload")
    @Produces({"text/plain"})
    public String uploadGetRequest() {
        return "Este es el directorio de fotos";
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({"application/json"})
    public JSONObject uploadFoto(
            @FormDataParam("files") InputStream uploadedInputStream,
            @FormDataParam("files") FormDataContentDisposition fileDetail) {

        String uploadedFileLocation = context.getRealPath("/") + context.getInitParameter("photos_directory") + "/" + fileDetail.getFileName();
        // save it
        writeToFile(uploadedInputStream, uploadedFileLocation);

        System.out.println("File uploaded to : " + uploadedFileLocation);

        //return Response.status(200).entity(output).build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", fileDetail.getFileName());
            jsonObject.put("size", fileDetail.getSize());
            jsonObject.put("url", context.getContextPath()+"/" + context.getInitParameter("photos_directory") + "/" + fileDetail.getFileName());

            
        } catch (JSONException ex) {
            Logger.getLogger(PersonasRESTFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonObject;

    }
    
    // save uploaded file to new location
    private void writeToFile(InputStream uploadedInputStream,
            String uploadedFileLocation) {

        try {
            OutputStream out = new FileOutputStream(new File(
                    uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }
}
