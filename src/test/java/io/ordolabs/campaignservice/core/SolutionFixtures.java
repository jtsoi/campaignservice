package io.ordolabs.campaignservice.core;

public class SolutionFixtures {

    public static KnapsackSolution solution1 = new KnapsackSolution(
            null,
            new long[]{0,8,0,0,0,2,1},
            true
    );
    public static KnapsackSolution solution2 = new KnapsackSolution(
            null,
            new long[]{10000,0,14,1},
            true
    );
    public static KnapsackSolution solution3 = new KnapsackSolution(
            null,
            new long[]{2,0,666},
            true
    );

}
