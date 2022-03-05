package br.com.sw2you.realmeet.email;

import br.com.sw2you.realmeet.config.properties.EmailConfigProperties;
import br.com.sw2you.realmeet.config.properties.TemplateConfigProperties;
import br.com.sw2you.realmeet.email.model.Attachment;
import br.com.sw2you.realmeet.email.model.EmailInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class EmailInfoComponent {
    EmailConfigProperties emailConfigProperties;
    TemplateConfigProperties templateConfigProperties;

    public EmailInfoComponent(
        EmailConfigProperties emailConfigProperties,
        TemplateConfigProperties templateConfigProperties
    ) {
        this.emailConfigProperties = emailConfigProperties;
        this.templateConfigProperties = templateConfigProperties;
    }

    public EmailConfigProperties getEmailConfigProperties() {
        return emailConfigProperties;
    }

    public TemplateConfigProperties getTemplateConfigProperties() {
        return templateConfigProperties;
    }

    public EmailInfo createEmailInfo(String email, TemplateType templateType, Map<String, Object> templateParams) {
        return createEmailInfo(email, templateType, templateParams, null);
    }

    public EmailInfo createEmailInfo(
        String email,
        TemplateType templateType,
        Map<String, Object> templateParams,
        List<Attachment> attachmentList
    ) {
        var emailTemplate = templateConfigProperties.getEmailTemplate(templateType);
        return (
            EmailInfo
                .newEmailInfoBuilder()
                .from(emailConfigProperties.getFrom())
                .to(List.of(email))
                .subject(emailTemplate.getSubject())
                .template(emailTemplate.getTemplateName())
                .templateData(templateParams)
                .attachments(attachmentList)
                .build()
        );
    }
}
