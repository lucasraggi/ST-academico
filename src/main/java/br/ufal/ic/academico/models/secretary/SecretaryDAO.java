package br.ufal.ic.academico.models.secretary;

import br.ufal.ic.academico.models.GeneralDAO;
import org.hibernate.SessionFactory;

import java.util.ArrayList;

public class SecretaryDAO extends GeneralDAO<Secretary> {
    public SecretaryDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public ArrayList<Secretary> getAll() {
        return (ArrayList<Secretary>) currentSession().createQuery("from Secretary").list();
    }
}
