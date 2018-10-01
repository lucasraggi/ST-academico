package br.ufal.ic.academico;

import br.ufal.ic.academico.models.course.Course;
import br.ufal.ic.academico.models.course.CourseDAO;
import br.ufal.ic.academico.models.department.Department;
import br.ufal.ic.academico.models.department.DepartmentDAO;
import br.ufal.ic.academico.models.discipline.Discipline;
import br.ufal.ic.academico.models.discipline.DisciplineDAO;
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
import java.util.List;
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

        s1.addCourse(new Course());
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

    @Test
    void disciplineCRUD() {
        DisciplineDAO dao = new DisciplineDAO(dbTesting.getSessionFactory());

        final Discipline d1 = new Discipline("Programação 1", "CC001", 80, 0, new ArrayList<>());
        final Discipline savedD1 = dbTesting.inTransaction(() -> dao.persist(d1));

        assertNull(dbTesting.inTransaction(() -> dao.getCourse(d1)));
        assertNull(dbTesting.inTransaction(() -> dao.getSecretary(d1)));
        assertNotNull(savedD1, "Falhou ao salvar uma nova Discipline");
        assertNotNull(savedD1.getId(), "Discipline não recebeu um id ao ser criada");
        assertEquals("CC001", savedD1.getCode(), "Code da Discipline não corresponde com o informado");
        assertEquals("Programação 1", savedD1.getName(),
                "Name da Discipline não corresponde com o informado");
        assertEquals(80, (int) savedD1.getCredits(), "Credits não corresponde com o informado");
        assertEquals(0, (int) savedD1.getRequiredCredits(), "Required Credits não corresponde com o informado");
        assertEquals(new ArrayList<>(), savedD1.getRequiredDisciplines(), "Pré-requisitos foram associados incorretamente");
        assertNull(savedD1.getTeacher(), "Um professor foi associado à nova Discipline");
        assertEquals(new ArrayList<>(), savedD1.getStudents(), "Aluno(s) foi(ram) associado(s) à nova Discipline");

        d1.setTeacher(new Teacher("Rodrigo", "Paes"));
        d1.setCredits(60);
        d1.setRequiredCredits(100);
        List<String> preRequisites = new ArrayList<>();
        preRequisites.add("CC002");
        preRequisites.add("CC003");
        d1.setRequiredDisciplines(preRequisites);
        final Discipline updatedD1 = dbTesting.inTransaction(() -> dao.persist(d1));
        assertNotNull(updatedD1.getTeacher(), "Professor não foi associado à Discipline");
        assertEquals(new ArrayList<>(), updatedD1.getStudents(), "Lista de alunos foi alterada quando não deveria");
        assertEquals(60, (int) updatedD1.getCredits(), "O valor de credits da Discipline não foi atualizado corretamente");
        assertEquals(100, (int) updatedD1.getRequiredCredits(), "Required credits não foi atualizado corretamente");
        assertEquals(2, updatedD1.getRequiredDisciplines().size(),
                "Pré-requisitos não foram atualizados corretamente");

        dbTesting.inTransaction(() -> dao.delete(updatedD1));
        assertNull(dbTesting.inTransaction(() -> dao.get(d1.getId())), "Discipline não foi removida");
        assertEquals(0, dbTesting.inTransaction(dao::getAll).size(),
                "Discipline não foi removido da listagem total de Disciplines");

        final Discipline d2 = new Discipline("Programação 2", "CC002", 0, 0, new ArrayList<>());
        final Discipline d3 = new Discipline("Teste de Software", "CC003", 0, 0, new ArrayList<>());
        final Discipline savedD2 = dbTesting.inTransaction(() -> dao.persist(d2));
        final Discipline savedD3 = dbTesting.inTransaction(() -> dao.persist(d3));

        assertNotNull(savedD2, "Falhou ao salvar uma segunda nova Discipline");
        assertNotNull(savedD3, "Falhou ao salvar uma terceira nova Discipline");
        assertEquals(2, dbTesting.inTransaction(dao::getAll).size(),
                "Nem todas as novas Disciplines estão aparecendo na listagem total de Disciplines");
        dbTesting.inTransaction(() -> dao.delete(d2));
        assertNull(dbTesting.inTransaction(() -> dao.get(d2.getId())), "Discipline não foi removida");
        assertEquals(1, dbTesting.inTransaction(dao::getAll).size(),
                "Discipline não foi removido da listagem total de Disciplines");
    }

    @Test
    void enrollmentCRUD() {
        StudentDAO studentDAO = new StudentDAO(dbTesting.getSessionFactory());
        TeacherDAO teacherDAO = new TeacherDAO(dbTesting.getSessionFactory());
        DepartmentDAO departmentDAO = new DepartmentDAO(dbTesting.getSessionFactory());
        SecretaryDAO secretaryDAO = new SecretaryDAO(dbTesting.getSessionFactory());
        CourseDAO courseDAO = new CourseDAO(dbTesting.getSessionFactory());
        DisciplineDAO disciplineDAO = new DisciplineDAO(dbTesting.getSessionFactory());

        // Students
        Student stdntNewGrad = new Student("Daniel", "Vassalo");
        Student savedStdntNewGrad = dbTesting.inTransaction(() -> studentDAO.persist(stdntNewGrad));

        Student stdntOldGrad = new Student("Gabriel", "Barbosa");
        stdntOldGrad.setCredits(240);
        Student savedStdntOldGrad = dbTesting.inTransaction(() -> studentDAO.persist(stdntOldGrad));

        Student stdntNewPostGrad = new Student("Romero", "Malaquias");
        Student savedStdntNewPostGrad = dbTesting.inTransaction(() -> studentDAO.persist(stdntNewPostGrad));

        Student stdntOldPostGrad = new Student("Marcos", "Paulo");
        stdntOldPostGrad.setCredits(300);
        Student savedStdntOldPostGrad = dbTesting.inTransaction(() -> studentDAO.persist(stdntOldPostGrad));

        // Teachers
        Teacher teacher1 = new Teacher("Willy", "Tiengo");
        dbTesting.inTransaction(() -> teacherDAO.persist(teacher1));

        Teacher teacher2 = new Teacher("Rodrigo", "Paes");
        dbTesting.inTransaction(() -> teacherDAO.persist(teacher2));

        // Disciplines
        List<String> prerequisites = new ArrayList<>();
        Discipline discipGrad1 = new Discipline("Programação 1", "EC001", 80, 0, new ArrayList<>());
        discipGrad1.setTeacher(teacher1);
        Discipline savedDiscipGrad1 = dbTesting.inTransaction(() -> disciplineDAO.persist(discipGrad1));

        prerequisites.add("EC001");
        Discipline discipGrad2 = new Discipline("Programação 2", "EC002", 80, 80, prerequisites);
        discipGrad2.setTeacher(teacher2);
        Discipline savedDiscipGrad2 = dbTesting.inTransaction(() -> disciplineDAO.persist(discipGrad2));

        Discipline discipPostGrad1 = new Discipline("Projeto e Análise de Algoritmos", "CC101", 80, 0, new ArrayList<>());
        discipPostGrad1.setTeacher(teacher2);
        Discipline savedDiscipPostGrad1 = dbTesting.inTransaction(() -> disciplineDAO.persist(discipPostGrad1));

        Discipline discipGrad3 = new Discipline("Direito Constitucional", "DD001", 80, 0, new ArrayList<>());
        discipGrad3.setTeacher(teacher1);
        Discipline savedDiscipGrad3 = dbTesting.inTransaction(() -> disciplineDAO.persist(discipGrad3));

        Discipline discipPostGrad2 = new Discipline("Direito Penal", "DD101", 80, 0, new ArrayList<>());
        discipPostGrad2.setTeacher(teacher2);
        Discipline savedDiscipPostGrad2 = dbTesting.inTransaction(() -> disciplineDAO.persist(discipPostGrad2));

        // Courses
        Course compEngineeringGrad = new Course("Engenharia da Computação");
        compEngineeringGrad.addDiscipline(discipGrad1);
        compEngineeringGrad.addDiscipline(discipGrad2);
        Course savedCompEngineeringGrad = dbTesting.inTransaction(() -> courseDAO.persist(compEngineeringGrad));

        Course compSciencePostGrad = new Course("Ciência da Computação");
        compSciencePostGrad.addDiscipline(discipPostGrad1);
        Course savedCompSciencePostGrad = dbTesting.inTransaction(() -> courseDAO.persist(compSciencePostGrad));

        Course lawGrad = new Course("Direito");
        lawGrad.addDiscipline(discipGrad3);
        Course savedLawGrad = dbTesting.inTransaction(() -> courseDAO.persist(lawGrad));

        Course lawPostGrad = new Course("Direito");
        lawPostGrad.addDiscipline(discipPostGrad2);
        Course savedLawPostGrad = dbTesting.inTransaction(() -> courseDAO.persist(lawPostGrad));

        // Secretaries
        Secretary secICgrad = new Secretary("GRADUATION");
        secICgrad.addCourse(compEngineeringGrad);
        Secretary savedSecICGrad = dbTesting.inTransaction(() -> secretaryDAO.persist(secICgrad));

        Secretary secICPostGrad = new Secretary("POST-GRADUATION");
        secICPostGrad.addCourse(compSciencePostGrad);
        Secretary savedSecICPostGrad = dbTesting.inTransaction(() -> secretaryDAO.persist(secICPostGrad));

        Secretary secFDAGrad = new Secretary("GRADUATION");
        secFDAGrad.addCourse(lawGrad);
        Secretary savedSecFDAGrad = dbTesting.inTransaction(() -> secretaryDAO.persist(secFDAGrad));

        Secretary secFDAPostGrad = new Secretary("POST-GRADUATION");
        secFDAPostGrad.addCourse(lawPostGrad);
        Secretary savedSecFDAPostGrad = dbTesting.inTransaction(() -> secretaryDAO.persist(secFDAPostGrad));

        // Departments
        Department IC = new Department("IC");
        IC.setGraduation(secICgrad);
        IC.setPostGraduation(secICPostGrad);
        Department savedIC = dbTesting.inTransaction(() -> departmentDAO.persist(IC));

        Department FDA = new Department("FDA");
        FDA.setGraduation(secFDAGrad);
        FDA.setPostGraduation(secFDAPostGrad);
        Department savedFDA = dbTesting.inTransaction(() -> departmentDAO.persist(FDA));

        // Tests
        assertNotNull(discipGrad1.enroll(stdntNewGrad, null, IC, null, secICgrad));

        assertNull(savedStdntNewGrad.getCourse(), "New Student foi associado à um curso na criação");
        stdntNewGrad.setCourse(compEngineeringGrad);
        savedStdntNewGrad = dbTesting.inTransaction(() -> studentDAO.persist(stdntNewGrad));
        assertEquals(compEngineeringGrad.getId(), savedStdntNewGrad.getCourse().getId(),
                "New Student não foi associado corretamente ao Course");
        assertEquals(secICgrad.getId(), dbTesting.inTransaction(() -> studentDAO.getSecretary(stdntNewGrad)).getId(),
                "New Student não foi associado à Secretary correta");
        assertEquals(IC.getId(), dbTesting.inTransaction(() -> studentDAO.getDepartment(stdntNewGrad)).getId(),
                "New Student não foi associado ao Department correto");

        assertNull(savedStdntOldGrad.getCourse(), "Old Student foi associado à um curso na criação");
        stdntOldGrad.setCourse(compEngineeringGrad);
        savedStdntOldGrad = dbTesting.inTransaction(() -> studentDAO.persist(stdntOldGrad));
        assertEquals(compEngineeringGrad.getId(), savedStdntOldGrad.getCourse().getId(),
                "Old Student não foi associado corretamente ao Course");
        assertEquals(secICgrad.getId(), dbTesting.inTransaction(() -> studentDAO.getSecretary(stdntOldGrad)).getId(),
                "Old Student não foi associado à Secretary correta");
        assertEquals(IC.getId(), dbTesting.inTransaction(() -> studentDAO.getDepartment(stdntOldGrad)).getId(),
                "Old Student não foi associado ao Department correto");

        assertNull(savedStdntNewPostGrad.getCourse(), "New Post Gradute Student foi associado à um curso na criação");
        stdntNewPostGrad.setCourse(compSciencePostGrad);
        savedStdntNewPostGrad = dbTesting.inTransaction(() -> studentDAO.persist(stdntNewPostGrad));
        assertEquals(compSciencePostGrad.getId(), savedStdntNewPostGrad.getCourse().getId(),
                "New Post Graduate Student não foi associado corretamente ao Course");
        assertEquals(secICPostGrad.getId(), dbTesting.inTransaction(() -> studentDAO.getSecretary(stdntNewPostGrad)).getId(),
                "New Post Graduate Student não foi associado à Secretary correta");
        assertEquals(IC.getId(), dbTesting.inTransaction(() -> studentDAO.getDepartment(stdntNewPostGrad)).getId(),
                "New Post Graduate Student não foi associado ao Department correto");

        assertNull(savedStdntOldPostGrad.getCourse(), "Old Post Graduate Student foi associado à um curso na criação");
        stdntOldPostGrad.setCourse(compSciencePostGrad);
        savedStdntOldPostGrad = dbTesting.inTransaction(() -> studentDAO.persist(stdntOldPostGrad));
        assertEquals(compSciencePostGrad.getId(), savedStdntOldPostGrad.getCourse().getId(),
                "Old Post Graduate Student não foi associado corretamente ao Course");
        assertEquals(secICPostGrad.getId(), dbTesting.inTransaction(() -> studentDAO.getSecretary(stdntOldPostGrad)).getId(),
                "Old Post Graduate Student não foi associado à Secretary correta");
        assertEquals(IC.getId(), dbTesting.inTransaction(() -> studentDAO.getDepartment(stdntOldPostGrad)).getId(),
                "Old Post Graduate Student não foi associado ao Department correto");

        assertEquals(compEngineeringGrad.getId(), dbTesting.inTransaction(() -> disciplineDAO.getCourse(discipGrad1)).getId(),
                "Graduation Discipline não foi associada ao Course correto");
        assertEquals(compEngineeringGrad.getId(), dbTesting.inTransaction(() -> disciplineDAO.getCourse(discipGrad2)).getId(),
                "Graduation Discipline não foi associada ao Course correto");
        assertEquals(compSciencePostGrad.getId(), dbTesting.inTransaction(() -> disciplineDAO.getCourse(discipPostGrad1)).getId(),
                "Post graduation Discipline não foi associada ao Course correto");
        assertEquals(lawGrad.getId(), dbTesting.inTransaction(() -> disciplineDAO.getCourse(discipGrad3)).getId(),
                "Graduation Discipline não foi associada ao Course correto");
        assertEquals(lawPostGrad.getId(), dbTesting.inTransaction(() -> disciplineDAO.getCourse(discipPostGrad2)).getId(),
                "Post graduation Discipline não foi associada ao Course correto");

        assertEquals(secICgrad.getId(), dbTesting.inTransaction(() -> disciplineDAO.getSecretary(discipGrad1)).getId(),
                "Graduation Discipline não foi associada à Secretary correta");
        assertEquals(secICgrad.getId(), dbTesting.inTransaction(() -> disciplineDAO.getSecretary(discipGrad2)).getId(),
                "Graduation Discipline não foi associada à Secretary correta");
        assertEquals(secICPostGrad.getId(), dbTesting.inTransaction(() -> disciplineDAO.getSecretary(discipPostGrad1)).getId(),
                "Post graduation Discipline não foi associada à Secretary correta");
        assertEquals(secFDAGrad.getId(), dbTesting.inTransaction(() -> disciplineDAO.getSecretary(discipGrad3)).getId(),
                "Graduation Discipline não foi associada à Secretary correta");
        assertEquals(secFDAPostGrad.getId(), dbTesting.inTransaction(() -> disciplineDAO.getSecretary(discipPostGrad2)).getId(),
                "Post graduation Discipline não foi associada à Secretary correta");

        assertEquals(IC.getId(), dbTesting.inTransaction(() -> disciplineDAO.getDepartment(discipGrad1)).getId(),
                "Graduation Discipline não foi associada ao Department correto");
        assertEquals(IC.getId(), dbTesting.inTransaction(() -> disciplineDAO.getDepartment(discipGrad2)).getId(),
                "Graduation Discipline não foi associada ao Department correto");
        assertEquals(IC.getId(), dbTesting.inTransaction(() -> disciplineDAO.getDepartment(discipPostGrad1)).getId(),
                "Post graduation Discipline não foi associada ao Department correto");
        assertEquals(FDA.getId(), dbTesting.inTransaction(() -> disciplineDAO.getDepartment(discipGrad3)).getId(),
                "Graduation Discipline não foi associada ao Department correto");
        assertEquals(FDA.getId(), dbTesting.inTransaction(() -> disciplineDAO.getDepartment(discipPostGrad2)).getId(),
                "Post graduation Discipline não foi associada ao Department correto");

        assertEquals(teacher1.getId(), savedDiscipGrad1.getTeacher().getId(),
                "Teacher não foi associado à Graduation Discipline correta");
        assertEquals(teacher2.getId(), savedDiscipGrad2.getTeacher().getId(),
                "Teacher não foi associado à Graduation Discipline correta");
        assertEquals(teacher2.getId(), savedDiscipPostGrad1.getTeacher().getId(),
                "Teacher não foi associado à Post Graduation Discipline correta");
        assertEquals(teacher1.getId(), savedDiscipGrad3.getTeacher().getId(),
                "Teacher não foi associado à Graduation Discipline correta");
        assertEquals(teacher2.getId(), savedDiscipPostGrad2.getTeacher().getId(),
                "Teacher não foi associado à Post Graduation Discipline correta");

        assertEquals(2, savedCompEngineeringGrad.getDisciplines().size(),
                "Graduation Disciplines não foram associadas ao Course corretamente");
        assertEquals(1, savedCompSciencePostGrad.getDisciplines().size(),
                "Post Graduation Disciplines não foram associadas ao Course corretamente");
        assertEquals(1, savedLawGrad.getDisciplines().size(),
                "Graduation Disciplines não foram associadas ao Course corretamente");
        assertEquals(1, savedLawPostGrad.getDisciplines().size(),
                "Post Graduation Disciplines não foram associadas ao Course corretamente");

        assertEquals(secICgrad.getId(), dbTesting.inTransaction(() -> courseDAO.getSecretary(compEngineeringGrad)).getId(),
                "Graduation Course não foi associado à Secretary correta");
        assertEquals(secICPostGrad.getId(), dbTesting.inTransaction(() -> courseDAO.getSecretary(compSciencePostGrad)).getId(),
                "Post Graduation Course não foi associado à Secretary correta");
        assertEquals(secFDAGrad.getId(), dbTesting.inTransaction(() -> courseDAO.getSecretary(lawGrad)).getId(),
                "Graduation Course não foi associado à Secretary correta");
        assertEquals(secFDAPostGrad.getId(), dbTesting.inTransaction(() -> courseDAO.getSecretary(lawPostGrad)).getId(),
                "Post Graduation Course não foi associado à Secretary correta");

        assertEquals(1, savedSecICGrad.getCourses().size(),
                "Course não foi associado à Graduation Secretary corretamente");
        assertEquals(1, savedSecICPostGrad.getCourses().size(),
                "Course não foi associado à Post Graduation Secretary corretamente");
        assertEquals(1, savedSecFDAGrad.getCourses().size(),
                "Course não foi associado à Graduation Secretary corretamente");
        assertEquals(1, savedSecFDAPostGrad.getCourses().size(),
                "Course não foi associado à Post Graduation Secretary corretamente");

        assertEquals(secICgrad.getId(), savedIC.getGraduation().getId(),
                "Graduation Secretary não foi associada ao Department correto");
        assertEquals(secICPostGrad.getId(), savedIC.getPostGraduation().getId(),
                "Post Graduation Secretary não foi associada ao Department correto");
        assertEquals(secFDAGrad.getId(), savedFDA.getGraduation().getId(),
                "Graduation Secretary não foi associada ao Department correto");
        assertEquals(secFDAPostGrad.getId(), savedFDA.getPostGraduation().getId(),
                "Post Graduation Secretary não foi associada ao Department correto");

        assertEquals(0, discipGrad1.getStudents().size(),
                "Graduation Discipline possui Students matriculados");
        assertNull(discipGrad1.enroll(stdntNewGrad, IC, IC, secICgrad, secICgrad));
        assertNull(discipGrad1.enroll(stdntOldGrad, IC, IC, secICgrad, secICgrad));
        assertNotNull(discipGrad1.enroll(stdntNewPostGrad, IC, IC, secICPostGrad, secICgrad));
        assertNotNull(discipGrad1.enroll(stdntOldPostGrad, IC, IC, secICPostGrad, secICgrad));
        assertEquals(2, discipGrad1.getStudents().size(),
                "Graduation Discipline possui número de Students matriculados diferente do esperado");
        assertTrue(discipGrad1.getStudents().contains(stdntNewGrad),
                "Newbie Graduation Student não foi matriculado em " + discipGrad1.getName());
        assertTrue(discipGrad1.getStudents().contains(stdntOldGrad),
                "Veteran Graduation Student não foi matriculado em " + discipGrad1.getName());
        assertFalse(discipGrad1.getStudents().contains(stdntNewPostGrad),
                "Newbie Post Graduation Student foi matriculado em " + discipGrad1.getName());
        assertFalse(discipGrad1.getStudents().contains(stdntOldPostGrad),
                "Veteran Post Graduation Student foi matriculado em " + discipGrad1.getName());

        assertNotNull(discipGrad3.enroll(stdntNewGrad, IC, FDA, secICgrad, secFDAGrad),
                "Newbie Graduation Student foi matriculado numa Discipline de um Department diferente do Student");
        assertNotNull(discipGrad2.enroll(stdntNewPostGrad, IC, IC, secICPostGrad, secICgrad),
                "Newbie Post Graduation Student foi matriculado numa Graduation Discipline");
        assertNull(discipPostGrad1.enroll(stdntNewPostGrad, IC, IC, secICPostGrad, secICPostGrad),
                "Newbie Post Graduation Student não foi matriculado numa Post Graduation Discipline");
        assertNotNull(discipPostGrad1.enroll(stdntNewGrad, IC, IC, secICgrad, secICPostGrad),
                "Newbie Graduation Student foi matriculado numa Post Graduation Discipline ");
        assertNull(discipPostGrad1.enroll(stdntOldGrad, IC, IC, secICgrad, secICPostGrad),
                "Veteran Graduation Student não foi matriculado numa Discipline de Post Graduation");
        assertNotNull(discipGrad2.enroll(stdntNewGrad, IC, IC, secICgrad, secICgrad),
                "Newbie Graduation Student foi matriculado numa Graduation Discipline que ele não possui créditos suficientes");
        assertNotNull(discipGrad2.enroll(stdntOldGrad, IC, IC, secICgrad, secICgrad),
                "Veteran Graduation Student foi matriculado numa Graduation Discipline que ele não atende aos pré-requisitos (Required Discipline)");
        assertNotNull(discipGrad1.enroll(stdntOldGrad, IC, IC, secICgrad, secICgrad),
                "Veteran Graduation Student foi matriculado numa Graduation Discipline em que ele já está matriculado");
        assertTrue(stdntOldGrad.completeDiscipline(discipGrad1),
                "Veteran Graduation Student falhou ao concluir uma Graduation Discipline sem requisitos");
        assertNotNull(discipGrad1.enroll(stdntOldGrad, IC, IC, secICgrad, secICgrad),
                "Veteran Graduation Student foi matriculado numa Graduation Discipline que ele já concluiu");
        assertNull(discipGrad2.enroll(stdntOldGrad, IC, IC, secICgrad, secICgrad),
                "Veteran Graduation Student não foi matriculado numa Graduation Discipline que ele atende todos os requisitos");

        assertEquals(1, dbTesting.inTransaction(() -> disciplineDAO.getAllByStudent(stdntNewGrad)).size(),
                "Quantidade de Disciplines em que o Newbie Graduation Student se encontra matriculado está diferente do esperado");
        assertEquals(2, dbTesting.inTransaction(() -> disciplineDAO.getAllByStudent(stdntOldGrad)).size(),
                "Quantidade de Disciplines em que o Veteran Graduation Student se encontra matriculado está diferente do esperado");
        assertEquals(1, dbTesting.inTransaction(() -> disciplineDAO.getAllByStudent(stdntNewPostGrad)).size(),
                "Quantidade de Disciplines em que o Newbie Post Graduation Student se encontra matriculado está diferente do esperado");
        assertEquals(0, dbTesting.inTransaction(() -> disciplineDAO.getAllByStudent(stdntOldPostGrad)).size(),
                "Quantidade de Disciplines em que o Veteran Post Graduation Student se encontra matriculado está diferente do esperado");

        savedStdntNewGrad = dbTesting.inTransaction(() -> studentDAO.persist(stdntNewGrad));
        assertEquals(0, (int) savedStdntNewGrad.getCredits(),
                "Newbie Graduation Student Credits diferente do esperado");
        assertEquals(0, savedStdntNewGrad.getCompletedDisciplines().size(),
                "Quantidade de Disciplines concluídas pelo Newbie Graduation Student está diferente do esperado");
        assertTrue(stdntNewGrad.completeDiscipline(discipGrad1),
                "Newbie Graduation Student falhou ao concluir uma Discipline sem requisitos");
        assertFalse(stdntNewGrad.completeDiscipline(discipGrad2),
                "Newbie Graduation Student concluiu uma Graduation Discipline que não estava matriculado");
        assertFalse(stdntNewGrad.completeDiscipline(discipGrad3),
                "Newbie Graduation Student concluiu uma Graduation Discipline de outro Department");
        assertFalse(stdntNewGrad.completeDiscipline(discipPostGrad1),
                "Newbie Graduation Student concluiu uma Post Graduation Discipline");
        assertEquals(discipGrad1.getCredits(), stdntNewGrad.getCredits(),
                "Credits obtido pelo Newbiew Graduation Student está diferente do esperado");
        assertEquals(1, stdntNewGrad.getCompletedDisciplines().size(),
                "Quantidade de Disciplines concluídas pelo Newbiew Graduation Student está em diferente do esperado");

        savedStdntOldGrad = dbTesting.inTransaction(() -> studentDAO.persist(stdntOldGrad));
        assertEquals(discipGrad1.getCredits() + 240, (int) savedStdntOldGrad.getCredits(),
                "Veteran Graduation Student Credits diferente do esperado");
        assertEquals(1, savedStdntNewGrad.getCompletedDisciplines().size(),
                "Quantidade de Disciplines concluídas pelo Veteran Graduation Student está diferente do esperado");
        assertFalse(stdntOldGrad.completeDiscipline(discipGrad1),
                "Veteran Graduation Student concluiu uma Discipline que ele já havia completado antes");
        assertTrue(stdntOldGrad.completeDiscipline(discipGrad2),
                "Veteran Graduation Student falhou ao concluir uma Graduation Discipline, com requisitos, que ele está matriculado");
        assertFalse(stdntOldGrad.completeDiscipline(discipGrad3),
                "Veteran Graduation Student concluiu uma Graduation Discipline de outro Department");
        assertTrue(stdntOldGrad.completeDiscipline(discipPostGrad1),
                "Veteran Graduation Student falhou ao concluir uma Post Graduation Discipline que ele está matriculado");
        assertEquals(discipGrad1.getCredits() + discipGrad2.getCredits() + discipPostGrad1.getCredits() + 240, (int) stdntOldGrad.getCredits(),
                "Credits obtido pelo Veteran Graduation Student está diferente do esperado");
        assertEquals(3, stdntOldGrad.getCompletedDisciplines().size(),
                "Quantidade de Disciplines concluídas pelo Veteran Graduation Student está em diferente do esperado");

        savedStdntNewPostGrad = dbTesting.inTransaction(() -> studentDAO.persist(stdntNewPostGrad));
        assertEquals(0, (int) savedStdntNewPostGrad.getCredits(),
                "Newbie Post Graduation Student Credits diferente do esperado");
        assertEquals(0, savedStdntNewPostGrad.getCompletedDisciplines().size(),
                "Quantidade de Disciplines concluídas pelo Newbie Post Graduation Student está diferente do esperado");
        assertFalse(stdntNewPostGrad.completeDiscipline(discipGrad1),
                "Newbie Post Graduation Student concluiu uma Graduation Discipline sem requisitos");
        assertFalse(stdntNewPostGrad.completeDiscipline(discipGrad2),
                "Newbie Post Graduation Student concluiu uma Graduation Discipline com requisitos");
        assertFalse(stdntNewPostGrad.completeDiscipline(discipGrad3),
                "Newbie Post Graduation Student concluiu uma Graduation Discipline de outro Department");
        assertTrue(stdntNewPostGrad.completeDiscipline(discipPostGrad1),
                "Newbie Post Graduation Student falhou ao concluir uma Post Graduation Discipline em que está matriculado");
        assertEquals(discipPostGrad1.getCredits(), stdntNewPostGrad.getCredits(),
                "Credits obtido pelo Newbiew Post Graduation Student está diferente do esperado");
        assertEquals(1, stdntNewPostGrad.getCompletedDisciplines().size(),
                "Quantidade de Disciplines concluídas pelo Newbiew Post Graduation Student está em diferente do esperado");

        savedStdntOldPostGrad = dbTesting.inTransaction(() -> studentDAO.persist(stdntOldPostGrad));
        assertEquals(300, (int) savedStdntOldPostGrad.getCredits(),
                "Veteran Post Graduation Student Credits diferente do esperado");
        assertEquals(0, savedStdntOldPostGrad.getCompletedDisciplines().size(),
                "Quantidade de Disciplines concluídas pelo Veteran Post Graduation Student está diferente do esperado");
        assertFalse(stdntOldPostGrad.completeDiscipline(discipGrad1),
                "Veteran Post Graduation Student concluiu uma Graduation Discipline sem requisitos");
        assertFalse(stdntOldPostGrad.completeDiscipline(discipGrad2),
                "VEteran Post Graduation Student concluiu uma Graduation Discipline com requisitos");
        assertFalse(stdntOldPostGrad.completeDiscipline(discipGrad3),
                "VEteran Post Graduation Student concluiu uma Graduation Discipline de outro Department");
        assertFalse(stdntOldPostGrad.completeDiscipline(discipPostGrad1),
                "Veteran Post Graduation Student concluiu uma Post Graduation Discipline em que não está matriculado");
        assertEquals(300, (int) stdntOldPostGrad.getCredits(),
                "Credits obtido pelo Veteran Post Graduation Student está diferente do esperado");
        assertEquals(0, stdntOldPostGrad.getCompletedDisciplines().size(),
                "Quantidade de Disciplines concluídas pelo Veteran Post Graduation Student está em diferente do esperado");

        assertTrue(compEngineeringGrad.deleteDiscipline(discipGrad1),
                "Falhou ao remover uma Graduation Discipline que pertence ao Course");
        assertFalse(compEngineeringGrad.deleteDiscipline(discipPostGrad1),
                "Removeu uma Post Graduation Discipline de um Graduation Course");
        assertFalse(lawGrad.deleteDiscipline(discipGrad2),
                "Removeu uma Discipline que não pertence ao Department do Course");
    }
}
