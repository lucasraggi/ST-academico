package br.ufal.ic.academico.models.secretary;

import br.ufal.ic.academico.models.course.Course;
import br.ufal.ic.academico.models.course.CourseDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Secretary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    String type;

    @OneToMany(cascade = CascadeType.ALL)
    List<Course> courses;

    public Secretary(SecretaryDTO entity) {
        this.type = entity.type.toUpperCase().equals("POST-GRADUATION") ? "POST-GRADUATION" : "GRADUATION";
        this.courses = new ArrayList<>();
    }

    public Secretary(String type) {
        this.type = type.toUpperCase().equals("POST-GRADUATION") ? "POST-GRADUATION" : "GRADUATION";
        this.courses = new ArrayList<>();
    }

    public boolean addCourse(Course course) {
        return this.courses.add(course);
    }

    public void deleteCourse(Course c) {
        this.courses.remove(c);
    }

    public boolean isGraduation() {
        return this.type.equals("GRADUATION");
    }
}
