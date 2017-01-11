package com.tablo.conf;

import java.util.List;

/**
 * Created by alec on 22/06/16.
 */
public interface Config {
    String URL();
    String resultRegex();
    int pollingIntervalMillis();
    int led();
    List<StatusMapping> mappings();
}
