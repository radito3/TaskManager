package com.sap.exercise.persistence;

import com.sap.exercise.services.DefaultPersistenceConfigurationProvider;
import org.hibernate.cfg.Configuration;

public class TransactionBuilderFactory {

    public static TransactionBuilder getTransactionBuilder() {
        return getTransactionBuilder(DefaultPersistenceConfigurationProvider.getConfiguration());
    }

    public static TransactionBuilder getTransactionBuilder(Configuration configuration) {
        return new TransactionBuilder(configuration);
    }
}
