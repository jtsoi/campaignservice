package io.ordolabs.campaignservice;

import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import io.ordolabs.campaignservice.resources.ProblemResource;
import io.ordolabs.campaignservice.resources.SolutionResource;

public class CampaignServiceApplication extends Application<CampaignServiceConfiguration> {
    public static void main(String[] args) throws Exception {
        new CampaignServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "campaignservice";
    }

    @Override
    public void initialize(Bootstrap<CampaignServiceConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(CampaignServiceConfiguration configuration, Environment environment) {
        SolutionResource sr = new SolutionResource();
        SolutionManager sm = new SolutionManager(sr);
        ProblemResource pr = new ProblemResource(sm);
        environment.lifecycle().manage(sm);
        environment.jersey().register(pr);
        environment.jersey().register(sr);

        environment.getObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

}
