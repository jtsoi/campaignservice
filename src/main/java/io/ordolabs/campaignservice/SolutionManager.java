package io.ordolabs.campaignservice;

import io.dropwizard.lifecycle.Managed;
import io.ordolabs.campaignservice.api.Problem;
import io.ordolabs.campaignservice.api.Solution;
import io.ordolabs.campaignservice.core.*;
import io.ordolabs.campaignservice.resources.SolutionResource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * SolutionManager implements the strategy to find a naive
 * solution as fast as possible, and then attempt to find an optimal solution.
 *
 * when thinkAbout() is invoked, a GreedySolver is used to find a
 * OK solution in linear time.
 * And then a task is created to be offloaded to an ExecutorService,
 * to find a more optimal solution.
 *
 * The second solution will be either solved with DynamicSolver or
 * BranchAndBoundSolver.
 *
 * DynamicSolver can only solve limited set of problems, so
 * we evaluate first if the problem can be solved using DynamicSolver.
 * DynamicSolver is much more efficient than BnBSolver.
 */
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

        GreedySolver greedySolver = new GreedySolver(knapsackProblem);
        KnapsackSolution knapsackSolution1 = greedySolver.solve();
        Solution solution1 = Solution.fromKnapsackSolution(problem, knapsackSolution1);

        solutionResource.addSolution(solution1);
        problem.setSolutionId(solution1.getId());

        // Offload search for possible optimal solution to a thread pool
        threadPoolExecutor.submit(() -> {
            KnapsackSolution knapsackSolution2 = null;

            DynamicSolver dynamicSolver = new DynamicSolver(knapsackProblem);
            if(dynamicSolver.canSolve()){
                knapsackSolution2 = dynamicSolver.solve();
            }else{
                BranchAndBoundSolver bnbSolver = new BranchAndBoundSolver(knapsackProblem, 60);
                knapsackSolution2 = bnbSolver.solve();
            }

            Solution solution2 = Solution.fromKnapsackSolution(problem, knapsackSolution2);

            solutionResource.addSolution(solution2);
            problem.setSolutionId(solution2.getId());
            problem.setOptimalSolution(knapsackSolution2.isOptimal());
        });

    }
}
