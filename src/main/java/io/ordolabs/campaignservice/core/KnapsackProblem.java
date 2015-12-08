package io.ordolabs.campaignservice.core;


public class KnapsackProblem{

    final long limit;
    final int itemCount;
    final long[] itemCost;
    final long[] itemValue;

    public KnapsackProblem(long limit, long[] itemCost, long[] itemValue){
        this.limit = limit;
        this.itemCount = itemCost.length;
        this.itemCost = itemCost;
        this.itemValue = itemValue;
    }

}
