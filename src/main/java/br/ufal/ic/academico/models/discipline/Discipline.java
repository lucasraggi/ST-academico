package br.ufal.ic.academico.models.discipline;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
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
    Integer credits, requiredCredits;

    @Getter
    @Setter
    @ManyToMany(cascade = CascadeType.ALL)
    List<Discipline> requiredDisciplines;

    public Discipline(DisciplineDTO entity) {
        this.name = entity.name;
        if (entity.credits != null) {
            this.credits = entity.credits;
        } else {
            this.credits = 0;
        }
        if (entity.requiredCredits != null) {
            this.requiredCredits = entity.requiredCredits;
        } else {
            this.requiredCredits = 0;
        }
        this.requiredDisciplines = disciplineDTOListToDisciplineList(entity.requiredDisciplines);
    }

    public void update(DisciplineDTO entity) {
        if (entity.name != null) {
            name = entity.name;
        }
        if (entity.credits != null) {
            credits = entity.credits;
        }
        if (entity.requiredCredits != null) {
            requiredCredits = entity.requiredCredits;
        }
        if (entity.requiredDisciplines != null) {
            requiredDisciplines = disciplineDTOListToDisciplineList(entity.requiredDisciplines);
        }
    }

    private List<Discipline> disciplineDTOListToDisciplineList(List<DisciplineDTO> disciplineDTOs) {
        ArrayList<Discipline> disciplines = new ArrayList<>();
        if (disciplineDTOs != null) {
            disciplineDTOs.forEach(d -> disciplines.add(new Discipline(d)));
        }
        return disciplines;
    }
}
