package io.agistep.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultConfigLoadTest {

    // TODO 기본값들 확인
// 리소스에 있던 없던 무조건 기본 설정을 불러온다.
    //

    @Test
    @DisplayName("기본 설정 확인 > 설정이 존재할 경우")
    void defaultConfig() {
        // custom 이라는 게 변수이지만, 뒤에 추가정보가 있으면 객체.

        assertThat(GamjaConfig.get("custom.config.path")).isEqualTo("gamja.yml");
        assertThat(GamjaConfig.get("core.basePackage")).isEqualTo("io.agistep");

    }

    @Test
    @DisplayName("기본 설정 확인 > 설정이 존재하지 않은 경우")
    void notFoundDefaultConfig() {
        assertThat(GamjaConfig.get("custom.foo")).isNull();
    }


    public static class Foo {
        String abc;

        public Foo(String abc) {
            this.abc = abc;
        }
    }

    @Test
    void name() {
        Foo foo = new Foo("zz");

        //TODO homework
    }
}
