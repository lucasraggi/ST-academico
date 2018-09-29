package br.ufal.ic.academico.models.department;

import br.ufal.ic.academico.models.secretary.Secretary;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    String name;

    @OneToOne(cascade = CascadeType.ALL)
    @Setter
    Secretary graduation, postGraduation;

    public Department(DepartmentDTO entity) {
        this.name = entity.name;
    }

    public Department(String name) {
        this.name = name;
    }

    public void update(DepartmentDTO entity) {
        if (entity.name != null) {
            this.name = entity.name;
        }
    }
}
