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
     * Simple Euclidean algorithm to find the gcd of two integers.
     */
    static int gcd(int a, int b){
        if(a == 0 || b == 0)
            return 0;
        if(a == 1 || b == 1)
            return 1;
        a = Math.abs(a);
        b = Math.abs(b);
        int remainder = 1;
        int smaller = Math.min(a, b);
        int larger = Math.max(a, b);
        while(remainder > 0){
            remainder = larger % smaller;
            larger = smaller;
            smaller = remainder;
        }
        return larger;
    }

    /**
     * Attempt to simplify problem by reducing cost and each item-cost by finding gcd.
     * If gcd is 1, the problem cannot be simplified. Then do nothing.
     */
    private void simplifyProblem(){
        moddedMaxCost = (int) problem.maxCost;
        moddedItemCost = Arrays.stream(problem.itemCost).mapToInt(cost -> (int) cost).toArray();
        itemValue = Arrays.stream(problem.itemValue).mapToInt(value -> (int) value).toArray();
        int gcd = DynamicSolver.gcd(Arrays.stream(moddedItemCost).sorted().reduce(DynamicSolver::gcd).getAsInt(), moddedMaxCost);
        if(gcd == 1)
            return;
        moddedMaxCost = moddedMaxCost / gcd;
        moddedItemCost = Arrays.stream(moddedItemCost).map(i -> i/gcd).toArray();
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
