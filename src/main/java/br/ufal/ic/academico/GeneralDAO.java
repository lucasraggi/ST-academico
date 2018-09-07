package br.ufal.ic.academico;

import br.ufal.ic.academico.person.student.Student;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.io.Serializable;

@Slf4j
public abstract class GeneralDAO<T> extends AbstractDAO<T> {
    public GeneralDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public T get(Serializable id) throws HibernateException {
        return super.get(id);
    }

    @Override
    public T persist(T entity) throws HibernateException {
        return super.persist(entity);
    }

    public void delete(T entity) throws HibernateException {
        super.currentSession().delete(entity);
    }
}
