package com.sap.exercise.handler;

import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.persistence.HibernateUtilFactory;
import org.hibernate.Session;

import javax.persistence.criteria.*;
import java.util.Map;

public class GetInTimeFrameOptions implements CrudOptions {

    protected String startDate;
    protected String endDate;

    public GetInTimeFrameOptions(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public Map<String, Object> getParameters() {
        return null;
    }

    @Override
    public Predicate getPredicate() {
        Session session = HibernateUtilFactory.getHibernateUtil().getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaDelete<CalendarEvents> criteriaDelete =
                criteriaBuilder.createCriteriaDelete(CalendarEvents.class);
        Root<CalendarEvents> root = criteriaDelete.from(CalendarEvents.class);
        return criteriaBuilder.between(root.get("date"), startDate, endDate);
    }
}
