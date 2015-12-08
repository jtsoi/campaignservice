package io.ordolabs.campaignservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

public class Offer {

    @JsonProperty
    private String customer;
    @JsonProperty
    private long impressions;
    @JsonProperty
    private long revenue;

    @JsonCreator
    public Offer(
        @JsonProperty("customer") String customer,
        @JsonProperty("impressions") long impressions,
        @JsonProperty("revenue") long revenue
    ) {
        this.customer = customer;
        this.impressions = impressions;
        this.revenue = revenue;
    }

    public String getCustomer() {
        return customer;
    }

    public long getImpressions() {
        return impressions;
    }

    public long getRevenue() {
        return revenue;
    }

    public boolean notZero(){
        return impressions > 0 && revenue > 0;
    }
}
