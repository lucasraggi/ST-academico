package br.ufal.ic.academico.models.secretary;

import br.ufal.ic.academico.models.course.Course;
import br.ufal.ic.academico.models.course.CourseDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.LinkedList;
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
        this.courses = coursesDTOListToCoursesList(entity.courses);
    }

    public void update(SecretaryDTO entity) {
        if (entity.type != null) {
            this.type = entity.type;
        }
        this.courses = coursesDTOListToCoursesList(entity.courses);
    }

    private LinkedList<Course> coursesDTOListToCoursesList(List<CourseDTO> courseDTOs) {
        LinkedList<Course> courses = new LinkedList<>();
        if (courseDTOs != null) {
            courseDTOs.forEach(c -> courses.addLast(new Course(c)));
        }
        return courses;
    }

    public Course addCourse(CourseDTO entity) {
        Course newCourse = new Course(entity);
        if (this.courses.add(newCourse)) {
            return newCourse;
        } else {
            return null;
        }
    }

    public void deleteCourse(Course c) {
        this.courses.remove(c);
    }
}
