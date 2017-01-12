package com.tablo;

import com.tablo.conf.Config;
import com.tablo.conf.ConfigParser;
import com.tablo.http.HTTPClient;
import com.tablo.http.Response;
import com.tablo.integration.IMessageHandler;
import com.tablo.integration.MessageHandler;
import com.tablo.regex.IRegexProcessor;
import com.tablo.regex.RegexProcessor;
import com.tablo.serial.SerialComms;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by alec on 22/06/16.
 */
public class Tablo {
    /**
     * The <code>Logger</code> to be used.
     */
    private static Logger log = Logger.getLogger(Tablo.class);
    private static SerialComms comms;

    public static void main(String[] args) {
        final String configFile = "tabloJenkins.json";
        List<Config> configuration = new ConfigParser(configFile).parseFile();

        HTTPClient a = new HTTPClient();
        try {
            comms = new SerialComms("/dev/ttyACM0");
            comms.startComms();

            while (true) {
                for (Config config : configuration) {
                    Response r = a.get(config);
                    IRegexProcessor regexP = new RegexProcessor(r);
                    log.info(regexP.outcome());
                    IMessageHandler messageHandler = new MessageHandler(regexP);

                    log.info(messageHandler.getSerial());
                    comms.write(messageHandler.getSerial() + "\n");
                }

                try {
                    TimeUnit.SECONDS.sleep(2L);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }
        } catch (InterruptedException e) {
            log.error(" ==>> PROBLEMS WITH SERIAL COMMUNICATIONS: " + e.getMessage(), e);
        }
    }
}
