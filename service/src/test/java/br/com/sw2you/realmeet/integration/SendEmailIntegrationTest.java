package br.com.sw2you.realmeet.integration;

import br.com.sw2you.realmeet.core.BaseIntegrationTest;
import br.com.sw2you.realmeet.email.EmailSender;
import br.com.sw2you.realmeet.email.model.EmailInfo;
import br.com.sw2you.realmeet.utils.TestUtils;
import java.util.List;
import java.util.Map;
import javax.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

public class SendEmailIntegrationTest extends BaseIntegrationTest {
    private static final String FROM_EMAIL_ADDRESS = "email@gmail.com";
    private static final String TO_EMAIL_ADDRESS = "email@hotmail.com";
    private static final String SUBJECT = "Envio de Email com Java";
    private static final String EMAIL_TEMPLATE = "template-test.html";

    @Autowired
    private EmailSender victim;

    @MockBean
    private JavaMailSender javaMailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Test
    void testSendEmail() {
        Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        var emailInfo = EmailInfo
            .newEmailInfoBuilder()
            .from(FROM_EMAIL_ADDRESS)
            .to(List.of(TO_EMAIL_ADDRESS))
            .subject(SUBJECT)
            .template(EMAIL_TEMPLATE)
            .templateData(Map.of("param", "Testando o envio de email usando 'Java Mail'"))
            .build();

        victim.send(emailInfo);
        TestUtils.sleep(2000);
        Mockito.verify(javaMailSender).send(ArgumentMatchers.eq((mimeMessage)));
    }
}
