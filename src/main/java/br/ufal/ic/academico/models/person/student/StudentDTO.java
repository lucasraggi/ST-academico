package br.ufal.ic.academico.models.person.student;

import br.ufal.ic.academico.models.course.CourseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class StudentDTO {
    public String firstName, lastName, role;
    public Integer credits;
    public CourseDTO course;

    public StudentDTO(Student entity) {
        this.firstName = entity.getFirstname();
        this.lastName = entity.getLastName();
        this.role = entity.getRole();
        this.credits = entity.getCredits();
        if (entity.course != null) {
            this.course = new CourseDTO(entity.getCourse());
        }
    }
}
