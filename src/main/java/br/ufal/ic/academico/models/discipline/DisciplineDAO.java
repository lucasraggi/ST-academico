package br.ufal.ic.academico.models.discipline;

import br.ufal.ic.academico.models.GeneralDAO;
import br.ufal.ic.academico.models.course.Course;
import br.ufal.ic.academico.models.person.student.Student;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class DisciplineDAO extends GeneralDAO<Discipline> {
    public DisciplineDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public ArrayList<Discipline> getAll() {
        return (ArrayList<Discipline>) currentSession().createQuery("from Discipline").list();
    }

    public Course getCourse(Discipline discipline) {
        ArrayList<Course> courses = (ArrayList<Course>) currentSession().createQuery("from Course").list();
        for (Course c : courses) {
            for (Discipline d : c.getDisciplines()) {
                if (d.getId().equals(discipline.getId())) {
                    return c;
                }
            }
        }
        return null;
    }

    public List<Discipline> getAllByStudent(Student s) {
        List<Discipline> disciplines = new ArrayList<>();
        List<Discipline> allDisciplines = this.getAll();
        for (Discipline d : allDisciplines) {
            if (d.students.contains(s)) {
                disciplines.add(d);
            }
        }
        return disciplines;
    }
}
