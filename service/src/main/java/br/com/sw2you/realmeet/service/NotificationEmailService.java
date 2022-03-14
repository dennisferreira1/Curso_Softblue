package br.com.sw2you.realmeet.service;

import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.email.EmailInfoGenerator;
import br.com.sw2you.realmeet.email.EmailSender;
import br.com.sw2you.realmeet.email.TemplateType;
import br.com.sw2you.realmeet.util.Constants;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class NotificationEmailService {
    private final EmailSender emailSender;
    private final EmailInfoGenerator emailInfoGenerator;

    public NotificationEmailService(EmailSender emailSender, EmailInfoGenerator emailInfoGenerator) {
        this.emailSender = emailSender;
        this.emailInfoGenerator = emailInfoGenerator;
    }

    public void notifyAllocationCreated(Allocation allocation) {
        notify(allocation, TemplateType.ALLOCATION_CREATED);
    }

    public void notifyAllocationUpdated(Allocation allocation) {
        notify(allocation, TemplateType.ALLOCATION_UPDATED);
    }

    public void notifyAllocationDeleted(Allocation allocation) {
        notify(allocation, TemplateType.ALLOCATION_DELETED);
    }

    private void notify(Allocation allocation, TemplateType templateType) {
        emailSender.send(
            emailInfoGenerator.createEmailInfo(
                allocation.getEmployee().getEmail(),
                templateType,
                Map.of(Constants.ALLOCATION, allocation)
            )
        );
    }
}
