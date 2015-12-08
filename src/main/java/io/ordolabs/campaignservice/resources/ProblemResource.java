package io.ordolabs.campaignservice.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.ordolabs.campaignservice.SolutionManager;
import io.ordolabs.campaignservice.api.Problem;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Path("/problems")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProblemResource {

    private SolutionManager solutionManager;
    private AtomicLong nextProblemId = new AtomicLong();
    private ConcurrentHashMap<Long, Problem> problemStore = new ConcurrentHashMap<>();

    public ProblemResource(SolutionManager solutionManager) {
        this.solutionManager = solutionManager;
    }

    public Problem addProblem(Problem problem){
        problem.setId(nextProblemId.getAndIncrement());
        problemStore.put(problem.getId(), problem);
        return problem;
    }

    @POST
    public Problem create(Problem problem) {
        addProblem(problem);
        solutionManager.thinkAbout(problem);
        return problem;
    }

    @GET
    @Path("{id}")
    public Problem read(@PathParam("id") Long id) {
        if(problemStore.containsKey(id)){
            return problemStore.get(id);
        }else{
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
}
