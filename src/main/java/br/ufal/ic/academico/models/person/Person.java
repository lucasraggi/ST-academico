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
    protected String firstname, lastName, role;

    public Person(String firstname, String lastName, String role) {
        this.firstname = firstname;
        this.lastName = lastName;
        this.role = role;
    }

    protected void update(String firstname, String lastName, String role) {
        if (firstname != null) {
            this.firstname = firstname;
        }
        if (lastName != null) {
            this.lastName = lastName;
        }
        if (role != null) {
            this.role = role;
        }
    }
}
