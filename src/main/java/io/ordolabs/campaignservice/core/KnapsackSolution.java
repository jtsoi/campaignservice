package io.ordolabs.campaignservice.core;

public class KnapsackSolution {

    final long[] itemCount;
    final boolean optimal;
    final KnapsackProblem problem;

    public KnapsackSolution(KnapsackProblem problem, long[] itemCount, boolean optimal) {
        this.problem = problem;
        this.optimal = optimal;
        this.itemCount = itemCount;
    }

    public long[] getItemCount(){
        return itemCount;
    }

    public boolean isOptimal(){
        return optimal;
    }

    public KnapsackProblem getProblem(){
        return problem;
    }
}
