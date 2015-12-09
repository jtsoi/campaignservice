package io.ordolabs.campaignservice.core;

import java.lang.Integer;
import java.lang.Double;
import java.util.Arrays;

/**
 * The Greedy solution sorts the items by "utility" (value/cost) and then,
 * greedily attempts to fit items into the knapsack, starting at the highest utility.
 * At worst, it should result in 50% fill.
 * https://en.wikipedia.org/wiki/Knapsack_problem#Greedy_approximation_algorithm
 */
public class GreedySolver extends BaseSolver{

    double[] itemUtility;
    Integer[] sortedByUtility;

    public GreedySolver(KnapsackProblem problem){
        super(problem);
    }

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

        long remainingCapacity = problem.maxCost;
        long[] itemCount = new long[problem.itemCount];
        for (Integer itemIdx : sortedByUtility) {
            long maxCountForItem = remainingCapacity / problem.itemCost[itemIdx];
            itemCount[itemIdx] = maxCountForItem;
            remainingCapacity -= problem.itemCost[itemIdx] * maxCountForItem;
        }

        return new KnapsackSolution(problem, itemCount, false);
    }

    @Override
    public boolean canSolve() {
        return true;
    }
}
