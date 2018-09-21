package br.ufal.ic.academico.models.secretary;

import br.ufal.ic.academico.models.course.CourseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class SecretaryDTO {
    public String type;
    public List<CourseDTO> courses = new LinkedList<>();

    public SecretaryDTO(Secretary entity) {
        if (entity != null) {
            this.type = entity.type;
            if (entity.courses != null) {
                LinkedList<CourseDTO> courses = new LinkedList<>();
                entity.courses.forEach(c -> courses.addLast(new CourseDTO(c)));
                this.courses = courses;
            }
        }
    }
}
