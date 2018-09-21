package br.ufal.ic.academico.models.course;

import br.ufal.ic.academico.models.discipline.DisciplineDTO;
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
public class CourseDTO {
    String name;
    List<DisciplineDTO> disciplines;

    public CourseDTO(Course entity) {
        this.name = entity.name;

        LinkedList<DisciplineDTO> disciplines = new LinkedList<>();
        entity.disciplines.forEach(d -> disciplines.addLast(new DisciplineDTO(d)));
        this.disciplines = disciplines;
    }
}
