package br.com.sw2you.realmeet.config;

import br.com.sw2you.realmeet.config.properties.EmailConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfiguration {
    private final EmailConfigProperties emailConfigProperties;

    public EmailConfiguration(EmailConfigProperties emailConfigProperties) {
        this.emailConfigProperties = emailConfigProperties;
    }

    @Bean
    public JavaMailSender mailSender() {
        var mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailConfigProperties.getHost());
        mailSender.setPort(
            Integer.parseInt(emailConfigProperties.getProperty(EmailConfigProperties.PROPERTY_SMTP_PORT))
        );
        mailSender.setUsername(emailConfigProperties.getUsername());
        mailSender.setPassword(emailConfigProperties.getPassword());

        var properties = mailSender.getJavaMailProperties();
        properties.put(
            EmailConfigProperties.PROPERTY_TRANSPORT_PROTOCOL,
            emailConfigProperties.getProperty(EmailConfigProperties.PROPERTY_TRANSPORT_PROTOCOL)
        );
        properties.put(
            EmailConfigProperties.PROPERTY_SMTP_AUTH,
            emailConfigProperties.getProperty(EmailConfigProperties.PROPERTY_SMTP_AUTH)
        );
        properties.put(
            EmailConfigProperties.PROPERTY_SMTP_STARTTLS_ENABLE,
            emailConfigProperties.getProperty(EmailConfigProperties.PROPERTY_SMTP_STARTTLS_ENABLE)
        );

        return mailSender;
    }
}
