package io.ordolabs.campaignservice.core;

public class ProblemFixtures {

    public static KnapsackProblem problem1 = new KnapsackProblem(
            32356000,
            new long[]{2000000,3500000,2300000,8000000,10000000,1500000,1000000},
            new long[]{200,400,210,730,1000,160,100}
    );
    public static KnapsackProblem problem2 = new KnapsackProblem(
            50000000,
            new long[]{2,3,70000,49000000},
            new long[]{2,2,71000,50000000}
    );
    public static KnapsackProblem problem3 = new KnapsackProblem(
            2000000000,
            new long[]{1000000,2000000,3000000},
            new long[]{5000,9000,20000}
    );

}
