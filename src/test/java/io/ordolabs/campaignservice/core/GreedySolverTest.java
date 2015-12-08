package io.ordolabs.campaignservice.core;

import static org.junit.Assert.*;
import org.junit.Test;

public class GreedySolverTest {

    @Test
    public void testSimpleCase() {
        KnapsackProblem p = new KnapsackProblem(50, new long[]{10, 10, 10}, new long[]{1, 2, 3});
        GreedySolver solver = new GreedySolver(p);
        KnapsackSolution solution = solver.solve();

        assertArrayEquals(new Integer[]{2, 1, 0}, solver.sortedByUtility);
        assertEquals(15, solution.getPackedValue());
        assertArrayEquals(new long[]{0, 0, 5}, solution.getItemCount());
    }
}