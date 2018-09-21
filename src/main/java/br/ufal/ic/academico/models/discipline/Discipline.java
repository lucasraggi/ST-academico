package br.ufal.ic.academico.models.discipline;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    String name;

    @Getter
    @Setter
    int credits, requiredCredits;

    @Getter
    @Setter
    @ManyToMany(cascade = CascadeType.ALL)
    List<Discipline> requiredDisciplines;

    public Discipline(DisciplineDTO entity) {
        this.name = entity.name;
        this.credits = entity.credits;
        this.requiredCredits = entity.requiredCredits;

        LinkedList<Discipline> requiredDisciplines = new LinkedList<>();
        entity.requiredDisciplines.forEach(d -> requiredDisciplines.add(new Discipline(d)));
        this.requiredDisciplines = requiredDisciplines;
    }
}
