package io.ordolabs.campaignservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Campaign {

    @JsonProperty
    private String customer;
    @JsonProperty
    private long campaignsSold;
    @JsonProperty
    private long totalImpressions;
    @JsonProperty
    private long totalRevenue;

    public Campaign(Offer offer, long campaignsSold) {
        this.customer = offer.getCustomer();
        this.campaignsSold = campaignsSold;
        this.totalImpressions = offer.getImpressions() * campaignsSold;
        this.totalRevenue = offer.getRevenue() * campaignsSold;
    }

    public String getCustomer() {
        return customer;
    }

    public long getTotalImpressions() {
        return totalImpressions;
    }

    public long getCampaignsSold() {
        return campaignsSold;
    }

    public long getTotalRevenue() {
        return totalRevenue;
    }
}
