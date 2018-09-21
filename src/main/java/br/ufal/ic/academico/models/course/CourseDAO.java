package br.ufal.ic.academico.models.course;

import br.ufal.ic.academico.models.GeneralDAO;
import org.hibernate.SessionFactory;

import java.util.ArrayList;

public class CourseDAO extends GeneralDAO<Course> {
    public CourseDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public ArrayList<Course> getAll() {
        return (ArrayList<Course>) currentSession().createQuery("from Course").list();
    }
}
