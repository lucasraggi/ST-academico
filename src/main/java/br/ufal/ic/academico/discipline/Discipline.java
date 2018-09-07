package br.ufal.ic.academico.discipline;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    String name;

    @Getter
    @Setter
    int credits, requiredCredits = 0;

    @Getter
    @Setter
    Discipline[] requiredDisciplines;
}
