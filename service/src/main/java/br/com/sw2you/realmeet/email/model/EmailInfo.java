package br.com.sw2you.realmeet.email.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EmailInfo {
    private final String from;
    private final List<String> to;
    private final List<String> cc;
    private final List<String> bcc;
    private final String subject;
    private final List<Attachment> attachments;
    private final String template;
    private final Map<String, Object> templateData;

    private EmailInfo(EmailInfoBuilder emailInfoBuilder) {
        from = emailInfoBuilder.from;
        to = emailInfoBuilder.to;
        cc = emailInfoBuilder.cc;
        bcc = emailInfoBuilder.bcc;
        subject = emailInfoBuilder.subject;
        attachments = emailInfoBuilder.attachments;
        template = emailInfoBuilder.template;
        templateData = emailInfoBuilder.templateData;
    }

    public String getFrom() {
        return from;
    }

    public List<String> getTo() {
        return to;
    }

    public List<String> getCc() {
        return cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public String getSubject() {
        return subject;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public String getTemplate() {
        return template;
    }

    public Map<String, Object> getTemplateData() {
        return templateData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailInfo emailInfo = (EmailInfo) o;
        return (
            Objects.equals(from, emailInfo.from) &&
            Objects.equals(to, emailInfo.to) &&
            Objects.equals(cc, emailInfo.cc) &&
            Objects.equals(bcc, emailInfo.bcc) &&
            Objects.equals(subject, emailInfo.subject) &&
            Objects.equals(attachments, emailInfo.attachments) &&
            Objects.equals(template, emailInfo.template) &&
            Objects.equals(templateData, emailInfo.templateData)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, cc, bcc, subject, attachments, template, templateData);
    }

    @Override
    public String toString() {
        return (
            "EmailInfo{" +
            "from='" +
            from +
            '\'' +
            ", to=" +
            to +
            ", cc=" +
            cc +
            ", bcc=" +
            bcc +
            ", subject='" +
            subject +
            '\'' +
            ", attachments=" +
            attachments +
            ", template='" +
            template +
            '\'' +
            ", templateData=" +
            templateData +
            '}'
        );
    }

    public static EmailInfoBuilder newEmailInfoBuilder() {
        return new EmailInfoBuilder();
    }

    public static final class EmailInfoBuilder {
        private String from;
        private List<String> to;
        private List<String> cc;
        private List<String> bcc;
        private String subject;
        private List<Attachment> attachments;
        private String template;
        private Map<String, Object> templateData;

        private EmailInfoBuilder() {}

        public EmailInfoBuilder from(String from) {
            this.from = from;
            return this;
        }

        public EmailInfoBuilder to(List<String> to) {
            this.to = to;
            return this;
        }

        public EmailInfoBuilder cc(List<String> cc) {
            this.cc = cc;
            return this;
        }

        public EmailInfoBuilder bcc(List<String> bcc) {
            this.bcc = bcc;
            return this;
        }

        public EmailInfoBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public EmailInfoBuilder attachments(List<Attachment> attachments) {
            this.attachments = attachments;
            return this;
        }

        public EmailInfoBuilder template(String template) {
            this.template = template;
            return this;
        }

        public EmailInfoBuilder templateData(Map<String, Object> templateData) {
            this.templateData = templateData;
            return this;
        }

        public EmailInfo build() {
            return new EmailInfo(this);
        }
    }
}
