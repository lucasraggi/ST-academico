package br.ufal.ic.academico.models.discipline;

import br.ufal.ic.academico.models.course.Course;
import br.ufal.ic.academico.models.person.student.Student;
import br.ufal.ic.academico.models.person.student.StudentDTO;
import br.ufal.ic.academico.models.person.teacher.Teacher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    String name;

    String code;

    @Setter
    Integer credits, requiredCredits;

    @Setter
    @ElementCollection
    List<String> requiredDisciplines;

    @Setter
    @OneToOne
    Teacher teacher;

    @ManyToMany
    List<Student> students;

    public Discipline(DisciplineDTO entity) {
        this.code = entity.code;
        this.name = entity.name;
        if (entity.credits != null) {
            this.credits = entity.credits;
        } else {
            this.credits = 0;
        }
        if (entity.requiredCredits != null) {
            this.requiredCredits = entity.requiredCredits;
        } else {
            this.requiredCredits = 0;
        }
        if (entity.requiredDisciplines != null) {
            this.requiredDisciplines = entity.requiredDisciplines;
        } else {
            this.requiredDisciplines = new ArrayList<>();
        }
    }

    public void update(DisciplineDTO entity) {
        if (entity.name != null) {
            name = entity.name;
        }
        if (entity.credits != null) {
            credits = entity.credits;
        }
        if (entity.requiredCredits != null) {
            requiredCredits = entity.requiredCredits;
        }
        if (entity.requiredDisciplines != null) {
            requiredDisciplines = entity.requiredDisciplines;
        }
    }

    public String enroll(Course disciplineCourse, Student student) {
        if (student.getCourse() == null) {
            return "Student isn't enrolled in any course.";
        } if (!student.getCourse().getId().equals(disciplineCourse.getId())) {
            return "Student course doesn't correspond to the discipline course. Student course is '" + student.getCourse().getName() +
                    "', discipline course is '" + disciplineCourse.getName() + "'.";
        }
        if (student.getCredits() < requiredCredits) {
            return "Student doesn't have enough credits. Required " + requiredCredits + ", has " + student.getCredits() +".";
        }
        for (String code : requiredDisciplines) {
            if (!student.getCompletedDisciplines().contains(code)) {
                return "Student hasn't completed the discipline " + code + ".";
            }
        }
        if (this.students.contains(student)) {
            return "Student already enrolled at this discipline.";
        }
        if (student.getCompletedDisciplines().contains(this.code)) {
            return "Student has already completed this discipline.";
        }

        this.students.add(student);
        return null;
    }

    public boolean removeStudent(Student student) {
        return this.students.remove(student);
    }
}
