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

        return Response.ok(disciplineDAO.getAll()).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response get(@PathParam("id") Long id) {
        log.info("get discipline: id={}", id);

        return Response.ok(disciplineDAO.get(id)).build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, DisciplineDTO entity) {
        log.info("update discipline: id={}", id);

        Discipline d = disciplineDAO.get(id);
        d.update(entity);
        return Response.ok(disciplineDAO.persist(d)).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response deleteCourse(@PathParam("id") Long id) {
        log.info("delete discipline {}", id);

        Discipline d = disciplineDAO.get(id);
        Course c = disciplineDAO.getCourse(d);
        c.deleteDiscipline(d);

        courseDAO.persist(c);
        disciplineDAO.delete(d);
        return Response.noContent().build();
    }
}
