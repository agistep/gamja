package io.agistep.config;

import org.junit.jupiter.api.Test;

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
}
