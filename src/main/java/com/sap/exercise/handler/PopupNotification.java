package com.sap.exercise.handler;

import com.sap.exercise.model.Event;

import javax.swing.JOptionPane;

public class PopupNotification extends AbstractNotification {

    PopupNotification(Event event) {
        super(event);
    }

    @Override
    public void send() {
        int duration = this.event.getDuration();
        boolean daysOrMinutes = this.event.getAllDay();
        String body = this.event.getTitle() + "\nDuration: " + duration + (daysOrMinutes ? " days" : " minutes");

        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), body, "Event reminder", JOptionPane.PLAIN_MESSAGE);
    }
}
