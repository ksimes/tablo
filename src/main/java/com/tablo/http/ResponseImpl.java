package com.tablo.http;

import com.tablo.conf.Config;

/**
 * Created by alec on 22/06/16.
 */
public class ResponseImpl implements Response {
    private final Config config;
    private final String response;

    public ResponseImpl (final Config c, final String r) {
        config = c;
        response = r;
    }

    @Override
    public Config config() {
        return config;
    }

    @Override
    public String response() {
        return response;
    }
}
