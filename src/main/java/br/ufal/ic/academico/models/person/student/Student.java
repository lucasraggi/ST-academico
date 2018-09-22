package br.ufal.ic.academico.models.person.student;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

import br.ufal.ic.academico.models.person.Person;

@Entity
@Getter
@RequiredArgsConstructor
public class Student extends Person {
    @Setter
    Integer credits;

    public Student(StudentDTO entity) {
        super(entity.name, "STUDENT");
        credits = 0;
        if (entity.credits != null) {
            credits = entity.credits;
        }
    }

    public void update(StudentDTO entity) {
        super.update(entity.name, "STUDENT");
        credits = entity.credits;
    }
}
