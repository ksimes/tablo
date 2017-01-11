package com.tablo.conf;

import java.util.List;

/**
 * Created by alec on 22/06/16.
 */
public class StatusMapping {
    public enum Outcome {
        SUCCESS, WARNING, FAILURE;
    }

    private List<String> match;
    private Outcome outcome;

    public StatusMapping() {
    }

    public void setMatch(List<String> match) {
        this.match = match;
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public StatusMapping(List<String> match, Outcome outcome) {
        this.match = match;
        this.outcome = outcome;
    }

    public List<String> match() {
        return match;
    }

    public Outcome outcome() {
        return outcome;
    }
}
