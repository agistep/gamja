package io.agistep.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

import static io.agistep.config.GamjaConfig.GamjaConfigLoader.loadConfig;

public class GamjaConfig {
    static final String DEFAULT_CONFIG_CLASSPATH = "/gamja.yml";
    private final static Logger log = LoggerFactory.getLogger("GamjaConfig");

    private static GamjaConfigProperties properties;

    static {
        properties = loadConfig(DEFAULT_CONFIG_CLASSPATH);
    }

    public static GamjaConfigProperties getProperties() {
        return properties;
    }

    public static String get(String s) {

        if (properties == null) {
            throw new RuntimeException(); // todo: 구체적인 exception class
        }

        if("core.basePackage".equals(s)) {
            return properties.getBasePackage();
        }

        return null;
    }

    class GamjaConfigLoader {

        static GamjaConfigProperties loadConfig(String filePath) {
            Yaml yaml = new Yaml(new Constructor(GamjaConfigProperties.class, new LoaderOptions()));

            InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream(filePath);
            return yaml.load(systemResourceAsStream);
        }
    }
}
