package br.com.sw2you.realmeet.email.model;

import java.io.InputStream;
import java.util.Objects;

public class Attachment {
    private final InputStream inputStream;
    private final String contentType;
    private final String fileName;

    private Attachment(AttachmentBuilder attachmentBuilder) {
        inputStream = attachmentBuilder.inputStream;
        contentType = attachmentBuilder.contentType;
        fileName = attachmentBuilder.fileName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return (
            Objects.equals(inputStream, that.inputStream) &&
            Objects.equals(contentType, that.contentType) &&
            Objects.equals(fileName, that.fileName)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputStream, contentType, fileName);
    }

    @Override
    public String toString() {
        return (
            "Attachment{" +
            "inputStream=" +
            inputStream +
            ", contentType='" +
            contentType +
            '\'' +
            ", fileName='" +
            fileName +
            '\'' +
            '}'
        );
    }

    public static AttachmentBuilder newAttachment() {
        return new AttachmentBuilder();
    }

    public static final class AttachmentBuilder {
        private InputStream inputStream;
        private String contentType;
        private String fileName;

        private AttachmentBuilder() {}

        public AttachmentBuilder inputStream(InputStream inputStream) {
            this.inputStream = inputStream;
            return this;
        }

        public AttachmentBuilder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public AttachmentBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Attachment build() {
            return new Attachment(this);
        }
    }
}
