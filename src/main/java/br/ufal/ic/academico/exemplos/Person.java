package br.ufal.ic.academico.exemplos;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Willy
 */
@Entity
@Getter
@RequiredArgsConstructor
public class Person {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    
    @Setter private int score;

    public Person(String name) {
        this.name = name;
    }
}
