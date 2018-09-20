package br.ufal.ic.academico;

import br.ufal.ic.academico.models.person.student.Student;
import br.ufal.ic.academico.models.person.student.StudentDAO;
import br.ufal.ic.academico.models.person.student.StudentDTO;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("enrollment")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class EnrollmentResources {
    private final StudentDAO studentDAO;

    @GET
    @UnitOfWork
    public Response getAll() {
        log.info("getAll students");
        return Response.ok(studentDAO.getAll()).build();
    }

    @POST
    @UnitOfWork
    @Consumes("application/json")
    public Response createStudent(StudentDTO entity) {
        log.info("create student: {}", entity);

        Student s = new Student(entity);
        return Response.ok(studentDAO.persist(s)).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getStudent(@PathParam("id") Long id) {
        return Response.ok(new StudentDTO(studentDAO.get(id))).build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response updateStudent(@PathParam("id") Long id, StudentDTO entity) {
        log.info("update student: id={}", id);

        Student s = studentDAO.get(id);
        s.update(entity);
        return Response.ok(studentDAO.persist(s)).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response deleteStudent(@PathParam("id") Long id) {
        log.info("delete student: id={}", id);

        Student s = studentDAO.get(id);
        studentDAO.delete(s);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
