package com.sap.exercise.persistence;

import org.hibernate.cfg.Configuration;

public class TransactionBuilderFactory {

    public static TransactionBuilder getTransactionBuilder() {
        return new TransactionBuilder(SessionProviderFactory.getSessionProvider().getSession());
    }

    public static TransactionBuilder getTransactionBuilder(Configuration config) {
        return new TransactionBuilder(SessionProviderFactory.getSessionProvider(config).getSession());
    }
}
