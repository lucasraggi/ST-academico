package br.ufal.ic.academico.models.discipline;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class DisciplineDTO {
    String name;
    int credits, requiredCredits = 0;
    List<DisciplineDTO> requiredDisciplines;

    public DisciplineDTO(Discipline entity) {
        this.name = entity.name;
        this.credits = entity.credits;
        this.requiredCredits = entity.requiredCredits;

        LinkedList<DisciplineDTO> requiredDisciplines = new LinkedList<>();
        entity.requiredDisciplines.forEach(d -> requiredDisciplines.addLast(new DisciplineDTO(d)));
        this.requiredDisciplines = requiredDisciplines;
    }
}
