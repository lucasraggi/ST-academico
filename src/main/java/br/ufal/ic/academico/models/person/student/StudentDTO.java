package br.ufal.ic.academico.models.person.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class StudentDTO {
    public String name;
    public Integer credits;

    public StudentDTO(Student entity) {
        this.name = entity.getName();
        this.credits = entity.getCredits();
    }
}
