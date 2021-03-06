package ca.ulaval.glo4003.evulution.domain.email;

import ca.ulaval.glo4003.evulution.domain.email.exceptions.EmailException;

import java.util.List;

public class Email {
    private final List<String> recipients;
    private final String subject;
    private final String message;
    private final EmailSender emailSender;

    public Email(List<String> recipients, String subject, String message, EmailSender emailSender) {
        this.recipients = recipients;
        this.subject = subject;
        this.message = message;
        this.emailSender = emailSender;
    }

    public void send() throws EmailException {
        emailSender.sendEmail(recipients, subject, message);
    }
}
