package br.ufal.ic.academico.models.person.student;

import br.ufal.ic.academico.models.person.PersonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class StudentDTO extends PersonDTO {
    public String name, role;
    public Integer credits;

    public StudentDTO(Student entity) {
        this.name = entity.getName();
        this.role = entity.getRole();
        this.credits = entity.getCredits();
    }
}
