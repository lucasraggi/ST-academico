package br.ufal.ic.academico.models.secretary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class SecretaryDTO {
    public String type;

    public SecretaryDTO(Secretary entity) {
        if (entity != null) {
            this.type = entity.type;
        }
    }
}
