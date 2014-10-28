package com.hp.score.lang.tests;

import com.google.common.collect.ImmutableMap;
import com.hp.score.api.ExecutionPlan;
import com.hp.score.api.Score;
import com.hp.score.api.TriggeringProperties;
import com.hp.score.lang.runtime.RunEnvironment;
import com.hp.score.lang.runtime.ScoreLangConstants;
import com.hp.score.lang.tests.runtime.builders.POCExecutionPlanActionsBuilder;
import com.hp.score.lang.tests.runtime.builders.POCParentExecutionPlanActionsBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * User: stoneo
 * Date: 06/10/2014
 * Time: 08:36
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/pocContext.xml")
public class RuntimePOC {

    @Autowired
    private Score score;

    @Test
    public void testLangRuntime() {

        //Parse YAML -> flow

        //Compile Flow -> ExecutionPlan
        POCExecutionPlanActionsBuilder builder = new POCExecutionPlanActionsBuilder();
        ExecutionPlan executionPlan = builder.getExecutionPlan();

        POCParentExecutionPlanActionsBuilder parentBuilder = new POCParentExecutionPlanActionsBuilder();
        ExecutionPlan parentExecutionPlan = parentBuilder.getExecutionPlan();

        //Trigger ExecutionPlan
        Map<String, Serializable> executionContext = createExecutionContext();
        Map<String, ExecutionPlan> dependencies = ImmutableMap.of("childFlow", executionPlan);
        TriggeringProperties triggeringProperties = TriggeringProperties
                .create(parentExecutionPlan)
                .setContext(executionContext)
                .setDependencies(dependencies);
        score.trigger(triggeringProperties);
        while(true){}
    }

    private static Map<String, Serializable> createExecutionContext() {
        Map<String, Serializable> executionContext = new HashMap<>();
        executionContext.put(ScoreLangConstants.RUN_ENV, new RunEnvironment());
        return executionContext;
    }


}