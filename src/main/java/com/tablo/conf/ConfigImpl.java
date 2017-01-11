package com.tablo.conf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 22/06/16.
 */
public class ConfigImpl implements Config{
    private String url;
    private List<StatusMapping> mappings;
    private String resultRegex;
    private int pollingIntervalMillis;
    private int led;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setResultRegex(String resultRegex) {
        this.resultRegex = resultRegex;
    }

    public void setPollingIntervalMillis(int pollingIntervalMillis) {
        this.pollingIntervalMillis = pollingIntervalMillis;
    }

    public void setled(int led) {
        this.led = led;
    }

    public void setMappings(List<StatusMapping> mappings) {
        this.mappings = mappings;
    }

    @Override
    public String URL() {
        return url;
    }

    @Override
    public String resultRegex() {
        return resultRegex;
    }

    @Override
    public int pollingIntervalMillis() {
        return pollingIntervalMillis;
    }

    @Override
    public List<StatusMapping> mappings() {
        return mappings;
    }

    @Override
    public int led() {
        return led;
    }

    @Override
    public String toString() {
        return "Url: " + url + "regex: " + resultRegex;
    }
}
