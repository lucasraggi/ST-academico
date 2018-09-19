package br.ufal.ic.academico.models.department;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@RequiredArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    private String name;

    Department(String name) {
        this.name = name;
    }
}
