package io.agistep.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class GamjaConfigTest {
    // todo: base pacakge가 설정되지 않으면 어떤 규칙에 따라 base package를 defualt로 설정한다. (like spring)
    // todo: gamja.yml의 파일 위치를 overriding할수있게하기


    @Test
    @DisplayName("gamja.yml이 없으면 예외를 던진다 ")
    void throwExceptionWhenNoConfig() {
        var resource = GamjaConfig.class.getResource(GamjaConfig.DEFAULT_CONFIG_CLASSPATH);
        assertThat(resource).isNull();
        assertThatThrownBy(() -> GamjaConfig.get("core.basePackage")); // todo: exception type test
    }


    @Test
    @DisplayName("classpath 최상위에서 gamja.yml을 찾게 한다 (classpath://gamja.yml)")
    void loadConfig() {

        var resource = GamjaConfig.class.getResource(GamjaConfig.DEFAULT_CONFIG_CLASSPATH);
        assertThat(resource).isNotNull();

        assertThat(GamjaConfig.get("core.basePackage")).isEqualTo("io.agistep");

    }

    @Test
    void name() {
        // todo: Stream closed error 원인 찾기..

        Yaml yaml = new Yaml(new Constructor(GamjaConfigProperties.class, new LoaderOptions()));

        InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream("/gamja.yml");
        yaml.load(systemResourceAsStream);
    }
}
