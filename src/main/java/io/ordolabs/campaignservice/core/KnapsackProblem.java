package io.ordolabs.campaignservice.core;


public class KnapsackProblem{

    final long maxCost;
    final int itemCount;
    final long[] itemCost;
    final long[] itemValue;

    public KnapsackProblem(long maxCost, long[] itemCost, long[] itemValue){
        this.maxCost = maxCost;
        this.itemCount = itemCost.length;
        this.itemCost = itemCost;
        this.itemValue = itemValue;
    }

}
