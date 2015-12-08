package io.ordolabs.campaignservice.resources;

import io.ordolabs.campaignservice.api.Solution;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Path("/solutions")
@Produces(MediaType.APPLICATION_JSON)
public class SolutionResource {


    private AtomicLong nextSolutionId = new AtomicLong();
    private ConcurrentHashMap<Long, Solution> solutionStore = new ConcurrentHashMap<>();

    public Solution addSolution(Solution solution){
        solution.setId(nextSolutionId.getAndIncrement());
        solutionStore.put(solution.getId(), solution);
        return solution;
    }

    @GET
    @Path("{id}")
    public Solution read(@PathParam("id") Long id) {

        if(solutionStore.containsKey(id)){
            return solutionStore.get(id);
        }else{
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
}
