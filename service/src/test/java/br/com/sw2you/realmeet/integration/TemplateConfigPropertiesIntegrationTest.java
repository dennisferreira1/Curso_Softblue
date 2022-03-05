package br.com.sw2you.realmeet.integration;

import br.com.sw2you.realmeet.config.properties.TemplateConfigProperties;
import br.com.sw2you.realmeet.core.BaseIntegrationTest;
import br.com.sw2you.realmeet.email.TemplateType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class TemplateConfigPropertiesIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private TemplateConfigProperties templateConfigProperties;

    @Value("${realmeet.email.templates.allocationCreated.subject}")
    private String allocationCreatedSubject;

    @Value("${realmeet.email.templates.allocationCreated.templateName}")
    private String allocationCreatedTemplateName;

    @Value("${realmeet.email.templates.allocationUpdated.subject}")
    private String allocationUpdatedSubject;

    @Value("${realmeet.email.templates.allocationUpdated.templateName}")
    private String allocationUpdatedTemplateName;

    @Value("${realmeet.email.templates.allocationDeleted.subject}")
    private String allocationDeletedSubject;

    @Value("${realmeet.email.templates.allocationDeleted.templateName}")
    private String allocationDeletedTemplateName;

    @Test
    void testLoadConfigProperties() {
        testLoadSubject(allocationCreatedSubject, TemplateType.ALLOCATION_CREATED);
        testLoadTemplateName(allocationCreatedTemplateName, TemplateType.ALLOCATION_CREATED);

        testLoadSubject(allocationUpdatedSubject, TemplateType.ALLOCATION_UPDATED);
        testLoadTemplateName(allocationUpdatedTemplateName, TemplateType.ALLOCATION_UPDATED);

        testLoadSubject(allocationDeletedSubject, TemplateType.ALLOCATION_DELETED);
        testLoadTemplateName(allocationDeletedTemplateName, TemplateType.ALLOCATION_DELETED);
    }

    private void testLoadSubject(String valueSubject, TemplateType templateType) {
        Assertions.assertNotNull(valueSubject);
        Assertions.assertEquals(valueSubject, templateConfigProperties.getEmailTemplate(templateType).getSubject());
    }

    private void testLoadTemplateName(String valueTemplateName, TemplateType templateType) {
        Assertions.assertNotNull(valueTemplateName);
        Assertions.assertEquals(
            valueTemplateName,
            templateConfigProperties.getEmailTemplate(templateType).getTemplateName()
        );
    }
}
