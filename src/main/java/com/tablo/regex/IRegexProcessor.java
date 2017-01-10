package com.tablo.regex;

import com.tablo.conf.StatusMapping;
import com.tablo.http.Response;

/**
 * Created by tyordanov on 22/06/16.
 */
public interface IRegexProcessor {
    public IRegexProcessor processStatusResponse();
    public StatusMapping.Outcome outcome();
    public Response response();
}
