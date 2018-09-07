package br.ufal.ic.academico.exemplos;

import io.dropwizard.hibernate.AbstractDAO;
import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

/**
 *
 * @author Willy
 */
@Slf4j
public class PersonDAO extends AbstractDAO<Person> {
    
    public PersonDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Person get(Serializable id) throws HibernateException {
        log.info("getting person: id={}", id);
        return super.get(id);
    }
    
    @Override
    public Person persist(Person entity) throws HibernateException {
        return super.persist(entity);
    }
}
