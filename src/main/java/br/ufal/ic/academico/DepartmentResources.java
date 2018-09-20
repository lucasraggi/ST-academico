package br.ufal.ic.academico;

import br.ufal.ic.academico.models.department.Department;
import br.ufal.ic.academico.models.department.DepartmentDAO;
import br.ufal.ic.academico.models.department.DepartmentDTO;
import br.ufal.ic.academico.models.secretary.Secretary;
import br.ufal.ic.academico.models.secretary.SecretaryDAO;
import br.ufal.ic.academico.models.secretary.SecretaryDTO;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("department")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class DepartmentResources {
    private final DepartmentDAO departmentDAO;
    private final SecretaryDAO secretaryDAO;

    @GET
    @UnitOfWork
    public Response getAll() {
        log.info("getAll departments");
        return Response.ok(departmentDAO.getAll()).build();
    }

    @POST
    @UnitOfWork
    @Consumes("application/json")
    public Response create(DepartmentDTO entity) {
        log.info("create department: {}", entity);

        Department d = new Department(entity);
        return Response.ok(departmentDAO.persist(d)).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getStudent(@PathParam("id") Long id) {
        return Response.ok(new DepartmentDTO(departmentDAO.get(id))).build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, DepartmentDTO entity) {
        log.info("update department: id={}", id);

        Department d = departmentDAO.get(id);
        d.update(entity);
        return Response.ok(departmentDAO.persist(d)).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response delete(@PathParam("id") Long id) {
        log.info("delete department: id={}", id);

        Department d = departmentDAO.get(id);
        departmentDAO.delete(d);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Path("/{id}/secretary")
    @UnitOfWork
    @Consumes("application/json")
    public Response createSecretary(@PathParam("id") Long id, SecretaryDTO entity) {
        log.info("create secretary");

        Secretary s = new Secretary(entity);
        secretaryDAO.persist(s);

        Department d = departmentDAO.get(id);
        if (s.getType().equals("GRADUATION")) {
            if (d.getGraduation() != null) {
                return Response.status(400).entity("Graduation Secretary already exists on this department.").build();
            }
            d.setGraduation(s);
        } else {
            if (d.getPostGraduation() != null) {
                return Response.status(400).entity("Post Graduation Secretary already exists on this department.").build();
            }
            d.setPostGraduation(s);
        }
        return Response.ok(departmentDAO.persist(d)).build();
    }

    @DELETE
    @Path("/{id}/secretary/{sId}")
    @UnitOfWork
    @Consumes("application/json")
    public Response deleteSecretary(@PathParam("id") Long id, @PathParam("sId") Long sId) {
        Department d = departmentDAO.get(id);
        Secretary s = secretaryDAO.get(sId);

        if (s.getType().equals("GRADUATION")) {
            d.setGraduation(null);
        } else {
            d.setPostGraduation(null);
        }
        departmentDAO.persist(d);

        secretaryDAO.delete(s);
        return Response.noContent().build();
    }
}
