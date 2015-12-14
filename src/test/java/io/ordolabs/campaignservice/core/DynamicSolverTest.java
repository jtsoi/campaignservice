package io.ordolabs.campaignservice.core;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DynamicSolverTest {

    @Test
    public void testGCD() {
        int[] a = {36, 36, 12, 1000000, 50000000, 32356000};
        int[] b = {10, 11, 36,   10000, 49000000,  3500000};
        int[] g = { 2, 1,  12,   10000,  1000000,     4000};

        for(int i = 0; i < a.length; i++){
            assertEquals(g[i], DynamicSolver.gcd(a[i], b[i]));
            assertEquals(g[i], DynamicSolver.gcd(b[i], a[i]));
        }
    }

    @Test
    public void testSimpleCase() {
        KnapsackProblem p = new KnapsackProblem(50, new long[]{10, 10, 10}, new long[]{1, 2, 3});
        DynamicSolver solver = new DynamicSolver(p);
        assertEquals(true, solver.canSolve());
        KnapsackSolution solution = solver.solve();

        assertArrayEquals(new long[]{0, 0, 5}, solution.getItemCount());
    }

    @Test
    public void testSampleCase1() {

        DynamicSolver solver = new DynamicSolver(ProblemFixtures.problem1);
        assertEquals(true, solver.canSolve());
        KnapsackSolution solution = solver.solve();
        assertArrayEquals(SolutionFixtures.solution1.getItemCount(), solution.getItemCount());
    }

    @Test
    public void testSampleCase2() {
        DynamicSolver solver = new DynamicSolver(ProblemFixtures.problem2);
        assertEquals(true, solver.canSolve());
        KnapsackSolution solution = solver.solve();
        assertArrayEquals(SolutionFixtures.solution2.getItemCount(), solution.getItemCount());
    }

    @Test
    public void testSampleCase3() {

        DynamicSolver solver = new DynamicSolver(ProblemFixtures.problem3);
        assertEquals(true, solver.canSolve());
        KnapsackSolution solution = solver.solve();
        assertArrayEquals(SolutionFixtures.solution3.getItemCount(), solution.getItemCount());
    }
}