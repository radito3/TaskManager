package com.sap.exercise.handler;

import com.sap.exercise.Application;
import com.sap.exercise.model.Event;
import com.sap.exercise.printer.OutputPrinter;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static com.sap.exercise.Application.Configuration.DEFAULT_NOTIFICATION;
import static com.sap.exercise.Application.Configuration.OUTPUT;

public class NotificationHandler implements Runnable {

    private static Map<Serializable, Thread> events = new ConcurrentHashMap<>();

    private final OutputPrinter printer = new OutputPrinter(OUTPUT);
    private long timeTo;
    private Event event;
    private Application.Configuration.NotificationType notificationType = DEFAULT_NOTIFICATION;

    NotificationHandler(Event event) {
        if (!events.containsKey(event.getId())) {
            Calendar timeOf = event.getTimeOf();
            Calendar now = Calendar.getInstance();
            this.timeTo = (timeOf.get(Calendar.HOUR_OF_DAY) * 60 + timeOf.get(Calendar.MINUTE))
                    - (now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE))
                    - event.getReminder();
            this.event = event;
            events.put(event.getId(), Thread.currentThread());
        }
    }

    NotificationHandler(Event event, Application.Configuration.NotificationType type) {
        this(event);
        this.notificationType = type;
    }

    @Override
    public void run() {
        if (event != null) {
            try {
                Thread.sleep(timeTo < 0 ? 0 : timeTo * 60000);
                switch (notificationType) {
                    case POPUP:
                        notifyByPopup();
                        break;
                    case EMAIL:
                        notifyByEmail();
                }
            } catch (InterruptedException e) {
                printer.error(e.getMessage()); //may need to remove
            }
        }
    }

    static Runnable onDelete(Event event) {
        return () -> {
            Thread thread;
            if ((thread = events.get(event.getId())) != null) {
                thread.interrupt();
                events.remove(event.getId());
            }
        };
    }

    private void notifyByPopup() {
        int duration = event.getDuration();
        boolean daysOrMinutes = event.getAllDay();
        String body = event.getTitle() + "\nDuration: " + duration + (daysOrMinutes ? " days" : " minutes");

        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), body, "Event reminder", JOptionPane.PLAIN_MESSAGE);
    }

    private void notifyByEmail() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "my-mail-server");
        Session session = Session.getInstance(props, null);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom("me@example.com");
            msg.setRecipients(Message.RecipientType.TO, "you@example.com");
            msg.setSubject("Event reminder: " + event.getTitle());
            msg.setSentDate(new Date());
            msg.setText("Event description: " + event.getDescription());
            Transport.send(msg, "me@example.com", "my-password");
        } catch (MessagingException mex) {
            printer.error("Send failed, exception: " + mex.toString());
        }
    }
}
