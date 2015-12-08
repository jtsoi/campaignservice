package io.ordolabs.campaignservice.api;

import org.junit.Test;
import static io.dropwizard.testing.FixtureHelpers.*;
import static org.junit.Assert.*;
import io.dropwizard.jackson.Jackson;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ProblemTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void deserializeJSON1() throws Exception {
        Problem problem = MAPPER.readValue(fixture("fixtures/problem_1.json"), Problem.class);
        assertEquals(10, problem.getMaxImpressions());
        assertEquals(0, problem.getOffers().size());
    }

    @Test
    public void deserializeJSON2() throws Exception {
        Problem problem = MAPPER.readValue(fixture("fixtures/problem_2.json"), Problem.class);
        assertEquals(20, problem.getMaxImpressions());
        assertEquals(1, problem.getOffers().size());
        Offer offer = problem.getOffers().get(0);
        assertEquals("foo", offer.getCustomer());
        assertEquals(3, offer.getImpressions());
        assertEquals(30, offer.getRevenue());
    }
}

