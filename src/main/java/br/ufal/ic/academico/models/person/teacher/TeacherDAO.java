package br.ufal.ic.academico.models.person.teacher;

import br.ufal.ic.academico.models.GeneralDAO;
import org.hibernate.SessionFactory;

import java.util.ArrayList;

public class TeacherDAO extends GeneralDAO<Teacher> {
    public TeacherDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public ArrayList<Teacher> getAll() {
        return (ArrayList<Teacher>) currentSession().createQuery("from Teacher").list();
    }
}
