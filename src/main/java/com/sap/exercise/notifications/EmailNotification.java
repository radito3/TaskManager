package com.sap.exercise.notifications;

import com.sap.exercise.Configuration;
import com.sap.exercise.model.Event;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Properties;

class EmailNotification extends Notification {

    private final Event event;

    EmailNotification(Event event, LocalDateTime time) {
        super(time);
        this.event = event;
    }

    @Override
    public void send() {
        if (!EmailValidator.getInstance(true).isValid(Configuration.USER_EMAIL)) {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Invalid email: " + Configuration.USER_EMAIL,
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Configuration.USER_EMAIL, Configuration.USER_PASS);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Configuration.USER_EMAIL));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(Configuration.RECIPIENT_EMAIL));
            message.setSubject("Event reminder: " + event.getTitle());
            message.setText("Event description: " + event.getDescription(), "utf-8");
            message.setSentDate(new Date());

            Transport.send(message);
        } catch (MessagingException e) {
            Logger.getLogger(EmailNotification.class).error(e.getMessage(), e);
        }
    }
}
