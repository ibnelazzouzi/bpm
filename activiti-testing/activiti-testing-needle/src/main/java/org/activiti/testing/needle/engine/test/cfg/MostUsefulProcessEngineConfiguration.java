package org.activiti.testing.needle.engine.test.cfg;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.activiti.engine.impl.jobexecutor.JobHandler;
import org.activiti.engine.test.mock.MockExpressionManager;

import com.google.common.base.Supplier;

/**
 * Configuration that makes the standard activiti.cfg.xml obsolete by setting the
 * history, schema and job-executor settings.
 */
public class MostUsefulProcessEngineConfiguration extends StandaloneInMemProcessEngineConfiguration {

  public static MostUsefulProcessEngineConfiguration mostUsefulProcessEngineConfiguration() {
    return new MostUsefulProcessEngineConfiguration();
  }

  public static final Supplier<ProcessEngineConfiguration> SUPPLIER = new Supplier<ProcessEngineConfiguration>() {
    @Override
    public ProcessEngineConfiguration get() {
      return mostUsefulProcessEngineConfiguration();
    }
  };

  public MostUsefulProcessEngineConfiguration() {
    this.history = "full";
    this.databaseSchemaUpdate = DB_SCHEMA_UPDATE_TRUE;
    this.jobExecutorActivate = false;
    this.expressionManager = new MockExpressionManager();
    //this.setCustomPostBPMNParseListeners(new ArrayList<BpmnParseListener>());
    this.setCustomJobHandlers(new ArrayList<JobHandler>());
  }

  public void addCustomJobHandler(final JobHandler jobHandler) {
    checkArgument(jobHandler != null, "jobHandler must not be null!");
    getCustomJobHandlers().add(jobHandler);
  }

//  public void addCustomPostBpmnParseListener(final BpmnParseListener bpmnParseListener) {
//    checkArgument(bpmnParseListener != null, "bpmnParseListener must not be null!");
//    getCustomPostBPMNParseListeners().add(bpmnParseListener);
//  }
}
