package br.com.sw2you.realmeet.service;

import br.com.sw2you.realmeet.email.EmailInfoGenerator;
import br.com.sw2you.realmeet.email.EmailSender;
import br.com.sw2you.realmeet.email.model.Attachment;
import br.com.sw2you.realmeet.report.model.GeneratedReport;
import java.io.ByteArrayInputStream;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReportDispatcherService {
    private final EmailSender emailSender;
    private final EmailInfoGenerator emailInfoGenerator;

    public ReportDispatcherService(EmailSender emailSender, EmailInfoGenerator emailInfoGenerator) {
        this.emailSender = emailSender;
        this.emailInfoGenerator = emailInfoGenerator;
    }

    public void dispatch(GeneratedReport generatedReport) {
        emailSender.send(
            emailInfoGenerator.createEmailInfo(
                generatedReport.getEmailTo(),
                generatedReport.getTemplateType(),
                null,
                List.of(
                    Attachment
                        .newAttachmentBuilder()
                        .fileName(generatedReport.getFileName())
                        .contentType(generatedReport.getReportFormat().getContentType())
                        .inputStream(new ByteArrayInputStream(generatedReport.getBytes()))
                        .build()
                )
            )
        );
    }
}
