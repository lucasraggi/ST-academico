package br.ufal.ic.academico.person.student;

import br.ufal.ic.academico.GeneralDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import java.util.ArrayList;

@Slf4j
public class StudentDAO extends GeneralDAO<Student> {
    public StudentDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public ArrayList<Student> getAll() {
        return (ArrayList<Student>) currentSession().createQuery("from Student").list();
    }
}
