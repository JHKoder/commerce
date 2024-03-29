package github.jhkoder.commerce.config;

import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Jasypt")
public class JasyptConfigTest {
    private final String password = System.getenv("JASYPT_PASSWORD");

    public String jasypt(String code) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setPoolSize("1");
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setStringOutputType("base64");
        config.setKeyObtentionIterations("1000");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        encryptor.setConfig(config);
        return encryptor.decrypt(code);
    }


    @Test
    @DisplayName("환경 변수 설정이 되어 있는가")
    void is_the_jasypt_password_environment_variable_set() {
        Assertions.assertThatThrownBy(() -> jasypt("password"))
                .isNotInstanceOf(EncryptionOperationNotPossibleException.class);
    }
}
