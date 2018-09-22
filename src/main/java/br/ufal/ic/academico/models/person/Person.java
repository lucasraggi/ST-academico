package br.ufal.ic.academico.models.person;

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
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    protected String name, role;

    public Person(String name, String role) {
        this.name = name;
        this.role = role;
    }

    protected void update(String name, String role) {
        this.name = name;
        this.role = role;
    }
}
