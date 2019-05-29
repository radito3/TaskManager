package com.sap.exercise.persistence;

import org.hibernate.cfg.Configuration;

public class TransactionBuilderFactory {

    public static TransactionBuilder getTransactionBuilder() {
        return new TransactionBuilder(HibernateUtilFactory.getHibernateUtil());
    }

    public static TransactionBuilder getTransactionBuilder(Configuration config) {
        return new TransactionBuilder(HibernateUtilFactory.getHibernateUtil(config));
    }
}
