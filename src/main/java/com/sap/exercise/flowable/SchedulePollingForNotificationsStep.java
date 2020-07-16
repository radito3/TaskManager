package com.sap.exercise.flowable;

import com.sap.exercise.notifications.NotificationsService;
import com.sap.exercise.services.SharedResourcesFactory;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.concurrent.TimeUnit;

public class SchedulePollingForNotificationsStep implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        SharedResourcesFactory.getAsyncExecutionsService()
                .schedule(NotificationsService::pollForUpcomingEvents,
                          0L, 24L, TimeUnit.HOURS);
    }
}
