package com.tablo.http;

import com.tablo.conf.Config;
import com.tablo.integration.IMessageHandler;
import com.tablo.integration.MessageHandler;
import com.tablo.conf.ConfigParser;
import com.tablo.regex.IRegexProcessor;
import com.tablo.regex.RegexProcessor;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by alec on 22/06/16.
 */
public class HTTPClient {
    private Client client;
    public HTTPClient() {
        client = ClientBuilder.newClient();
    }

    public Response get(final Config config) {
        String response = client
                .target(config.URL())
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        return new ResponseImpl(config, response);
    }

    public static void main (String[] args) {
        HTTPClient a = new HTTPClient();
        List<Config> c = new ConfigParser("resources/tabloJenkins.json").parseFile();
        for (Config config : c) {
            Response r = a.get(config);
            System.out.println(r.response());
            System.out.println("Parsing...");
            IRegexProcessor regexP = new RegexProcessor(r);
            System.out.println(regexP.outcome());
            IMessageHandler messageHandler = new MessageHandler(regexP);
            System.out.println(messageHandler.getSerial());
        }
    }
}
