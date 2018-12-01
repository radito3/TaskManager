package com.sap.exercise.handler;

import com.sap.exercise.Application;
import com.sap.exercise.model.Event;
import org.apache.commons.validator.routines.EmailValidator;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import java.util.Date;
import java.util.Properties;

public class EmailNotificationHandler extends Notifications implements Runnable {

    private boolean isValidEmail;

    EmailNotificationHandler(Event event) {
        super(event);

        EmailValidator validator = EmailValidator.getInstance(true);

        if (!validator.isValid(Application.Configuration.USER_EMAIL)) {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Invalid email", "Error", JOptionPane.ERROR_MESSAGE);
            isValidEmail = false;
        } else {
            isValidEmail = true;
        }
    }

    @Override
    public void run() {
        if (event != null) {
            try {
                Thread.sleep(timeTo < 0 ? 0 : timeTo);

                notifyByEmail();

            } catch (InterruptedException ignored) {}
        }
    }

    private void notifyByEmail() {
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
                printer.error("Send failed, exception: " + mex.toString());
            }
        }
    }
}
