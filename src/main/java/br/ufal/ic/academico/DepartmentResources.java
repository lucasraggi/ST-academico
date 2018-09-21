package br.ufal.ic.academico;

import br.ufal.ic.academico.models.course.Course;
import br.ufal.ic.academico.models.course.CourseDAO;
import br.ufal.ic.academico.models.course.CourseDTO;
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

@Path("department")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class DepartmentResources {
    private final DepartmentDAO departmentDAO;
    private final SecretaryDAO secretaryDAO;
    private final CourseDAO courseDAO;

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
    public Response get(@PathParam("id") Long id) {
        log.info("get department: id={}", id);

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

    @GET
    @Path("/{id}/secretary")
    @UnitOfWork
    @Consumes("application/json")
    public Response getAllSecretaries(@PathParam("id") Long id) {
        log.info("getAll secretaries from department {}", id);

        ArrayList<Secretary> secretaries = new ArrayList<>();
        Department d = departmentDAO.get(id);
        if (d.getGraduation() != null) {
            secretaries.add(d.getGraduation());
        }
        if (d.getPostGraduation() != null) {
            secretaries.add(d.getPostGraduation());
        }
        return Response.ok(secretaries).build();
    }

    @POST
    @Path("/{id}/secretary")
    @UnitOfWork
    @Consumes("application/json")
    public Response createSecretary(@PathParam("id") Long id, SecretaryDTO entity) {
        log.info("create secretary on department {}", id);

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

    @GET
    @Path("/{dId}/secretary/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response getSecretaries(@PathParam("id") Long id) {
        log.info("get secretary: id={}", id);

        return Response.ok(new SecretaryDTO(secretaryDAO.get(id))).build();
    }

    @PUT
    @Path("/{dId}/secretary/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response updateSecretary(@PathParam("id") Long id, SecretaryDTO entity) {
        log.info("update secretary: id={}", id);

        Secretary s = secretaryDAO.get(id);
        s.update(entity);
        return Response.ok(secretaryDAO.persist(s)).build();
    }

    @DELETE
    @Path("/{dId}/secretary/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response deleteSecretary(@PathParam("dId") Long dId, @PathParam("id") Long id) {
        log.info("delete secretary: id={}", id);

        Department d = departmentDAO.get(dId);
        Secretary s = secretaryDAO.get(id);

        if (s.getType().equals("GRADUATION")) {
            d.setGraduation(null);
        } else {
            d.setPostGraduation(null);
        }
        departmentDAO.persist(d);

        secretaryDAO.delete(s);
        return Response.noContent().build();
    }

    @GET
    @Path("/{dId}/secretary/{id}/course")
    @UnitOfWork
    @Consumes("application/json")
    public Response getAllCourses(@PathParam("dId") Long dId, @PathParam("id") Long id) {
        log.info("getAll courses from secretary {}", id);

        Secretary s = secretaryDAO.get(id);
        return Response.ok(s.getCourses()).build();
    }

    @POST
    @Path("/{dId}/secretary/{id}/course")
    @UnitOfWork
    @Consumes("application/json")
    public Response createCourse(@PathParam("id") Long id, CourseDTO entity) {
        log.info("create course on secretary {}", id);

        Secretary s = secretaryDAO.get(id);
        Course c = s.addCourse(entity);
        if (c != null) {
            courseDAO.persist(c);
        }
        return Response.ok(secretaryDAO.persist(s)).build();
    }

    @GET
    @Path("/{dId}/secretary/{sId}/course/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response getCourse(@PathParam("id") Long id) {
        log.info("get course: id={}", id);

        return Response.ok(courseDAO.get(id)).build();
    }

    @PUT
    @Path("/{dId}/secretary/{sId}/course/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response updateCourse(@PathParam("id") Long id, CourseDTO entity) {
        log.info("update course: id={}", id);

        Course c = courseDAO.get(id);
        c.update(entity);
        return Response.ok(courseDAO.persist(c)).build();
    }

    @DELETE
    @Path("/{dId}/secretary/{sId}/course/{id}")
    @UnitOfWork
    public Response deleteCourse(@PathParam("sId") Long sId, @PathParam("id") Long id) {
        log.info("delete course {}", id);

        Course c = courseDAO.get(id);

        Secretary s = secretaryDAO.get(sId);
        s.deleteCourse(c);
        secretaryDAO.persist(s);

        courseDAO.delete(c);
        return Response.noContent().build();
    }
}
