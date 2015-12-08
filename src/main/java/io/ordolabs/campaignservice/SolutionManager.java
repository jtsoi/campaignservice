package io.ordolabs.campaignservice;

import io.dropwizard.lifecycle.Managed;
import io.ordolabs.campaignservice.api.Problem;
import io.ordolabs.campaignservice.api.Solution;
import io.ordolabs.campaignservice.core.*;
import io.ordolabs.campaignservice.resources.SolutionResource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SolutionManager implements Managed{

    private SolutionResource solutionResource;
    private ExecutorService threadPoolExecutor;
    private final int cores = Math.max(Runtime.getRuntime().availableProcessors() - 1, 1);

    public SolutionManager(SolutionResource solutionResource){
        this.solutionResource = solutionResource;
    }

    @Override
    public void start() throws Exception {
        this.threadPoolExecutor = Executors.newWorkStealingPool(cores);
    }

    @Override
    public void stop() throws Exception {
        try {
            threadPoolExecutor.shutdown();
            threadPoolExecutor.awaitTermination(5, TimeUnit.SECONDS);
        }
        finally {
            threadPoolExecutor.shutdownNow();
        }
    }

    public void thinkAbout(final Problem problem){
        final KnapsackProblem knapsackProblem = problem.getKnapsackProblem();

        GreedySolver naiveSolver = new GreedySolver(knapsackProblem);
        KnapsackSolution knapsackSolution = naiveSolver.solve();
        Solution naiveSolution = Solution.fromKnapsackSolution(problem, knapsackSolution);

        solutionResource.addSolution(naiveSolution);
        problem.setSolutionId(naiveSolution.getId());

        // Offload search for optimal solution to a thread pool
        threadPoolExecutor.submit(() -> {
            BaseSolver optimalSolver = new DynamicSolver(knapsackProblem);
            if(!optimalSolver.canSolve()){
                optimalSolver = new BranchAndBoundSolver(knapsackProblem);
            }
            KnapsackSolution optimalKnapsackSolution = optimalSolver.solve();
            if(optimalKnapsackSolution == null){
                return;
            }
            Solution optimalSolution = Solution.fromKnapsackSolution(problem, knapsackSolution);

            solutionResource.addSolution(optimalSolution);
            problem.setSolutionId(optimalSolution.getId());
            problem.setOptimalSolution(true);
        });

    }
}
