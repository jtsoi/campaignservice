package io.ordolabs.campaignservice.core;

import java.util.Arrays;

/**
 * Dynamic programming, algorithm from:
 * http://www.ifors.ms.unimelb.edu.au/tutorial/knapsack/dp_unbounded.html
 *
 * Will trade space for time, has time complexity of O(nW)
 * space complexity of O(W), where W is knapsack capacity, and n is number of items.
 * Only works for reasonable values of W.
 *
 */
public class DynamicSolver extends BaseSolver{

    public DynamicSolver(KnapsackProblem problem){
        super(problem);
    }

    int moddedMaxCost;
    int[] moddedItemCost;
    int[] itemValue;

    int[] valueAtCost;
    int[] lastItemAdded;

    /**
     * Attempt to simplify problem by removing trailing zeros from cost and each item-cost.
     * Each successful loop will reduce the memory requirements by an order of magnitude.
     */
    private void simplifyProblem(){
        moddedMaxCost = (int) problem.maxCost;
        moddedItemCost = Arrays.stream(problem.itemCost).mapToInt(cost -> (int) cost).toArray();
        itemValue = Arrays.stream(problem.itemValue).mapToInt(value -> (int) value).toArray();
        while(moddedMaxCost % 10 == 0 && Arrays.stream(moddedItemCost).allMatch(c -> c % 10 == 0)){
            moddedMaxCost = moddedMaxCost / 10;
            for(int itemIdx=0; itemIdx<moddedItemCost.length;itemIdx++){
                moddedItemCost[itemIdx] = moddedItemCost[itemIdx] / 10;
            }
        }
    }

    @Override
    public KnapsackSolution solve(){
        valueAtCost = new int[moddedMaxCost + 1];
        lastItemAdded = new int[moddedMaxCost + 1];
        Arrays.fill(lastItemAdded, -1);

        // Fill valueAtCost array
        for (int cost = 1; cost < valueAtCost.length; cost++) {
            for (int itemIdx = 0; itemIdx < problem.itemCount; itemIdx++) {
                if (moddedItemCost[itemIdx] <= cost &&
                        (itemValue[itemIdx] + valueAtCost[cost - moddedItemCost[itemIdx]]) > valueAtCost[cost]){
                    valueAtCost[cost] = itemValue[itemIdx] + valueAtCost[cost - moddedItemCost[itemIdx]];
                    lastItemAdded[cost] = itemIdx;
                }
            }
        }

        // Backtrack solution
        long[] itemCount = new long[problem.itemCount];
        int remainingCost = moddedMaxCost;
        int lastItemIdx = lastItemAdded[remainingCost];
        while (lastItemIdx != -1 && remainingCost > 0)
        {
            itemCount[lastItemIdx]++;
            remainingCost = remainingCost - moddedItemCost[lastItemIdx];
            lastItemIdx = lastItemAdded[remainingCost];
        }

        return new KnapsackSolution(problem, itemCount, true);
    }

    /**
     * Allow Dynamic algorithm to run, iff the simplified problem is
     * fewer iterations than 80MM, as dynamic algorithm is O(nW) time complexity
     * and O(W) space complexity where n is item count and W is max cost.
     *
     * Dynamic algorithm is trading space for time, and we need
     * two int[] for this to work, so we need to limit size of W*4*2 < 1GB or so.
     * 80MM will consume about 600+MB, a good compromise.
     */
    @Override
    public boolean canSolve() {
        if(problem.maxCost > Integer.MAX_VALUE){
            return false;
        }
        simplifyProblem();
        return (moddedMaxCost) <= 80000000;
    }
}
