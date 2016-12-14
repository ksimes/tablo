package com.tablo.com.tablo.main;

import com.tablo.conf.Config;
import com.tablo.conf.ConfigParser;
import com.tablo.http.HTTPClient;
import com.tablo.http.Response;
import com.tablo.integration.IMessageHandler;
import com.tablo.integration.MessageHandler;
import com.tablo.regex.IRegexProcessor;
import com.tablo.regex.RegexProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by alec on 22/06/16.
 */
public class Tablo {
    public static void main (String[] args) {
        String configFile = "resources/tabloJenkins.json";
        List<Config> c = new ConfigParser(configFile).parseFile();

        HTTPClient a = new HTTPClient();
        while (true) {
            for (Config config : c) {
                Response r = a.get(config);
                // System.out.println(r.response());
                // System.out.println("Parsing...");
                IRegexProcessor regexP = new RegexProcessor(r);
                System.out.println(regexP.outcome());
                IMessageHandler messageHandler = new MessageHandler(regexP);
                System.out.println(messageHandler.getSerial());
            }
            try {
                TimeUnit.SECONDS.sleep(2L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
