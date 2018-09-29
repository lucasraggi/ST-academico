package br.ufal.ic.academico;

import br.ufal.ic.academico.models.course.Course;
import br.ufal.ic.academico.models.department.Department;
import br.ufal.ic.academico.models.discipline.Discipline;
import br.ufal.ic.academico.models.person.student.Student;
import br.ufal.ic.academico.models.person.student.StudentDAO;
import br.ufal.ic.academico.models.person.teacher.Teacher;
import br.ufal.ic.academico.models.person.teacher.TeacherDAO;
import br.ufal.ic.academico.models.secretary.Secretary;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import lombok.SneakyThrows;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
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
    
    private StudentDAO studentDAO;
    private TeacherDAO teacherDAO;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        studentDAO = new StudentDAO(dbTesting.getSessionFactory());
        teacherDAO = new TeacherDAO(dbTesting.getSessionFactory());
    }
    
    @Test
    void studentCRUD() {
        final Student s1 = new Student("Daniel", "Humberto Cavalcante Vassalo");
        final Student savedS1 = dbTesting.inTransaction(() -> studentDAO.persist(s1));

        assertNotNull(savedS1, "Falhou ao salvar um novo Student");
        assertNotNull(savedS1.getId(), "Student não recebeu um id ao ser criado");
        assertEquals(s1.getFirstname(), savedS1.getFirstname(), "First name do Student não corresponde com o informado");
        assertEquals(s1.getLastName(), savedS1.getLastName(), "Last name do Student não corresponde com o informado");
        assertEquals(new Integer(0), savedS1.getCredits(), "Student foi cadastro com 'credits' diferente de 0");
        assertNull(savedS1.getCourse(), "Student recebeu um curso ao ser criado");
        assertEquals(new ArrayList<>(), savedS1.getCompletedDisciplines(),
                "Student recebeu uma lista de matérias concluídas ao ser criado");
        assertNull(dbTesting.inTransaction(() -> studentDAO.getDepartment(savedS1)), "Student foi vinculado à um Department");
        assertNull(dbTesting.inTransaction(() -> studentDAO.getSecretary(savedS1)), "Student foi vinculado à uma Secretary");

        for (int i = 0; i < 50; i++) {
            Integer credits = new Random().nextInt();
            s1.setCredits(credits);
            final Student updatedS1 = dbTesting.inTransaction(() -> studentDAO.persist(s1));
            assertEquals(credits, updatedS1.getCredits(), "Créditos não foram atualizados corretamente");
        }
        s1.setLastName("Dan");
        s1.setLastName("Vassalo");
        final Student updatedS1 = dbTesting.inTransaction(() -> studentDAO.persist(s1));
        assertEquals(s1.getFirstname(), updatedS1.getFirstname(), "First name não foi atualizado corretamente");
        assertEquals(s1.getLastName(), updatedS1.getLastName(), "Last name não foi atualizado corretamente");

        dbTesting.inTransaction(() -> studentDAO.delete(updatedS1));
        assertNull(dbTesting.inTransaction(() -> studentDAO.get(s1.getId())), "Student não foi removido");
        assertEquals(0, dbTesting.inTransaction(() -> studentDAO.getAll()).size(),
                "Student não foi removido da listagem de todos os Student");

        final Student s2 = new Student("Lucas", "Raggi");
        final Student s3 = new Student("Gabriel", "Barbosa");
        final Student savedS2 = dbTesting.inTransaction(() -> studentDAO.persist(s2));
        final Student savedS3 = dbTesting.inTransaction(() -> studentDAO.persist(s3));

        assertNotNull(savedS2, "Falhou ao salvar um segundo novo Student");
        assertNotNull(savedS3, "Falhou ao salvar um terceiro novo Student");
        assertEquals(2, dbTesting.inTransaction(() -> studentDAO.getAll()).size(),
                "Nem todos os novos Students estão aparecendo na listagem total de Students");
        dbTesting.inTransaction(() -> studentDAO.delete(s2));
        assertNull(dbTesting.inTransaction(() -> studentDAO.get(s2.getId())), "Student não foi removido");
        assertEquals(1, dbTesting.inTransaction(() -> studentDAO.getAll()).size(),
                "Student não foi removido da listagem de todos os Students");
    }

    @Test
    void teacherCRUD() {
        final Teacher t1 = new Teacher("Willy", "Carvalho Tiengo");
        final Teacher savedT1 = dbTesting.inTransaction(() -> teacherDAO.persist(t1));

        assertNotNull(savedT1, "Falhou ao salvar um novo Teacher");
        assertNotNull(savedT1.getId(), "Teacher não recebeu um id ao ser criado");
        assertEquals(t1.getFirstname(), savedT1.getFirstname(), "First name do Teacher não corresponde com o informado");
        assertEquals(t1.getLastName(), savedT1.getLastName(), "Last name do Teacher não corresponde com o informado");

        t1.setLastName("Will");
        t1.setLastName("Tiengo");
        final Teacher updatedT1 = dbTesting.inTransaction(() -> teacherDAO.persist(t1));
        assertEquals(t1.getFirstname(), updatedT1.getFirstname(), "First name não foi atualizado corretamente");
        assertEquals(t1.getLastName(), updatedT1.getLastName(), "Last name não foi atualizado corretamente");

        dbTesting.inTransaction(() -> teacherDAO.delete(updatedT1));
        assertNull(dbTesting.inTransaction(() -> teacherDAO.get(t1.getId())), "Teacher não foi removido");
        assertEquals(0, dbTesting.inTransaction(() -> teacherDAO.getAll()).size(),
                "Teacher não foi removido da listagem de todos os Teachers");

        final Teacher t2 = new Teacher("Rodrigo", "Paes");
        final Teacher t3 = new Teacher("Márcio", "Ribeiro");
        final Teacher savedT2 = dbTesting.inTransaction(() -> teacherDAO.persist(t2));
        final Teacher savedT3 = dbTesting.inTransaction(() -> teacherDAO.persist(t3));

        assertNotNull(savedT2, "Falhou ao salvar um segundo novo Teacher");
        assertNotNull(savedT3, "Falhou ao salvar um terceiro novo Teacher");
        assertEquals(2, dbTesting.inTransaction(() -> teacherDAO.getAll()).size(),
                "Nem todos os novos Teachers estão aparecendo na listagem total de Teachers");
        dbTesting.inTransaction(() -> teacherDAO.delete(t2));
        assertNull(dbTesting.inTransaction(() -> teacherDAO.get(t2.getId())), "Teacher não foi removido");
        assertEquals(1, dbTesting.inTransaction(() -> teacherDAO.getAll()).size(),
                "Teacher não foi removido da listagem de todos os Teachers");
    }
}
