package br.com.sw2you.realmeet.report.model;

import br.com.sw2you.realmeet.email.TemplateType;
import br.com.sw2you.realmeet.report.enumeration.ReportFormat;
import java.util.Arrays;
import java.util.Objects;

public class GeneratedReport {
    private final byte[] bytes;
    private final ReportFormat reportFormat;
    private final String fileName;
    private final String emailTo;
    private final TemplateType templateType;

    private GeneratedReport(GeneratedReportBuilder builder) {
        bytes = builder.bytes;
        reportFormat = builder.reportFormat;
        fileName = builder.fileName;
        emailTo = builder.emailTo;
        templateType = builder.templateType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public ReportFormat getReportFormat() {
        return reportFormat;
    }

    public String getFileName() {
        return fileName;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public TemplateType getTemplateType() {
        return templateType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneratedReport that = (GeneratedReport) o;
        return (
            Arrays.equals(bytes, that.bytes) &&
            reportFormat == that.reportFormat &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(emailTo, that.emailTo) &&
            templateType == that.templateType
        );
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(reportFormat, fileName, emailTo, templateType);
        result = 31 * result + Arrays.hashCode(bytes);
        return result;
    }

    @Override
    public String toString() {
        return (
            "GeneratedReport{" +
            "bytes=" +
            Arrays.toString(bytes) +
            ", reportFormat=" +
            reportFormat +
            ", fileName='" +
            fileName +
            '\'' +
            ", emailTo='" +
            emailTo +
            '\'' +
            ", templateType=" +
            templateType +
            '}'
        );
    }

    public static GeneratedReportBuilder newGeneratedReportBuilder() {
        return new GeneratedReportBuilder();
    }

    public static final class GeneratedReportBuilder {
        private byte[] bytes;
        private ReportFormat reportFormat;
        private String fileName;
        private String emailTo;
        private TemplateType templateType;

        private GeneratedReportBuilder() {}

        public GeneratedReportBuilder bytes(byte[] bytes) {
            this.bytes = bytes;
            return this;
        }

        public GeneratedReportBuilder reportFormat(ReportFormat reportFormat) {
            this.reportFormat = reportFormat;
            return this;
        }

        public GeneratedReportBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public GeneratedReportBuilder emailTo(String emailTo) {
            this.emailTo = emailTo;
            return this;
        }

        public GeneratedReportBuilder templateType(TemplateType templateType) {
            this.templateType = templateType;
            return this;
        }

        public GeneratedReport build() {
            return new GeneratedReport(this);
        }
    }
}
