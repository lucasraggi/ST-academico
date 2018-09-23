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
import java.util.ArrayList;
import java.util.List;

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

        return Response.ok(departmentListToDTOList(departmentDAO.getAll())).build();
    }

    @POST
    @UnitOfWork
    @Consumes("application/json")
    public Response create(DepartmentDTO entity) {
        log.info("create department: {}", entity);

        Department d = new Department(entity);
        return Response.ok(new DepartmentDTO(departmentDAO.persist(d))).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response get(@PathParam("id") Long id) {
        log.info("get department: id={}", id);

        Department d = departmentDAO.get(id);
        if (d == null) {
            return Response.status(404).entity("Department not found.").build();
        }
        return Response.ok(new DepartmentDTO(d)).build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, DepartmentDTO entity) {
        log.info("update department: id={}", id);

        Department d = departmentDAO.get(id);
        if (d == null) {
            return Response.status(404).entity("Department not found.").build();
        }
        d.update(entity);
        return Response.ok(new DepartmentDTO(departmentDAO.persist(d))).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {
        log.info("delete department: id={}", id);

        Department d = departmentDAO.get(id);
        if (d == null) {
            return Response.status(404).entity("Department not found.").build();
        }
        departmentDAO.delete(d);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/{id}/secretary")
    @UnitOfWork
    public Response getAllSecretariesFromDepartment(@PathParam("id") Long id) {
        log.info("getAll secretaries from department {}", id);

        Department d = departmentDAO.get(id);
        if (d == null) {
            return Response.status(404).entity("Department not found.").build();
        }

        ArrayList<SecretaryDTO> secretaries = new ArrayList<>();
        if (d.getGraduation() != null) {
            secretaries.add(new SecretaryDTO(d.getGraduation()));
        }
        if (d.getPostGraduation() != null) {
            secretaries.add(new SecretaryDTO(d.getPostGraduation()));
        }
        return Response.ok(secretaries).build();
    }

    @POST
    @Path("/{id}/secretary")
    @UnitOfWork
    @Consumes("application/json")
    public Response createSecretary(@PathParam("id") Long id, SecretaryDTO entity) {
        log.info("create secretary on department {}", id);

        Department d = departmentDAO.get(id);
        if (d == null) {
            return Response.status(404).entity("Department not found.").build();
        }

        if (!entity.type.toUpperCase().equals("GRADUATION") && !entity.type.toUpperCase().equals("POST-GRADUATION")) {
            return Response.status(400).entity("Invalid secretary type.").build();
        }

        Secretary s = new Secretary(entity);
        secretaryDAO.persist(s);

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
        return Response.ok(new DepartmentDTO(departmentDAO.persist(d))).build();
    }

    private List<DepartmentDTO> departmentListToDTOList(List<Department> list) {
        List<DepartmentDTO> dtoList = new ArrayList<>();
        if (list != null) {
            list.forEach(d -> dtoList.add(new DepartmentDTO(d)));
        }
        return dtoList;
    }
}
