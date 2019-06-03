package com.sap.exercise.handler;

import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.persistence.SessionProviderFactory;
import org.hibernate.Session;

import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.Map;

public class TimeFrameCondition implements CrudCondition {

    protected String startDate;
    protected String endDate;

    public TimeFrameCondition(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public Map<String, Object> parameters() {
        Map<String, Object> result = new HashMap<>();
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        return result;
    }

    @Override
    public Predicate queryCondition() {
        Session session = SessionProviderFactory.getSessionProvider().getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<CalendarEvents> criteriaQuery = criteriaBuilder.createQuery(CalendarEvents.class);
        Root<CalendarEvents> root = criteriaQuery.from(CalendarEvents.class);
        return criteriaBuilder.between(root.get("date"), startDate, endDate);
    }
}
