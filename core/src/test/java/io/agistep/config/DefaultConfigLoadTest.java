package io.agistep.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultConfigLoadTest {

    // TODO 기본값들 확인

    @Test
    @DisplayName("기본 설정 확인")
    void defaultConfing() {

        assertThat(GamjaConfig.get("custom.config.path")).isEqualTo("gamja.yml");
        assertThat(GamjaConfig.get("core.basePackage")).isEqualTo("io.agistep");

    }
}
