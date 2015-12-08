package io.ordolabs.campaignservice.api;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.ordolabs.campaignservice.core.KnapsackSolution;

public class Solution {

    @JsonProperty
    private long id;
    @JsonProperty
    private List<Campaign> campaigns;
    @JsonProperty
    private long totalImpressions;
    @JsonProperty
    private long totalRevenue;
    @JsonProperty
    private long problemId;

    /**
     * Creates a Solution based on a KnapsackSolution & Problem.
     * The for loop is needed to "fill" the Offers that were filtered due to having
     * zero revenue or impressions.
     */
    public static Solution fromKnapsackSolution(Problem problem, KnapsackSolution solution){
        ArrayList<Offer> offers = (ArrayList<Offer>) problem.getOffers();
        ArrayList<Campaign> campaigns = new ArrayList<>();
        long[] itemCount = solution.getItemCount();

        for(int itemIdx=0, offerIdx=0; offerIdx < offers.size();){
            Offer offer = offers.get(offerIdx);
            if(offer.notZero()){
                campaigns.add(new Campaign(offer, itemCount[itemIdx]));
                itemIdx++;
            }else{
                campaigns.add(new Campaign(offer, 0));
            }
            offerIdx++;
        }

        return new Solution(problem, campaigns);
    }

    public Solution(Problem problem, List<Campaign> campaigns){
        this.campaigns = campaigns;
        this.problemId = problem.getId();
        this.totalImpressions = campaigns.stream().mapToLong(Campaign::getTotalImpressions).sum();
        this.totalRevenue = campaigns.stream().mapToLong(Campaign::getTotalRevenue).sum();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Campaign> getCampaigns(){
        return campaigns;
    }

    public long getTotalImpressions() {
        return totalImpressions;
    }

    public long getTotalRevenue() {
        return totalRevenue;
    }

    public long getProblemId() {
        return problemId;
    }

}
