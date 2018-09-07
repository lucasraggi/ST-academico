package br.ufal.ic.academico.exemplos;

import io.dropwizard.hibernate.AbstractDAO;
import java.io.Serializable;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.Order;

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

    public void delete(Person entity) throws HibernateException {
        super.currentSession().delete(entity);
    }

    public List<Order> getAll() {
        log.info(String.valueOf(super.criteriaQuery()));
        return super.criteriaQuery().getOrderList();
    }
}
