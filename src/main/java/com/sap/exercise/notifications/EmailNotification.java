package com.sap.exercise.notifications;

import com.sap.exercise.Application;
import com.sap.exercise.model.Event;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import java.util.Date;
import java.util.Properties;

class EmailNotification implements Notification {

    private boolean isValidEmail;
    private Event event;

    EmailNotification(Event event) {
        this.event = event;
        EmailValidator validator = EmailValidator.getInstance(true);

        if (!validator.isValid(Application.Configuration.USER_EMAIL)) {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Invalid email", "Error", JOptionPane.ERROR_MESSAGE);
            isValidEmail = false;
        } else {
            isValidEmail = true;
        }
    }

    @Override
    public void send() {
        if (isValidEmail) {
            Properties props = new Properties();
            props.put("mail.smtp.host", "my-mail-server");
            Session session = Session.getInstance(props, null);

            try {
                MimeMessage msg = new MimeMessage(session);
                msg.setFrom("me@example.com");
                msg.setRecipients(Message.RecipientType.TO, Application.Configuration.USER_EMAIL);
                msg.setSubject("Event reminder: " + event.getTitle());
                msg.setSentDate(new Date());
                msg.setText("Event description: " + event.getDescription(), "utf-8", "plain");
                Transport.send(msg, "me@example.com", "my-password");
            } catch (MessagingException mex) {
                Logger.getLogger(EmailNotification.class).error("Message sending error", mex);
            }
        }
    }
}
