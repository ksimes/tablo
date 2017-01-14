package com.tablo.http;

import com.tablo.conf.Config;
import com.tablo.integration.IMessageHandler;
import com.tablo.integration.MessageHandler;
import com.tablo.conf.ConfigParser;
import com.tablo.regex.IRegexProcessor;
import com.tablo.regex.RegexProcessor;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by alec on 22/06/16.
 */
public class HTTPClient {
    private ResteasyClient client;
    public HTTPClient() {
        client = new ResteasyClientBuilder().build();
    }

    public Response get(final Config config) {
        ResteasyWebTarget target = client.target(config.URL());
        javax.ws.rs.core.Response response = target.request(MediaType.APPLICATION_JSON).get();
        String value = response.toString();
        response.close();
//        return value;
//        String response = client
//                .target(config.URL())
//                .request(MediaType.APPLICATION_JSON)
//                .get(String.class);
        return new ResponseImpl(config, value);
    }

    public static void main (String[] args) {
        HTTPClient a = new HTTPClient();
        List<Config> c = new ConfigParser("tabloJenkins.json").parseFile();
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
