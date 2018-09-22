package br.ufal.ic.academico.models.discipline;

import br.ufal.ic.academico.models.GeneralDAO;
import br.ufal.ic.academico.models.course.Course;
import org.hibernate.SessionFactory;

import java.util.ArrayList;

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
}
