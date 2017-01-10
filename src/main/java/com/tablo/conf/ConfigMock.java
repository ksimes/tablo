package com.tablo.conf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alec on 22/06/16.
 */
public class ConfigMock implements Config {

    @Override
    public String URL() {
        return "http://192.168.0.20:8080/job/Fail/lastBuild/api/json";
    }

    @Override
    public String resultRegex() {
        return "(.*)(\\\"result\\\"\\s*:\\s*\\\"([A-Za-z0-9]+)\\\")(.*)";
    }

    @Override
    public int pollingIntervalMillis() {
        return 0;
    }

    @Override
    public List<StatusMapping> mappings() {
        List<StatusMapping> statusMappings = new ArrayList<>();
        List<String> passMatches = new ArrayList<String>();
        passMatches.add("SUCCESS");

        StatusMapping passMapping = new StatusMapping(1,passMatches, StatusMapping.Outcome.SUCCESS);
        List<String> failMatches = new ArrayList<>();
        failMatches.add("FAILURE");
        StatusMapping failMapping = new StatusMapping(2,failMatches, StatusMapping.Outcome.FAILURE);

        List<String> warnMatches = new ArrayList<>();
        warnMatches.add("WARN");
        warnMatches.add("WARNING");

        StatusMapping warnMapping = new StatusMapping(3,warnMatches, StatusMapping.Outcome.FAILURE);



        statusMappings.add(passMapping);
        statusMappings.add(failMapping);
        statusMappings.add(warnMapping);
        return statusMappings;
    }
}
