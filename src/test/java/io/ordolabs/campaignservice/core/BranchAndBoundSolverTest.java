package io.ordolabs.campaignservice.core;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BranchAndBoundSolverTest {

    @Test
    public void testSimpleCase() {
        KnapsackProblem p = new KnapsackProblem(50, new long[]{10, 10, 10}, new long[]{1, 2, 3});
        BranchAndBoundSolver solver = new BranchAndBoundSolver(p);
        assertEquals(true, solver.canSolve());
        KnapsackSolution solution = solver.solve();

        assertArrayEquals(new long[]{0, 0, 5}, solution.getItemCount());
    }

    @Test
    public void testSampleCase1() {

        BranchAndBoundSolver solver = new BranchAndBoundSolver(ProblemFixtures.problem1);
        assertEquals(true, solver.canSolve());
        KnapsackSolution solution = solver.solve();
        assertArrayEquals(SolutionFixtures.solution1.getItemCount(), solution.getItemCount());
    }

    @Test
    public void testSampleCase2TimeBoxed() {
        if(System.getenv().containsKey("JUNIT_SLOW_TEST") && System.getenv().get("JUNIT_SLOW_TEST").equals("1")) {

            BranchAndBoundSolver solver = new BranchAndBoundSolver(ProblemFixtures.problem2, 20);
            assertEquals(true, solver.canSolve());
            KnapsackSolution solution = solver.solve();
            assertArrayEquals(SolutionFixtures.solution2.getItemCount(), solution.getItemCount());
        }
    }

    @Test
    public void testSampleCase2Unbound(){
        if(System.getenv().containsKey("JUNIT_SLOW_TEST") && System.getenv().get("JUNIT_SLOW_TEST").equals("1")) {

            BranchAndBoundSolver solver = new BranchAndBoundSolver(ProblemFixtures.problem2);
            assertEquals(true, solver.canSolve());
            KnapsackSolution solution = solver.solve();
            assertArrayEquals(SolutionFixtures.solution2.getItemCount(), solution.getItemCount());
        }
    }

    @Test
    public void testSampleCase3() {

        BranchAndBoundSolver solver = new BranchAndBoundSolver(ProblemFixtures.problem3);
        assertEquals(true, solver.canSolve());
        KnapsackSolution solution = solver.solve();
        assertArrayEquals(SolutionFixtures.solution3.getItemCount(), solution.getItemCount());
    }
}