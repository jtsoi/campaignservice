package io.ordolabs.campaignservice.core;

import java.util.Arrays;

/**
 * Brute force solver, with time complexity of O((W*n)^2)
 * Algorithm taken from:
 * http://www.ifors.ms.unimelb.edu.au/tutorial/knapsack/bb_unbounded.html
 *
 * Guaranteed to produce at least the greedy solution.
 * Can be time boxed, and will return best solution it can find during that time.
 */
public class BranchAndBoundSolver extends BaseSolver{

    int timeLimit;
    long startTime;
    boolean optimalSolutionFound;
    double[] itemUtility;
    long[] itemCount;
    Integer[] itemRemap;

    long[] orderedItemValue;
    long[] orderedItemCost;

    long[] bestSelection;
    long remainingCapacity;
    long bestValue;

    public BranchAndBoundSolver(KnapsackProblem problem){
        super(problem);
    }

    public BranchAndBoundSolver(KnapsackProblem problem, int timeLimit){
        super(problem);
        this.timeLimit = timeLimit;
    }

    /**
     * Step 1,
     * Reorder items by their "utility": value/cost, highest to lowest.
     * We use a lookup array "itemRemap", and then create two helpers:
     * - orderedItemValue
     * - orderedItemCost
     */
    void orderByUtility(){
        itemCount = new long[problem.itemCount];
        itemUtility = new double[problem.itemCount];
        itemRemap = new Integer[problem.itemCount];
        for(int itemIdx = 0; itemIdx < problem.itemCount; itemIdx++){
            itemUtility[itemIdx] = problem.itemValue[itemIdx] / (double) problem.itemCost[itemIdx];
            itemRemap[itemIdx] = itemIdx;
        }

        // Note reverse sort
        Arrays.sort(itemRemap, (a, b) -> -Double.compare(itemUtility[a], itemUtility[b]));

        orderedItemValue = new long[problem.itemCount];
        orderedItemCost = new long[problem.itemCount];

        for(int itemIdx = 0; itemIdx < problem.itemCount; itemIdx++){
            orderedItemValue[itemIdx] = problem.itemValue[itemRemap[itemIdx]];
            orderedItemCost[itemIdx] = problem.itemCost[itemRemap[itemIdx]];
        }
    }

    /**
     * Step 2,
     * Create a starting point where we fill the knapsack with as many
     * high utility items as we can. This defines our "upper bound".
     * Imagine this being the "maximum" number, and next step will "count"
     * down from this.
     */
    void prepareGreedySolution(){
        bestSelection = new long[problem.itemCount];
        remainingCapacity = problem.maxCost;
        for (int itemIdx = 0; itemIdx < bestSelection.length && remainingCapacity > 0; itemIdx++) {
            long maxCountForItem = remainingCapacity / orderedItemCost[itemIdx];
            bestSelection[itemIdx] = maxCountForItem;
            remainingCapacity = remainingCapacity - (orderedItemCost[itemIdx] * maxCountForItem);
            bestValue += orderedItemValue[itemIdx] * maxCountForItem;
        }
    }

    /**
     * Step 3,
     * This is basically a counter,
     * imagine this binary knapsack: [1,0,0,0] - meaning include item @ 0, but not any others.
     * the reduceAndExpand will basically do:
     * [1,0,0,0]
     * [0,1,1,1]
     * [0,1,1,0]
     * [0,1,0,0]
     * [0,0,1,1]
     * [0,0,1,0]
     * [0,0,0,1]
     * [0,0,0,0]
     *
     * And at each step calculating the value of the knapsack, and saving the most valuable
     * combination. But for our problem, the value of each code point is not 0/1
     * but a number of items that can possibly fit into the knapsack at that point.
     */
    void reduceAndExpand() {

        long[] workingSelection = Arrays.copyOf(bestSelection, bestSelection.length);
        long workingValue = bestValue;
        int lsItemIdx; // Least significant item index.
        while(true){

            //Find the first non zero item count from the right in the working set.
            for(lsItemIdx = problem.itemCount - 1; lsItemIdx >= 0; lsItemIdx--){
                if (workingSelection[lsItemIdx] != 0){
                    break;
                }
            }

            // IF we are at the very last item, just set it to 0
            if (lsItemIdx == problem.itemCount - 1){
                remainingCapacity += workingSelection[lsItemIdx] * orderedItemCost[lsItemIdx];
                workingValue -= workingSelection[lsItemIdx] * orderedItemValue[lsItemIdx];
                workingSelection[lsItemIdx] = 0;

            // Else, reduce by one, and try to fill with other items.
            }else{
                remainingCapacity += orderedItemCost[lsItemIdx];
                workingValue -= orderedItemValue[lsItemIdx];
                workingSelection[lsItemIdx]--;

                for (int itemIdx=lsItemIdx + 1; itemIdx < workingSelection.length; itemIdx++){
                    long maxCountForItem = remainingCapacity / orderedItemCost[itemIdx];
                    workingSelection[itemIdx] = maxCountForItem;
                    remainingCapacity -= orderedItemCost[itemIdx] * maxCountForItem;
                    workingValue += orderedItemValue[itemIdx] * maxCountForItem;
                }

                if (workingValue > bestValue){
                    bestValue = workingValue;
                    bestSelection = Arrays.copyOf(workingSelection, workingSelection.length);
                }
            }
            // Break while loop if we exceeded time limit
            if(timeLimit != 0 && (System.currentTimeMillis() - startTime) / 1000 > timeLimit){
                break;
            }
            // Break loop if we looked at all possible options.
            if(Arrays.stream(workingSelection).sum() == 0){
                optimalSolutionFound = true;
                break;
            }
        }
    }

    /**
     * Remap the itemCount array back to original positions.
     */
    void remapCostArray() {
        for(int itemIdx = 0; itemIdx < bestSelection.length; itemIdx++){
            itemCount[itemRemap[itemIdx]] = bestSelection[itemIdx];
        }
    }

    @Override
    public KnapsackSolution solve(){
        startTime = System.currentTimeMillis();
        orderByUtility();
        prepareGreedySolution();
        reduceAndExpand();
        remapCostArray();
        return new KnapsackSolution(problem, itemCount, optimalSolutionFound);
    }

    @Override
    public boolean canSolve() {
        return true;
    }
}
