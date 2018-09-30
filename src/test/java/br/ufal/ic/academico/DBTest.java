package br.ufal.ic.academico;

import br.ufal.ic.academico.models.course.Course;
import br.ufal.ic.academico.models.course.CourseDAO;
import br.ufal.ic.academico.models.course.CourseDTO;
import br.ufal.ic.academico.models.department.Department;
import br.ufal.ic.academico.models.department.DepartmentDAO;
import br.ufal.ic.academico.models.discipline.Discipline;
import br.ufal.ic.academico.models.person.student.Student;
import br.ufal.ic.academico.models.person.student.StudentDAO;
import br.ufal.ic.academico.models.person.teacher.Teacher;
import br.ufal.ic.academico.models.person.teacher.TeacherDAO;
import br.ufal.ic.academico.models.secretary.Secretary;
import br.ufal.ic.academico.models.secretary.SecretaryDAO;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Random;

@ExtendWith(DropwizardExtensionsSupport.class)
class DBTest {
    
    private DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
            .addEntityClass(Student.class)
            .addEntityClass(Teacher.class)
            .addEntityClass(Department.class)
            .addEntityClass(Secretary.class)
            .addEntityClass(Course.class)
            .addEntityClass(Discipline.class)
            .build();
    
    @Test
    void studentCRUD() {
        StudentDAO dao = new StudentDAO(dbTesting.getSessionFactory());

        final Student s1 = new Student("Daniel", "Humberto Cavalcante Vassalo");
        final Student savedS1 = dbTesting.inTransaction(() -> dao.persist(s1));

        assertNotNull(savedS1, "Falhou ao salvar um novo Student");
        assertNotNull(savedS1.getId(), "Student não recebeu um id ao ser criado");
        assertEquals(s1.getFirstname(), savedS1.getFirstname(), "First name do Student não corresponde com o informado");
        assertEquals(s1.getLastName(), savedS1.getLastName(), "Last name do Student não corresponde com o informado");
        assertEquals(new Integer(0), savedS1.getCredits(), "Student foi cadastro com 'credits' diferente de 0");
        assertNull(savedS1.getCourse(), "Student recebeu um curso ao ser criado");
        assertEquals(new ArrayList<>(), savedS1.getCompletedDisciplines(),
                "Student recebeu uma lista de matérias concluídas ao ser criado");
        assertNull(dbTesting.inTransaction(() -> dao.getDepartment(savedS1)), "Student foi vinculado à um Department");
        assertNull(dbTesting.inTransaction(() -> dao.getSecretary(savedS1)), "Student foi vinculado à uma Secretary");

        for (int i = 0; i < 50; i++) {
            Integer credits = new Random().nextInt();
            s1.setCredits(credits);
            final Student updatedS1 = dbTesting.inTransaction(() -> dao.persist(s1));
            assertEquals(credits, updatedS1.getCredits(), "Créditos não foram atualizados corretamente");
        }
        s1.setLastName("Dan");
        s1.setLastName("Vassalo");
        final Student updatedS1 = dbTesting.inTransaction(() -> dao.persist(s1));
        assertEquals(s1.getFirstname(), updatedS1.getFirstname(), "First name não foi atualizado corretamente");
        assertEquals(s1.getLastName(), updatedS1.getLastName(), "Last name não foi atualizado corretamente");

        dbTesting.inTransaction(() -> dao.delete(updatedS1));
        assertNull(dbTesting.inTransaction(() -> dao.get(s1.getId())), "Student não foi removido");
        assertEquals(0, dbTesting.inTransaction(dao::getAll).size(),
                "Student não foi removido da listagem de todos os Student");

        final Student s2 = new Student("Lucas", "Raggi");
        final Student s3 = new Student("Gabriel", "Barbosa");
        final Student savedS2 = dbTesting.inTransaction(() -> dao.persist(s2));
        final Student savedS3 = dbTesting.inTransaction(() -> dao.persist(s3));

        assertNotNull(savedS2, "Falhou ao salvar um segundo novo Student");
        assertNotNull(savedS3, "Falhou ao salvar um terceiro novo Student");
        assertEquals(2, dbTesting.inTransaction(dao::getAll).size(),
                "Nem todos os novos Students estão aparecendo na listagem total de Students");
        dbTesting.inTransaction(() -> dao.delete(s2));
        assertNull(dbTesting.inTransaction(() -> dao.get(s2.getId())), "Student não foi removido");
        assertEquals(1, dbTesting.inTransaction(dao::getAll).size(),
                "Student não foi removido da listagem de todos os Students");
    }

    @Test
    void teacherCRUD() {
        TeacherDAO dao = new TeacherDAO(dbTesting.getSessionFactory());

        final Teacher t1 = new Teacher("Willy", "Carvalho Tiengo");
        final Teacher savedT1 = dbTesting.inTransaction(() -> dao.persist(t1));

        assertNotNull(savedT1, "Falhou ao salvar um novo Teacher");
        assertNotNull(savedT1.getId(), "Teacher não recebeu um id ao ser criado");
        assertEquals(t1.getFirstname(), savedT1.getFirstname(), "First name do Teacher não corresponde com o informado");
        assertEquals(t1.getLastName(), savedT1.getLastName(), "Last name do Teacher não corresponde com o informado");

        t1.setLastName("Will");
        t1.setLastName("Tiengo");
        final Teacher updatedT1 = dbTesting.inTransaction(() -> dao.persist(t1));
        assertEquals(t1.getFirstname(), updatedT1.getFirstname(), "First name não foi atualizado corretamente");
        assertEquals(t1.getLastName(), updatedT1.getLastName(), "Last name não foi atualizado corretamente");

        dbTesting.inTransaction(() -> dao.delete(updatedT1));
        assertNull(dbTesting.inTransaction(() -> dao.get(t1.getId())), "Teacher não foi removido");
        assertEquals(0, dbTesting.inTransaction(dao::getAll).size(),
                "Teacher não foi removido da listagem de todos os Teachers");

        final Teacher t2 = new Teacher("Rodrigo", "Paes");
        final Teacher t3 = new Teacher("Márcio", "Ribeiro");
        final Teacher savedT2 = dbTesting.inTransaction(() -> dao.persist(t2));
        final Teacher savedT3 = dbTesting.inTransaction(() -> dao.persist(t3));

        assertNotNull(savedT2, "Falhou ao salvar um segundo novo Teacher");
        assertNotNull(savedT3, "Falhou ao salvar um terceiro novo Teacher");
        assertEquals(2, dbTesting.inTransaction(dao::getAll).size(),
                "Nem todos os novos Teachers estão aparecendo na listagem total de Teachers");
        dbTesting.inTransaction(() -> dao.delete(t2));
        assertNull(dbTesting.inTransaction(() -> dao.get(t2.getId())), "Teacher não foi removido");
        assertEquals(1, dbTesting.inTransaction(dao::getAll).size(),
                "Teacher não foi removido da listagem de todos os Teachers");
    }

    @Test
    void departmentCRUD() {
        DepartmentDAO dao = new DepartmentDAO(dbTesting.getSessionFactory());

        final Department d1 = new Department("IC");
        final Department savedD1 = dbTesting.inTransaction(() -> dao.persist(d1));

        assertNotNull(savedD1, "Falhou ao salvar um novo Department");
        assertNotNull(savedD1.getId(), "Department não recebeu um id ao ser criado");
        assertEquals(d1.getName(), savedD1.getName(), "Name do Department não corresponde com o informado");
        assertNull(savedD1.getGraduation(), "Department recebeu uma secretaria de graduação ao ser criado");
        assertNull(savedD1.getPostGraduation(), "Department recebeu uma secretaria de pós graduação ao ser criado");

        d1.setName("FDA");
        d1.setGraduation(new Secretary());
        d1.setPostGraduation(new Secretary());
        final Department updatedD1 = dbTesting.inTransaction(() -> dao.persist(d1));
        assertEquals(d1.getName(), updatedD1.getName(), "Name do Department não foi atualizado corretamente");
        assertEquals(d1.getGraduation().getId(), updatedD1.getGraduation().getId(),
                "Secretaria de graduação associada incorretamente");
        assertEquals(d1.getPostGraduation().getId(), updatedD1.getPostGraduation().getId(),
                "Secretaria de pós graduação associada incorretamente");

        dbTesting.inTransaction(() -> dao.delete(updatedD1));
        assertNull(dbTesting.inTransaction(() -> dao.get(d1.getId())), "Department não foi removido");
        assertEquals(0, dbTesting.inTransaction(dao::getAll).size(),
                "Department não foi removido da listagem total de Department");

        final Department d2 = new Department("ICBS");
        final Department d3 = new Department("COS");
        final Department savedD2 = dbTesting.inTransaction(() -> dao.persist(d2));
        final Department savedD3 = dbTesting.inTransaction(() -> dao.persist(d3));

        assertNotNull(savedD2, "Falhou ao salvar um segundo novo Department");
        assertNotNull(savedD3, "Falhou ao salvar um terceiro novo Department");
        assertEquals(2, dbTesting.inTransaction(dao::getAll).size(),
                "Nem todos os novos Departments estão aparecendo na listagem total de Departments");
        dbTesting.inTransaction(() -> dao.delete(d2));
        assertNull(dbTesting.inTransaction(() -> dao.get(d2.getId())), "Department não foi removido");
        assertEquals(1, dbTesting.inTransaction(dao::getAll).size(),
                "Department não foi removido da listagem total de Departments");
    }

    @Test
    void secretaryCRUD() {
        SecretaryDAO dao = new SecretaryDAO(dbTesting.getSessionFactory());

        final Secretary s1 = new Secretary("GRADUATION");
        final Secretary savedS1 = dbTesting.inTransaction(() -> dao.persist(s1));

        assertNotNull(savedS1, "Falhou ao salvar uma nova Secretary");
        assertNotNull(savedS1.getId(), "Secretary não recebeu um id ao ser criada");
        assertEquals("GRADUATION", savedS1.getType(), "Tipo da Secretary não corresponde com o informado (GRADUATION)");
        assertEquals(0, savedS1.getCourses().size(), "Secretary foi criada com Course(s) associado(s)");

        s1.addCourse(new CourseDTO());
        final Secretary updatedS1 = dbTesting.inTransaction(() -> dao.persist(s1));
        assertEquals("GRADUATION", updatedS1.getType(), "Tipo da Secretary foi alterado");
        assertEquals(1, updatedS1.getCourses().size(), "Curso associado não foi salvo corretamente");

        dbTesting.inTransaction(() -> dao.delete(updatedS1));
        assertNull(dbTesting.inTransaction(() -> dao.get(s1.getId())), "Secretary não foi removida");
        assertEquals(0, dbTesting.inTransaction(dao::getAll).size(),
                "Secretary não foi removida da listagem total de Secretaries");

        final Secretary s2 = new Secretary("POST-GRADUATION");
        final Secretary s3 = new Secretary("GRADUATION");
        final Secretary savedS2 = dbTesting.inTransaction(() -> dao.persist(s2));
        final Secretary savedS3 = dbTesting.inTransaction(() -> dao.persist(s3));

        assertNotNull(savedS2, "Falhou ao salvar uma segunda nova Secretary");
        assertNotNull(savedS3, "Falhou ao salvar uma terceira nova Secretary");
        assertEquals(2, dbTesting.inTransaction(dao::getAll).size(),
                "Nem todas as novas Secretaries estão aparecendo na listagem total de Secretaries");
        dbTesting.inTransaction(() -> dao.delete(s2));
        assertNull(dbTesting.inTransaction(() -> dao.get(s2.getId())), "Secretary não foi removida");
        assertEquals(1, dbTesting.inTransaction(dao::getAll).size(),
                "Secretary não foi removida da listagem total de Secretaries");
    }

    @Test
    void courseCRUD() {
        CourseDAO dao = new CourseDAO(dbTesting.getSessionFactory());

        final Course c1 = new Course("Ciência da Computação");
        final Course savedC1 = dbTesting.inTransaction(() -> dao.persist(c1));

        assertNotNull(savedC1, "Falhou ao salvar um novo Course");
        assertNotNull(savedC1.getId(), "Course não recebeu um id ao ser criado");
        assertEquals("Ciência da Computação", savedC1.getName(),
                "Name do Course não corresponde com o informado");
        assertEquals(0, savedC1.getDisciplines().size(), "Course foi criado com Discipline(s) associada(s)");
        assertNull(dbTesting.inTransaction(() -> dao.getSecretary(savedC1)), "Course foi associado à uma Secretary");

        c1.setName("Engenharia da Computação");
        c1.addDiscipline(new Discipline());
        final Course updatedC1 = dbTesting.inTransaction(() -> dao.persist(c1));
        assertEquals("Engenharia da Computação", updatedC1.getName(), "Name do Course não foi atualizado corretamente");
        assertEquals(1, updatedC1.getDisciplines().size(), "Discpline não foi associada corretamente");

        dbTesting.inTransaction(() -> dao.delete(updatedC1));
        assertNull(dbTesting.inTransaction(() -> dao.get(c1.getId())), "Course não foi removido");
        assertEquals(0, dbTesting.inTransaction(dao::getAll).size(),
                "Course não foi removido da listagem total de Courses");

        final Course c2 = new Course("Jornalismo");
        final Course c3 = new Course("Direito");
        final Course savedC2 = dbTesting.inTransaction(() -> dao.persist(c2));
        final Course savedC3 = dbTesting.inTransaction(() -> dao.persist(c3));

        assertNotNull(savedC2, "Falhou ao salvar um segundo novo Course");
        assertNotNull(savedC3, "Falhou ao salvar um terceiro novo Course");
        assertEquals(2, dbTesting.inTransaction(dao::getAll).size(),
                "Nem todos os novos Courses estão aparecendo na listagem total de Courses");
        dbTesting.inTransaction(() -> dao.delete(c2));
        assertNull(dbTesting.inTransaction(() -> dao.get(c2.getId())), "Course não foi removido");
        assertEquals(1, dbTesting.inTransaction(dao::getAll).size(),
                "Course não foi removido da listagem total de Courses");
    }
}
