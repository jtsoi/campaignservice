package io.ordolabs.campaignservice.core;

public abstract class BaseSolver {

    final KnapsackProblem problem;

    public BaseSolver(KnapsackProblem problem){
        this.problem = problem;
    }

    public abstract KnapsackSolution solve();

    public abstract boolean canSolve();

}
