package io.ordolabs.campaignservice.core;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class DynamicSolver extends BaseSolver{

    double[] itemUtility;
    Integer[] sortedByUtility;

    public DynamicSolver(KnapsackProblem problem){
        super(problem);
    }

    /**
     * The Greedy solution sorts the items by "utility" (value/cost) and then,
     * greedily attempts to fit items into the knapsack, starting at the highest utility.
     * At worst, it should result in 50% fill.
     * https://en.wikipedia.org/wiki/Knapsack_problem#Greedy_approximation_algorithm
     */
    @Override
    public KnapsackSolution solve(){
        itemUtility = new double[problem.itemCount];
        sortedByUtility = new Integer[problem.itemCount];
        for(int i = 0; i< problem.itemCount; i++){
            itemUtility[i] = problem.itemValue[i] / (double) problem.itemCost[i];
            sortedByUtility[i] = i;
        }
        // Note reverse sort
        Arrays.sort(sortedByUtility, (a, b) -> -Double.compare(itemUtility[a], itemUtility[b]));

        long remainingCapacity = problem.limit;
        long[] itemCount = new long[problem.itemCount];
        for (Integer itemIdx : sortedByUtility) {
            while (remainingCapacity >= problem.itemCost[itemIdx]) {
                remainingCapacity -= problem.itemCost[itemIdx];
                itemCount[itemIdx]++;
            }
        }
        try {
            TimeUnit.SECONDS.sleep(30);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new KnapsackSolution(problem, itemCount);
    }

    @Override
    public boolean canSolve() {
        return true;
    }
}
