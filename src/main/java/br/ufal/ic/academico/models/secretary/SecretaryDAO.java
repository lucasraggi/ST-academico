package br.ufal.ic.academico.models.secretary;

import br.ufal.ic.academico.models.GeneralDAO;
import br.ufal.ic.academico.models.department.Department;
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

    public Department getDepartment(Secretary secretary) {
        String query = "select D from Department D, Secretary S where D." + secretary.getType().toLowerCase() + ".id = S.id";
        return (Department) currentSession().createQuery(query).list().get(0);
    }
}
