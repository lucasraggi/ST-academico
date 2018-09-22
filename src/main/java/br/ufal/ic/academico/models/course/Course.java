package br.ufal.ic.academico.models.course;

import br.ufal.ic.academico.models.discipline.Discipline;
import br.ufal.ic.academico.models.discipline.DisciplineDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    String name;

    @Getter
    @ManyToMany(cascade = {CascadeType.ALL})
    List<Discipline> disciplines;

    public Course(CourseDTO entity) {
        this.name = entity.name;
        this.disciplines = disciplineDTOListToDisciplineList(entity.disciplines);
    }

    public void update(CourseDTO entity) {
        if (entity.name != null) {
            this.name = entity.name;
        }
        this.disciplines = disciplineDTOListToDisciplineList(entity.disciplines);
    }

    private List<Discipline> disciplineDTOListToDisciplineList(List<DisciplineDTO> disciplineDTOs) {
        ArrayList<Discipline> disciplines = new ArrayList<>();
        if (disciplineDTOs != null) {
            disciplineDTOs.forEach(d -> disciplines.add(new Discipline(d)));
        }
        return disciplines;
    }
}
