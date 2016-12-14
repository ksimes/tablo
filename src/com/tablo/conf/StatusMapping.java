package com.tablo.conf;

import java.util.List;

/**
 * Created by alec on 22/06/16.
 */
public class StatusMapping {
    public enum Outcome {
        SUCCESS, WARNING, FAILURE;
    }

    public StatusMapping(){}

    public void setPin(int pin) {
        this.pin = pin;
    }

    public void setMatch(List<String> match) {
        this.match = match;
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public StatusMapping(int pin, List<String> match, Outcome outcome) {
        this.pin = pin;
        this.match = match;
        this.outcome = outcome;
    }

    public int pin() {
        return pin;
    }

    public List<String> match() {
        return match;
    }

    public Outcome outcome() {
        return outcome;
    }

    private int pin;
    private List<String> match;
    private Outcome outcome;
}
