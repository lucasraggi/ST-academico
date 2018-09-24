package br.ufal.ic.academico;

import br.ufal.ic.academico.models.course.Course;
import br.ufal.ic.academico.models.course.CourseDAO;
import br.ufal.ic.academico.models.department.Department;
import br.ufal.ic.academico.models.discipline.Discipline;
import br.ufal.ic.academico.models.discipline.DisciplineDAO;
import br.ufal.ic.academico.models.discipline.DisciplineDTO;
import br.ufal.ic.academico.models.person.student.Student;
import br.ufal.ic.academico.models.person.student.StudentDAO;
import br.ufal.ic.academico.models.person.student.StudentDTO;
import br.ufal.ic.academico.models.person.teacher.Teacher;
import br.ufal.ic.academico.models.person.teacher.TeacherDAO;
import br.ufal.ic.academico.models.person.teacher.TeacherDTO;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("enrollment")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class EnrollmentResources {
    private final StudentDAO studentDAO;
    private final TeacherDAO teacherDAO;
    private final CourseDAO courseDAO;
    private final DisciplineDAO disciplineDAO;

    @GET
    @Path("/student")
    @UnitOfWork
    public Response getAllStudents() {
        log.info("getAll students");

        return Response.ok(studentListToDTOList(studentDAO.getAll())).build();
    }

    @POST
    @Path("/student")
    @UnitOfWork
    @Consumes("application/json")
    public Response createPerson(StudentDTO entity) {
        log.info("create student: {}", entity);

        if (entity.firstName == null) {
            return Response.status(400).entity("First Name required.").build();
        }
        Student s = new Student(entity);
        return Response.ok(new StudentDTO(studentDAO.persist(s))).build();
    }

    @GET
    @Path("/student/{id}")
    @UnitOfWork
    public Response getStudent(@PathParam("id") Long id) {
        log.info("get student: id={}", id);

        Student s = studentDAO.get(id);
        if (s == null) {
            return Response.status(404).entity("Student not found.").build();
        }
        return Response.ok(new StudentDTO(s)).build();
    }

    @PUT
    @Path("/student/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response updateStudent(@PathParam("id") Long id, StudentDTO entity) {
        log.info("update student: id={}", id);

        Student s = studentDAO.get(id);
        if (s == null) {
            return Response.status(404).entity("Student not found.").build();
        }
        s.update(entity);
        return Response.ok(new StudentDTO(studentDAO.persist(s))).build();
    }
// ToDo Resolver esse DELETE
//    @DELETE
//    @Path("/student/{id}")
//    @UnitOfWork
//    public Response deleteStudent(@PathParam("id") Long id) {
//        log.info("delete student: id={}", id);
//
//        Student s = studentDAO.get(id);
//        if (s == null) {
//            return Response.status(404).entity("Student not found.").build();
//        }
//        studentDAO.delete(s);
//        return Response.status(Response.Status.NO_CONTENT).build();
//    }

    @GET
    @Path("/teacher")
    @UnitOfWork
    public Response getAllTeachers() {
        log.info("getAll teachers");

        return Response.ok(teacherListToDTOList(teacherDAO.getAll())).build();
    }

    @POST
    @Path("/teacher")
    @UnitOfWork
    @Consumes("application/json")
    public Response createPerson(TeacherDTO entity) {
        log.info("create teacher: {}", entity);

        if (entity.firstName == null) {
            return Response.status(400).entity("First Name required.").build();
        }
        Teacher t = new Teacher(entity);
        return Response.ok(new TeacherDTO(teacherDAO.persist(t))).build();
    }

    @GET
    @Path("/teacher/{id}")
    @UnitOfWork
    public Response getTeacher(@PathParam("id") Long id) {
        log.info("get teacher: id={}", id);

        Teacher t = teacherDAO.get(id);
        if (t == null) {
            return Response.status(404).entity("Teacher not found.").build();
        }
        return Response.ok(new TeacherDTO(t)).build();
    }

    @PUT
    @Path("/teacher/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response updateTeacher(@PathParam("id") Long id, TeacherDTO entity) {
        log.info("update teacher: id={}", id);

        Teacher t = teacherDAO.get(id);
        if (t == null) {
            return Response.status(404).entity("Teacher not found.").build();
        }
        t.update(entity);
        return Response.ok(new TeacherDTO(teacherDAO.persist(t))).build();
    }
// ToDo Resolver esse DELETE
//    @DELETE
//    @Path("/teacher/{id}")
//    @UnitOfWork
//    public Response deleteTeacher(@PathParam("id") Long id) {
//        log.info("delete teacher: id={}", id);
//
//        Teacher t = teacherDAO.get(id);
//        if (t == null) {
//            return Response.status(404).entity("Teacher not found.").build();
//        }
//        teacherDAO.delete(t);
//        return Response.status(Response.Status.NO_CONTENT).build();
//    }

    @POST
    @Path("/teacher/{tId}/discipline/{id}")
    @UnitOfWork
    public Response allocateTeacher(@PathParam("tId") Long tId, @PathParam("id") Long id) {
        log.info("allocate teacher {} in discipline {}", tId, id);

        Teacher t = teacherDAO.get(tId);
        if (t == null) {
            return Response.status(404).entity("Teacher not found.").build();
        }
        Discipline d = disciplineDAO.get(id);
        if (d == null) {
            return Response.status(404).entity("Discipline not found.").build();
        }

        d.setTeacher(t);
        return Response.ok(new DisciplineDTO(disciplineDAO.persist(d))).build();
    }

    @POST
    @Path("/student/{sId}/course/{id}")
    @UnitOfWork
    public Response enrollInCourse(@PathParam("sId") Long sId, @PathParam("id") Long id) {
        log.info("enroll student {} in course {}", sId, id);

        Course c = courseDAO.get(id);
        if (c == null) {
            return Response.status(404).entity("Course not found.").build();
        }

        Student s = studentDAO.get(sId);
        if (s == null) {
            return Response.status(404).entity("Student not found.").build();
        }
        if (s.getCourse() != null) {
            return Response.status(400).entity("Student already enrolled in a course.").build();
        }
        s.setCourse(c);
        return Response.ok(new StudentDTO(studentDAO.persist(s))).build();
    }

    @GET
    @Path("/student/{sId}/discipline")
    @UnitOfWork
    public Response getAllDisciplinesFromStudentDepartment(@PathParam("sId") Long sId) {
        log.info("getAll disciplines from student {} department", sId);

        Student student = studentDAO.get(sId);
        if (student == null) {
            return Response.status(404).entity("Student not found.").build();
        }
        Department department = studentDAO.getDepartment(student);
        if (department == null) {
            return Response.status(400).entity("Student isn't enrolled in any course.").build();
        }

        List<DisciplineDTO> dtoList = new ArrayList<>();
        if (department.getGraduation() != null) {
            for (Course c : department.getGraduation().getCourses()) {
                List<Discipline> res = c.getDisciplines();
                if (res != null) {
                    dtoList.addAll(res.stream().map(DisciplineDTO::new).collect(Collectors.toList()));
                }
            }
        }
        return Response.ok(dtoList).build();
    }

    @POST
    @Path("/student/{sId}/discipline/{id}")
    @UnitOfWork
    public Response enrollStudentInDiscipline(@PathParam("sId") Long sId, @PathParam("id") Long id) {
        log.info("enroll student {} in discipline {}", sId, id);

        Student s = studentDAO.get(sId);
        if (s == null) {
            return Response.status(404).entity("Student not found.").build();
        }
        Discipline d = disciplineDAO.get(id);
        if (d == null) {
            return Response.status(404).entity("Discipline not found.").build();
        }

        String res = d.enroll(s, studentDAO.getDepartment(s), disciplineDAO.getDepartment(d),
                studentDAO.getSecretary(s), disciplineDAO.getSecretary(d));
        if (res != null) {
            return Response.status(400).entity(res).build();
        }
        return Response.ok(new DisciplineDTO(disciplineDAO.persist(d))).build();
    }

    @POST
    @Path("/student/{sId}/complete/{id}")
    @UnitOfWork
    public Response completeDiscipline(@PathParam("sId") Long sId, @PathParam("id") Long id) {
        log.info("complete discipline {}, student {}", id, sId);

        Student s = studentDAO.get(sId);
        if (s == null) {
            return Response.status(404).entity("Student not found.").build();
        }
        Discipline d = disciplineDAO.get(id);
        if (d == null) {
            return Response.status(404).entity("Discipline not found.").build();
        }
        if (!d.removeStudent(s)) {
            return Response.status(400).entity("Student isn't enrolled at this discipline.").build();
        }

        s.completeDiscipline(d);
        disciplineDAO.persist(d);
        return Response.ok(new StudentDTO(studentDAO.persist(s))).build();
    }

    @GET
    @Path("/proof/{id}")
    @UnitOfWork
    public Response enrollmentProof(@PathParam("id") Long id) {
        log.info("enrollment proof of student {}", id);

        Student student = studentDAO.get(id);
        if (student == null) {
            return Response.status(404).entity("Student not found.").build();
        }

        List<Discipline> disciplines = disciplineDAO.getAllByStudent(student);

        List<ProofDiscipline> disciplineProofs = new ArrayList<>();
        for (Discipline d : disciplines) {
            disciplineProofs.add(new ProofDiscipline(d.getCode(), d.getName()));
        }
        return Response.ok(new Proof(student.getId(), student.getFirstname() + (student.getLastName() != null ? " " + student.getLastName() : ""),
                disciplineProofs)).build();
    }

    private List<StudentDTO> studentListToDTOList(List<Student> list) {
        List<StudentDTO> dtoList = new ArrayList<>();
        if (list != null) {
            list.forEach(s -> dtoList.add(new StudentDTO(s)));
        }
        return dtoList;
    }

    private List<TeacherDTO> teacherListToDTOList(List<Teacher> list) {
        List<TeacherDTO> dtoList = new ArrayList<>();
        if (list != null) {
            list.forEach(s -> dtoList.add(new TeacherDTO(s)));
        }
        return dtoList;
    }

    @Getter
    @AllArgsConstructor
    @RequiredArgsConstructor
    @ToString
    private class Proof {
        Long id;
        String name;
        List<ProofDiscipline> disciplines;
    }

    @Getter
    @AllArgsConstructor
    @RequiredArgsConstructor
    @ToString
    private class ProofDiscipline {
        String code, name;
    }
}
