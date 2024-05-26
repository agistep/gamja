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

    // TODO : gamja.core 는 기본적인 설정을 gamja-default.yml 에서 로드함


    // TODO : 사용자정의 설정파일


    // TODO : base pacakge가 설정되지 않으면 어떤 규칙에 따라 base package를 defualt로 설정한다. (like spring)


    @Test
    @DisplayName("gamja.yml이 없으면 예외를 던진다 ")
    void throwExceptionWhenNoConfig() {
        // 나는 커스텀 설정을 classpath:/foo.yml
        // foo 인지 bar 인지 어떻게 알아?
        // Rule 등록 메카니즘
        var resource = GamjaConfig.class.getResource("/gamja.yml");
        assertThat(resource).isNull();

        assertThatThrownBy(() -> GamjaConfig.get("core.basePackage")); // todo: exception type test
    }

}
