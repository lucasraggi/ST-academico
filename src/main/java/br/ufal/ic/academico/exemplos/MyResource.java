package br.ufal.ic.academico.exemplos;

import io.dropwizard.hibernate.UnitOfWork;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Willy
 */
@Path("exemplos")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class MyResource {
    
    private final PersonDAO personDAO;
    
    @GET
    @UnitOfWork
    public Response getAll() {
        
        log.info("getAll");
        
        return Response.ok("[]").build();
    }
    
    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getById(@PathParam("id") Long id) {
        
        log.info("getById: id={}", id);
        
        Person p = personDAO.get(id);
        
        return Response.ok(p).build();
    }

    @POST
    @UnitOfWork
    @Consumes("application/json")
    public Response save(PersonDTO entity) {
        
        log.info("save: {}", entity);
        
        Person p = new Person(entity.getName());
        p.setScore(entity.getNumber());
        
        return Response.ok(personDAO.persist(p)).build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, PersonDTO entity) {
        
        log.info("update: id={}, {}", id, entity);
        
        // TODO update
        
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {
        
        log.info("delete: id={}", id);
        
        // TODO delete
        
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class PersonDTO {
        
        private String name;
        private int number;
    }
}
