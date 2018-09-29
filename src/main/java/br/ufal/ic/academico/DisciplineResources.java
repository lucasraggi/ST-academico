package br.ufal.ic.academico;

import br.ufal.ic.academico.models.course.Course;
import br.ufal.ic.academico.models.course.CourseDAO;
import br.ufal.ic.academico.models.discipline.Discipline;
import br.ufal.ic.academico.models.discipline.DisciplineDAO;
import br.ufal.ic.academico.models.discipline.DisciplineDTO;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("discipline")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class DisciplineResources {
    private final CourseDAO courseDAO;
    private final DisciplineDAO disciplineDAO;

    @GET
    @UnitOfWork
    public Response getAll() {
        log.info("getAll disciplines");

        return Response.ok(disciplineListToDTOList(disciplineDAO.getAll())).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response get(@PathParam("id") Long id) {
        log.info("get discipline: id={}", id);

        Discipline d = disciplineDAO.get(id);
        if (d == null) {
            return Response.status(404).entity("Discipline not found.").build();
        }
        return Response.ok(new DisciplineDTO(d)).build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, DisciplineDTO entity) {
        log.info("update discipline: id={}", id);

        Discipline d = disciplineDAO.get(id);
        if (d == null) {
            return Response.status(404).entity("Discipline not found.").build();
        }
        d.update(entity);
        return Response.ok(new DisciplineDTO(disciplineDAO.persist(d))).build();
    }
// ToDo Resolver esse DELETE
    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response deleteCourse(@PathParam("id") Long id) {
        log.info("delete discipline {}", id);

        Discipline d = disciplineDAO.get(id);
        if (d == null) {
            return Response.status(404).entity("Discipline not found.").build();
        }

        Course c = disciplineDAO.getCourse(d);
        c.deleteDiscipline(d);
        courseDAO.persist(c);
        disciplineDAO.delete(d);
        return Response.noContent().build();
    }

    private List<DisciplineDTO> disciplineListToDTOList(List<Discipline> list) {
        List<DisciplineDTO> dtoList = new ArrayList<>();
        if (list != null) {
            list.forEach(d -> dtoList.add(new DisciplineDTO(d)));
        }
        return dtoList;
    }
}
