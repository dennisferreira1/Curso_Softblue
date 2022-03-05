package br.com.sw2you.realmeet.service;

import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.email.EmailInfoComponent;
import br.com.sw2you.realmeet.email.EmailSender;
import br.com.sw2you.realmeet.email.TemplateType;
import br.com.sw2you.realmeet.util.Constants;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class NotificationEmailService {
    private final EmailSender emailSender;
    private final EmailInfoComponent emailInfoComponent;

    public NotificationEmailService(EmailSender emailSender, EmailInfoComponent emailInfoComponent) {
        this.emailSender = emailSender;
        this.emailInfoComponent = emailInfoComponent;
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
            emailInfoComponent.createEmailInfo(
                allocation.getEmployee().getEmail(),
                templateType,
                Map.of(Constants.ALLOCATION, allocation)
            )
        );
    }
}
