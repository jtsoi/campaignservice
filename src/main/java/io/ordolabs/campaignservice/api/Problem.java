package io.ordolabs.campaignservice.api;

import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.ordolabs.campaignservice.core.KnapsackProblem;

public class Problem {

    private long id;
    private long maxImpressions;
    private List<Offer> offers;
    private long solutionId;
    private boolean optimalSolution;

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty
    public List<Offer> getOffers(){
        return offers;
    }

    @JsonProperty
    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    @JsonProperty
    public long getMaxImpressions() {
        return maxImpressions;
    }

    @JsonProperty
    public void setMaxImpressions(long maxImpressions) {
        this.maxImpressions = maxImpressions;
    }

    @JsonProperty
    public long getSolutionId() {
        return solutionId;
    }

    @JsonProperty
    public void setSolutionId(long solutionId) {
        this.solutionId = solutionId;
    }

    @JsonProperty
    public boolean isOptimalSolution() {
        return optimalSolution;
    }

    @JsonProperty
    public void setOptimalSolution(boolean optimalSolution) {
        this.optimalSolution = optimalSolution;
    }

    /**
     * Create a KnapsackProblem, but filter offers that add no value.
     */
    @JsonIgnore
    public KnapsackProblem getKnapsackProblem(){
        long[] itemCost = getOffers().stream().filter(Offer::notZero).mapToLong(Offer::getImpressions).toArray();
        long[] itemValue = getOffers().stream().filter(Offer::notZero).mapToLong(Offer::getRevenue).toArray();
        return new KnapsackProblem(maxImpressions, itemCost, itemValue);
    }

}
