package br.ufal.ic.academico.models.secretary;

import br.ufal.ic.academico.models.GeneralDAO;
import br.ufal.ic.academico.models.department.Department;
import br.ufal.ic.academico.models.department.DepartmentDAO;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class SecretaryDAO extends GeneralDAO<Secretary> {
    public SecretaryDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public ArrayList<Secretary> getAll() {
        return (ArrayList<Secretary>) currentSession().createQuery("from Secretary").list();
    }

    public Department getDepartment(Secretary secretary) {
        if (secretary == null) {
            return null;
        }

        String type = secretary.getType().equals("GRADUATION") ? "graduation" : "postGraduation";
        String query = "select D from Department D where D." + type + ".id = " + secretary.getId();
        return (Department) currentSession().createQuery(query).uniqueResult();
    }
}
