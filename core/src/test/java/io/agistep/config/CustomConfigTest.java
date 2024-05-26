package io.agistep.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class CustomConfigTest {

    // TODO : gamja.core 는 기본적인 설정을 gamja-default.yml 에서 로드함


    // TODO : 사용자정의 설정파일


    // TODO : base pacakge가 설정되지 않으면 어떤 규칙에 따라 base package를 defualt로 설정한다. (like spring)


    @Test
    @DisplayName("사용자 정의 설정 파일이 존재하지 않는 경우에는 에러를 발생한다.")
    void customConfig() {
        // 나는 커스텀 설정을 classpath:/gamja.yml 가 싫고 foo 로 등록해놓고 resource에 foo를 등록하지 않은 케이스에 에러 발생
        //config path 등록 메커니즘이 존재한다고 가정
        System.setProperty("custom.config.path", "/foo.yml");

        assertThatThrownBy(() -> GamjaConfig.get("core.basePackage"))
                .isInstanceOf(ExceptionInInitializerError.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("사용자 정의 설정 ")
    void aaaaa() {
        System.setProperty("custom.config.path", "/bar.yml");

        assertThat(GamjaConfig.get("core.basePackage")).isEqualTo("bar");
    }

}
