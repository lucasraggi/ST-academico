package br.ufal.ic.academico.models.department;

import br.ufal.ic.academico.models.GeneralDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import java.util.ArrayList;

@Slf4j
public class DepartmentDAO extends GeneralDAO<Department> {
    public DepartmentDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public ArrayList<Department> getAll() {
        return (ArrayList<Department>) currentSession().createQuery("from Department").list();
    }
}
