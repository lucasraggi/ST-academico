package br.ufal.ic.academico.models.person.teacher;

import br.ufal.ic.academico.models.discipline.Discipline;
import br.ufal.ic.academico.models.discipline.DisciplineDTO;
import br.ufal.ic.academico.models.person.Person;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Teacher extends Person {
    @ManyToMany(cascade = {CascadeType.ALL})
    List<Discipline> disciplines;

    public Teacher(TeacherDTO entity) {
        super(entity.firstName, entity.lastName, "TEACHER");
        disciplines = disciplineDTOListToDisciplineList(entity.disciplines);
    }

    public void update(TeacherDTO entity) {
        super.update(entity.firstName, entity.lastName, "TEACHER");
    }

    private List<Discipline> disciplineDTOListToDisciplineList(List<DisciplineDTO> disciplineDTOs) {
        List<Discipline> disciplines = new ArrayList<>();
        if (disciplineDTOs != null) {
            disciplineDTOs.forEach(d -> disciplines.add(new Discipline(d)));
        }
        return disciplines;
    }
}
