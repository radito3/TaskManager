package com.sap.exercise.handler;

import com.sap.exercise.model.Event;
import org.apache.log4j.Logger;

import javax.swing.JOptionPane;

public class PopupNotificationHandler extends Notifications implements Runnable {

    PopupNotificationHandler(Event event) {
        super(event);
    }

    @Override
    public void run() {
        if (event != null) {
            try {
                Thread.sleep(timeTo < 0 ? 0 : timeTo);

                int duration = event.getDuration();
                boolean daysOrMinutes = event.getAllDay();
                String body = event.getTitle() + "\nDuration: " + duration + (daysOrMinutes ? " days" : " minutes");

                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), body, "Event reminder", JOptionPane.PLAIN_MESSAGE);

            } catch (InterruptedException e) {
                Logger.getLogger(PopupNotificationHandler.class).debug("Notification deleted", e);
            }
        }
    }
}
