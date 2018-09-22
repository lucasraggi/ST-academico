package br.ufal.ic.academico.models.person.student;

import br.ufal.ic.academico.models.course.Course;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import br.ufal.ic.academico.models.person.Person;

@Entity
@Getter
@RequiredArgsConstructor
public class Student extends Person {
    @Setter
    Integer credits;

    @Setter
    @ManyToOne
    Course course;

    public Student(StudentDTO entity) {
        super(entity.firstName, entity.lastName, "STUDENT");
        credits = 0;
        if (entity.credits != null) {
            credits = entity.credits;
        }
        if (entity.course != null) {
            this.course = new Course(entity.course);
        }
    }

    public void update(StudentDTO entity) {
        super.update(entity.firstName, entity.lastName, "STUDENT");
        if (entity.credits != null) {
            credits = entity.credits;
        }
        if (entity.course != null) {
            course = new Course(entity.course);
        }
    }
}
