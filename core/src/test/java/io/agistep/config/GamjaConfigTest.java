package io.agistep.config;

import org.apache.commons.lang3.ClassPathUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class GamjaConfigTest {

    @Test
    void loadConfig() {
        // TODO 설정을 어디에서 찾는가?
        // TODO 설정이 없으면 어떻게 동작가?
        // TODO Refact

        // GamjaConfig 는 왜 어디서 사용되는가?

        assertThat(GamjaConfig.get("core.basePackage")).isEqualTo("io.agistep");

    }

    @Test
    void classLoaderTest() {
//        URL resource = GamjaConfigTest.class.getClass("test.yml");

        Yaml yaml = new Yaml(new Constructor(GamjaConfigProperties.class, new LoaderOptions()));

        InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream("test.yml");
        Object load = yaml.load(systemResourceAsStream);
        assertThat(load).isNotNull();
    }
}
