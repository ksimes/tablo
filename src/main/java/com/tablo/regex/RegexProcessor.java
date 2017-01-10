package com.tablo.regex;

import com.tablo.conf.StatusMapping;
import com.tablo.http.Response;

import static com.tablo.conf.StatusMapping.Outcome;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tyordanov on 22/06/16.
 */
public class RegexProcessor implements IRegexProcessor {

    final int REGEX_GROUP_RESULT = 2;
    final String KEYWORD_FAIL = "FAILURE";
    final String KEYWORD_WARN = "WARNING";
    final String KEYWORD_PASS = "SUCCESS";
    Outcome outcome;
    Response response;

    public RegexProcessor(Response response) {
        this.response = response;
        processStatusResponse();
    }


    @Override
    public IRegexProcessor processStatusResponse() {
        return matchResponseStringWithConfigRegex();
    }

    @Override
    public Outcome outcome() {
        return outcome;
    }

    @Override
    public Response response() {
        return response;
    }


    private IRegexProcessor matchResponseStringWithConfigRegex() {
        String responseStr = response.response();
        final String resultRegex = response.config().resultRegex();
        Pattern pattern = Pattern.compile(resultRegex);
        Matcher matcher = pattern.matcher(responseStr);
        String matchResult = safeFindMatch(matcher);
        outcome = findStatus(matchResult, response.config().mappings());
        return this;

    }

    private Outcome findStatus(String matchResult, List<StatusMapping> mappings) {
        for (StatusMapping statusMapping : mappings) {
            for (String matchStr : statusMapping.match()) {
                if (matchResult.contains(matchStr)) {
                    return statusMapping.outcome();
                }
            }
        }
        return Outcome.FAILURE;
    }


    private String safeFindMatch(Matcher matcher) {
        String result = "";
        if (matcher.find()) {
            try {
                result = matcher.group(REGEX_GROUP_RESULT);
            } catch (Throwable ex) {
                // ex.printStackTrace();
            } finally {
                // System.out.println(result);
                return result;
            }
        }
        System.out.println(result);
        return result;
    }
}
