package com.tablo.integration;

import com.tablo.conf.Config;
import com.tablo.conf.StatusMapping;
import com.tablo.regex.IRegexProcessor;

import java.util.List;

/**
 * Created by tyordanov on 22/06/16.
 */
public class MessageHandler implements IMessageHandler {

    IRegexProcessor regexProcessor;
    String serial;

    public MessageHandler(IRegexProcessor regexProcessor) {
        this.regexProcessor = regexProcessor;
        generateSerialString();
    }

    @Override
    public MessageHandler generateSerialString() {
        String serialProtocolStr = "%%PINS 1 2 3 S ";
        StatusMapping.Outcome outcome = regexProcessor.outcome();
        Config config = regexProcessor.response().config();
        List<StatusMapping> mappings = config.mappings();
        StatusMapping desiredMapping = null;
        for(StatusMapping mapping : mappings){
            if(mapping.outcome() == outcome){
                desiredMapping = mapping;
                break;
            }
        }
        int desiredPin = desiredMapping.pin();
        serialProtocolStr+=desiredPin;
        serial = serialProtocolStr;
        return this;
    }

    @Override
    public String getSerial() {
        return serial;
    }

}
