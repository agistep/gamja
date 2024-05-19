package io.agistep.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CustomConfigTest {

    // TODO : gamja.core 는 기본적인 설정을 gamja-default.yml 에서 로드함


    // TODO : 사용자정의 설정파일


    // TODO : base pacakge가 설정되지 않으면 어떤 규칙에 따라 base package를 defualt로 설정한다. (like spring)


    @Test
    @DisplayName("사용자 정의 설정 ")
    void customConfig() {
        // 나는 커스텀 설정을 classpath:/foo.yml

        var resource = GamjaConfig.class.getResource("/foo.xml");
        assertThat(resource).isNull();

        assertThatThrownBy(() -> GamjaConfig.get("core.basePackage")); // todo: exception type test
    }

    @Test
    @DisplayName("사용자 정의 설정 ")
    void aaaaa() {
        // 나는 커스텀 설정을 classpath:/bar.yml

        var resource = GamjaConfig.class.getResource("/bar.xml");
        assertThat(resource).isNull();

        assertThatThrownBy(() -> GamjaConfig.get("core.basePackage")); // todo: exception type test
    }

}
