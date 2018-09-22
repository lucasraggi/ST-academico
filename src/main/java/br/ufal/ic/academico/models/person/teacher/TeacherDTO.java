package br.ufal.ic.academico.models.person.teacher;

import br.ufal.ic.academico.models.person.PersonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class TeacherDTO extends PersonDTO {
    public String name, role;

    public TeacherDTO(Teacher entity) {
        name = entity.getName();
        role = entity.getRole();
    }
}
