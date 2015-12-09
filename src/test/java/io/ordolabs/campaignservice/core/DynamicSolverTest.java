package io.ordolabs.campaignservice.core;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DynamicSolverTest {

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