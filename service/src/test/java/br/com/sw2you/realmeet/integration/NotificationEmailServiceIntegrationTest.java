package br.com.sw2you.realmeet.integration;

import br.com.sw2you.realmeet.config.properties.EmailConfigProperties;
import br.com.sw2you.realmeet.config.properties.TemplateConfigProperties;
import br.com.sw2you.realmeet.core.BaseIntegrationTest;
import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.email.EmailSender;
import br.com.sw2you.realmeet.email.TemplateType;
import br.com.sw2you.realmeet.service.NotificationEmailService;
import br.com.sw2you.realmeet.util.Constants;
import br.com.sw2you.realmeet.utils.TestDataCreator;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class NotificationEmailServiceIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private NotificationEmailService notificationEmailService;

    @MockBean
    private EmailSender emailSender;

    @Autowired
    private TemplateConfigProperties templateConfigProperties;

    @Autowired
    private EmailConfigProperties emailConfigProperties;

    private Allocation allocation;

    @Override
    protected void setupEach() throws Exception {
        allocation = TestDataCreator.newAllocationBuilderDefault().build();
    }

    @Test
    void testNotifyAllocationCreated() {
        notificationEmailService.notifyAllocationCreated(allocation);
        testInteraction(TemplateType.ALLOCATION_CREATED);
    }

    @Test
    void testNotifyAllocationUpdated() {
        notificationEmailService.notifyAllocationUpdated(allocation);
        testInteraction(TemplateType.ALLOCATION_UPDATED);
    }

    @Test
    void testNotifyAllocationDeleted() {
        notificationEmailService.notifyAllocationDeleted(allocation);
        testInteraction(TemplateType.ALLOCATION_DELETED);
    }

    private void testInteraction(TemplateType templateType) {
        var emailTemplate = templateConfigProperties.getEmailTemplate(templateType);
        Mockito
            .verify(emailSender)
            .send(
                ArgumentMatchers.argThat(
                    emailInfo ->
                        emailInfo.getSubject().equals(emailTemplate.getSubject()) &&
                        emailInfo.getTemplate().equals(emailTemplate.getTemplateName()) &&
                        emailInfo.getFrom().equals(emailConfigProperties.getFrom()) &&
                        emailInfo.getTo().get(0).equals(allocation.getEmployee().getEmail()) &&
                        emailInfo.getTemplateData().get(Constants.ALLOCATION).equals(allocation)
                )
            );
    }
}
