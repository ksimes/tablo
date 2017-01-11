package com.tablo.integration;

import com.tablo.conf.Config;
import com.tablo.conf.StatusMapping;
import com.tablo.regex.IRegexProcessor;

import java.util.List;

/**
 * Created by tyordanov on 22/06/16.
 */
public class MessageHandler implements IMessageHandler {

    private IRegexProcessor regexProcessor;
    private String serial;

    public MessageHandler(IRegexProcessor regexProcessor) {
        this.regexProcessor = regexProcessor;
        generateSerialString();
    }

    @Override
    public MessageHandler generateSerialString() {
        String serialProtocolStr = "{%%SETSTATE ";
        StatusMapping.Outcome outcome = regexProcessor.outcome();
        Config config = regexProcessor.response().config();
        int ledToDisplay = config.led();

        serialProtocolStr += ledToDisplay + " " + outcome.name();
        serial = serialProtocolStr + " }";
        return this;
    }

    @Override
    public String getSerial() {
        return serial;
    }

}
