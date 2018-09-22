package br.ufal.ic.academico.models.person.teacher;

import br.ufal.ic.academico.models.discipline.DisciplineDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class TeacherDTO {
    public String firstName, lastName, role;
    List<DisciplineDTO> disciplines = new ArrayList<>();

    public TeacherDTO(Teacher entity) {
        this.firstName = entity.getFirstname();
        this.lastName = entity.getLastName();
        this.role = entity.getRole();
        ArrayList<DisciplineDTO> disciplines = new ArrayList<>();
        if (entity.disciplines != null) {
            entity.disciplines.forEach(d -> disciplines.add(new DisciplineDTO(d)));
        }
        this.disciplines = disciplines;
    }
}
