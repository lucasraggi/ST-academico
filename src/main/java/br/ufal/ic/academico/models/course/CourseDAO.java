package br.ufal.ic.academico.models.course;

import br.ufal.ic.academico.models.GeneralDAO;
import br.ufal.ic.academico.models.secretary.Secretary;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class CourseDAO extends GeneralDAO<Course> {
    public CourseDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public ArrayList<Course> getAll() {
        return (ArrayList<Course>) currentSession().createQuery("from Course").list();
    }

    public Secretary getSecretary(Course course) {
        List<Secretary> secretaries = (List<Secretary>)  currentSession().createQuery("from Secretary").list();
        for (Secretary s : secretaries) {
            for (Course c : s.getCourses()) {
                if (c.getId().equals(course.getId())) {
                    return s;
                }
            }
        }
        return null;
    }
}
