package br.ufal.ic.academico.models.secretary;

import br.ufal.ic.academico.models.department.Department;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class Secretary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    String type;

    public Secretary(SecretaryDTO entity) {
        this.type = entity.type.toUpperCase().equals("POST-GRADUATION") ? "POST-GRADUATION" : "GRADUATION";
    }
}
