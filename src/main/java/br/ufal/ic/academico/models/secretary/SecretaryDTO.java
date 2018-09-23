package br.ufal.ic.academico.models.secretary;

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
    public Long id;
    public String type;
    public List<String> courses = new LinkedList<>();

    public SecretaryDTO(Secretary entity) {
        this.id = entity.getId();
        this.type = entity.type;
        if (entity.courses != null) {
            LinkedList<String> courses = new LinkedList<>();
            entity.courses.forEach(c -> courses.addLast(c.getName()));
            this.courses = courses;
        }
    }
}
