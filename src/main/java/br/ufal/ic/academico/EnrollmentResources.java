package br.ufal.ic.academico;

import br.ufal.ic.academico.models.course.Course;
import br.ufal.ic.academico.models.course.CourseDAO;
import br.ufal.ic.academico.models.person.student.Student;
import br.ufal.ic.academico.models.person.student.StudentDAO;
import br.ufal.ic.academico.models.person.student.StudentDTO;
import br.ufal.ic.academico.models.person.teacher.Teacher;
import br.ufal.ic.academico.models.person.teacher.TeacherDAO;
import br.ufal.ic.academico.models.person.teacher.TeacherDTO;
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
    private final TeacherDAO teacherDAO;
    private final CourseDAO courseDAO;

    @GET
    @Path("/student")
    @UnitOfWork
    public Response getAllStudents() {
        log.info("getAll students");

        return Response.ok(studentDAO.getAll()).build();
    }

    @POST
    @Path("/student")
    @UnitOfWork
    @Consumes("application/json")
    public Response createPerson(StudentDTO entity) {
        log.info("create student: {}", entity);

        Student s = new Student(entity);
        return Response.ok(studentDAO.persist(s)).build();
    }

    @GET
    @Path("/student/{id}")
    @UnitOfWork
    public Response getStudent(@PathParam("id") Long id) {
        log.info("get student: id={}", id);

        return Response.ok(new StudentDTO(studentDAO.get(id))).build();
    }

    @PUT
    @Path("/student/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response updateStudent(@PathParam("id") Long id, StudentDTO entity) {
        log.info("update student: id={}", id);

        Student s = studentDAO.get(id);
        s.update(entity);
        return Response.ok(studentDAO.persist(s)).build();
    }

    @DELETE
    @Path("/student/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response deleteStudent(@PathParam("id") Long id) {
        log.info("delete student: id={}", id);

        Student s = studentDAO.get(id);
        studentDAO.delete(s);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/teacher")
    @UnitOfWork
    public Response getAllTeachers() {
        log.info("getAll teachers");

        return Response.ok(teacherDAO.getAll()).build();
    }

    @POST
    @Path("/teacher")
    @UnitOfWork
    @Consumes("application/json")
    public Response createPerson(TeacherDTO entity) {
        log.info("create teacher: {}", entity);

        Teacher t = new Teacher(entity);
        return Response.ok(teacherDAO.persist(t)).build();
    }

    @GET
    @Path("/teacher/{id}")
    @UnitOfWork
    public Response getTeacher(@PathParam("id") Long id) {
        log.info("get teacher: id={}", id);

        return Response.ok(new TeacherDTO(teacherDAO.get(id))).build();
    }

    @PUT
    @Path("/teacher/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response updateTeacher(@PathParam("id") Long id, TeacherDTO entity) {
        log.info("update teacher: id={}", id);

        Teacher t = teacherDAO.get(id);
        t.update(entity);
        return Response.ok(teacherDAO.persist(t)).build();
    }

    @DELETE
    @Path("/teacher/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response deleteTeacher(@PathParam("id") Long id) {
        log.info("delete teacher: id={}", id);

        Teacher t = teacherDAO.get(id);
        teacherDAO.delete(t);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Path("/student/{sId}/course/{id}")
    @UnitOfWork
    public Response enrollInCourse(@PathParam("sId") Long sId, @PathParam("id") Long id) {
        log.info("enroll student {} in course {}", sId, id);

        Course c = courseDAO.get(id);
        Student s = studentDAO.get(sId);
        if (s.getCourse() != null) {
            return Response.status(400).entity("This student is already enrolled in a course.").build();
        }
        s.setCourse(c);
        return Response.ok(studentDAO.persist(s)).build();
    }
}
