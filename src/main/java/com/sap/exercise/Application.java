package com.sap.exercise;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;

import java.util.HashMap;
import java.util.Map;

public class Application {

    public static void main(String[] args) {
        Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        Logger.getLogger("javax.mail").setLevel(Level.WARN);

        ProcessEngineConfiguration cfg =
                new StandaloneProcessEngineConfiguration()
                        .setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1")
                        .setJdbcUsername("sa")
                        .setJdbcPassword("")
                        .setJdbcDriver("org.h2.Driver")
                        .setDatabaseSchemaUpdate(
                                ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        ProcessEngine processEngine = cfg.buildProcessEngine();

        processEngine.getRepositoryService()
                .createDeployment()
                .addClasspathResource("ConsoleCalendar.bpmn20.xml")
                .deploy();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String, Object> variables = new HashMap<>();
        variables.put("allCommands", new String[] {});
        variables.put("error", false);

        runtimeService.startProcessInstanceByKey("BaseProcess", variables);
    }
}
