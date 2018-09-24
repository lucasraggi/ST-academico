package br.ufal.ic.academico.models.discipline;

import br.ufal.ic.academico.models.person.student.Student;
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
    List<StudentDTO> students;

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
        ArrayList<StudentDTO> dtoList = new ArrayList<>();
        entity.students.forEach(s -> dtoList.add(new StudentDTO(s)));
        this.students = dtoList;
    }

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    private class StudentDTO {
        public Long id;
        public String name;

        StudentDTO(Student entity) {
            this.id = entity.getId();
            this.name = entity.getFirstname() + (entity.getLastName() != null ? " " + entity.getLastName() : "");
        }
    }
}
