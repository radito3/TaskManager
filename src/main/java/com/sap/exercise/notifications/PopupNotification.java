package com.sap.exercise.notifications;

import com.sap.exercise.model.Event;

import javax.swing.JOptionPane;
import java.time.LocalDateTime;

class PopupNotification extends Notification {

    private final Event event;

    PopupNotification(Event event, LocalDateTime time) {
        super(time);
        this.event = event;
    }

    @Override
    public void send() {
        int duration = event.getDuration();
        boolean isAllDay = event.getAllDay();
        String body = event.getTitle() + "\nDuration: " + duration + (isAllDay ? " days" : " minutes");

        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), body, "Event reminder", JOptionPane.PLAIN_MESSAGE);
    }
}
