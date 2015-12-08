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

    public long getPackedValue(){
        long value = 0;
        for(int i=0; i<itemCount.length;i++){
            value += problem.itemValue[i]  * itemCount[i];
        }
        return value;
    }
}
