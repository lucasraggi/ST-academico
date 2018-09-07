package br.ufal.ic.academico.person.student;


import br.ufal.ic.academico.person.Person;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@RequiredArgsConstructor
public class Student extends Person {
    @Setter
    Integer credits;

    public Student(StudentDTO entity) {
        name = entity.name;
        credits = 0;
        if (entity.credits != null) {
            credits = entity.credits;
        }
    }

    public void update(StudentDTO entity) {
        name = entity.name;
        credits = entity.credits;
    }
}
