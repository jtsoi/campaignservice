package io.ordolabs.campaignservice.core;

public class KnapsackSolution {

    final long[] itemCount;
    final KnapsackProblem problem;

    public KnapsackSolution(KnapsackProblem problem, long[] itemCount) {
        this.problem = problem;
        this.itemCount = itemCount;
    }

    public long[] getItemCount(){
        return itemCount;
    }

    public KnapsackProblem getProblem(){
        return problem;
    }
}
