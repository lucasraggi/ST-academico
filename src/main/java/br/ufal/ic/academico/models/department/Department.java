package br.ufal.ic.academico.models.department;

import br.ufal.ic.academico.models.secretary.Secretary;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Setter
    Secretary[] secretaries;
}
