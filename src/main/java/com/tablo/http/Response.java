package com.tablo.http;

import com.tablo.conf.Config;

/**
 * Created by alec on 22/06/16.
 */
public interface Response {
    Config config();
    String response();
}
