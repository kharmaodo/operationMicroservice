package sn.free.selfcare.service.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class NotificationDTO implements Serializable {

    private String from;

    @NotNull
    private String to;

    @NotNull
    private String text;

    private String subject;

	public NotificationDTO() {
        this.from = "freeB2B";
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
            "from='" + from + '\'' +
            ", to='" + to + '\'' +
            ", text='" + text + '\'' +
            ", subject='" + subject + '\'' +
            '}';
    }
}
