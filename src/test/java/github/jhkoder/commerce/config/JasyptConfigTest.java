package github.jhkoder.commerce.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Jasypt")
public class JasyptConfigTest {
    private final String password = System.getenv("JASYPT_PASSWORD");


    @Test
    @DisplayName("환경 변수 설정이 되어 있는가")
    void is_the_jasypt_password_environment_variable_set() {
        assertThat(password).isNotNull();
    }

}
