package com.tablo.conf;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by alec on 22/06/16.
 */
public class ConfigParser {
    /**
     * The <code>Logger</code> to be used.
     */
    private static Logger log = Logger.getLogger(ConfigParser.class);
    private String filePath;

    public ConfigParser(String configFile) {
        filePath = configFile;
    }

    public List<Config> parseFile () {
        InputStream inputStream = null;

        try {
            // **NOTE** Has to be 'this.getClass' or you will get all sorts of problems finding file.
            inputStream = this.getClass().getResourceAsStream("/" + filePath);

            if (inputStream == null) {
                File file = new File("./" + filePath);
                inputStream = new FileInputStream(file);
            }

        } catch (IOException ioException) {
            log.error("Unable to find or open comments file", ioException);
        }

        ObjectMapper mapper = new ObjectMapper();
        List<Config> config = null;
        try {
            config = mapper.readValue(inputStream, new TypeReference<List<ConfigImpl>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;
    }
}
