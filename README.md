# Endpoints List
## Requisitos funcionais
### Requisito 1
- *Path:* /enrollment
- *Method:* GET
> lista todos os alunos da universidade
---
- *Path:* /enrollment/<student_pk>
- *Method:* GET
> lista todas as disciplinas do departamento do curso do aluno
---
- *Path:* /enrollment/<student_pk>/<discipline_pk>
- *Method:* POST
> deve informar se o aluno foi matriculado ou não, justificando nesse ultimo caso
---

### Requisito 3
- *Path:* /departament/<departament_pk>/<secretary_pk>
- *Method:* GET
> lista todas as disciplinas sendo ofertadas, com seus nomes, códigos, números de créditos, os códigos dos pré-requisitos e os números de créditos mínimos
---
- *Path:* /departament/<departament_pk>/<secretary_pk>/<discipline_pk>
- *Method:* GET
> lista o código, número de créditos, os códigos dos pré-requisitos, o número de créditos mínimo, o nome da disciplina, o nome do professor responsável e a lista de alunos matriculados (com seus nomes e números de matrícula)
---
- *Path:* /enrollment/proof/<student_pk>
- *Method:* GET
> retorna o comprovante de matrícula do aluno, ou seja, uma lista com seu nome e número de matrícula, e com os códigos e nomes das disciplinas nas quais o aluno está matriculado
---
