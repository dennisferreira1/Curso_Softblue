package br.com.sw2you.realmeet.unit;

import br.com.sw2you.realmeet.config.properties.EmailConfigProperties;
import br.com.sw2you.realmeet.core.BaseIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class EmailConfigPropertiesUnitTest extends BaseIntegrationTest {
    @Autowired
    private EmailConfigProperties emailConfigProperties;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String userName;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.from}")
    private String from;

    @Value("${spring.mail.properties.mail.transport.protocol}")
    private String protocol;

    @Value("${spring.mail.properties.mail.smtp.port}")
    private String port;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttlsEnable;

    @Test
    void testLoadConfigProperties() {
        Assertions.assertNotNull(host);
        Assertions.assertEquals(host, emailConfigProperties.getHost());

        Assertions.assertNotNull(userName);
        Assertions.assertEquals(userName, emailConfigProperties.getUsername());

        Assertions.assertNotNull(password);
        Assertions.assertEquals(password, emailConfigProperties.getPassword());

        Assertions.assertNotNull(from);
        Assertions.assertEquals(from, emailConfigProperties.getFrom());

        Assertions.assertNotNull(protocol);
        Assertions.assertEquals(
            protocol,
            emailConfigProperties.getProperty(EmailConfigProperties.PROPERTY_TRANSPORT_PROTOCOL)
        );

        Assertions.assertNotNull(port);
        Assertions.assertEquals(port, emailConfigProperties.getProperty(EmailConfigProperties.PROPERTY_SMTP_PORT));

        Assertions.assertNotNull(auth);
        Assertions.assertEquals(auth, emailConfigProperties.getProperty(EmailConfigProperties.PROPERTY_SMTP_AUTH));

        Assertions.assertNotNull(starttlsEnable);
        Assertions.assertEquals(
            starttlsEnable,
            emailConfigProperties.getProperty(EmailConfigProperties.PROPERTY_SMTP_STARTTLS_ENABLE)
        );
    }
}
