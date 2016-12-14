package com.tablo.conf;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by alec on 22/06/16.
 */
public class ConfigParser {
    private String filePath;
    public ConfigParser(String configFile) {
        filePath = configFile;
    }

    public List<Config> parseFile () {
        File f = new File(filePath);
        ObjectMapper mapper = new ObjectMapper();
        List<Config> c = null;
        try {
            c = mapper.readValue(f, new TypeReference<List<ConfigImpl>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return c;
    }
}
