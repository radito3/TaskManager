package com.sap.exercise.notifications;

import com.sap.exercise.model.Event;

import javax.swing.JOptionPane;

class PopupNotification implements Notification {

    private Event event;

    PopupNotification(Event event) {
        this.event = event;
    }

    @Override
    public void send() {
        int duration = event.getDuration();
        boolean daysOrMinutes = event.getAllDay();
        String body = event.getTitle() + "\nDuration: " + duration + (daysOrMinutes ? " days" : " minutes");

        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), body, "Event reminder", JOptionPane.PLAIN_MESSAGE);
    }
}
