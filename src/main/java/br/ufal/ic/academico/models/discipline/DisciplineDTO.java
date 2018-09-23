package br.ufal.ic.academico.models.discipline;

import br.ufal.ic.academico.models.person.student.StudentDTO;
import br.ufal.ic.academico.models.person.teacher.Teacher;
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
public class DisciplineDTO {
    Long id;
    String code;
    String name;
    Integer credits = 0, requiredCredits = 0;
    List<String> requiredDisciplines;
    String teacher;
    List<String> students;

    public DisciplineDTO(Discipline entity) {
        this.id = entity.getId();
        this.code = entity.code;
        this.name = entity.name;
        this.credits = entity.credits;
        this.requiredCredits = entity.requiredCredits;
        this.requiredDisciplines = entity.requiredDisciplines;
        if (entity.teacher != null) {
            this.teacher = entity.teacher.getFirstname() + (entity.teacher.getLastName() != null ? " " + entity.teacher.getLastName() : "");
        }
        ArrayList<String> students = new ArrayList<>();
        entity.students.forEach(s -> students.add("[" + s.getId() + "] " + s.getFirstname() + (s.getLastName() != null ? " " + s.getLastName() : "")));
        this.students = students;

    }
}
