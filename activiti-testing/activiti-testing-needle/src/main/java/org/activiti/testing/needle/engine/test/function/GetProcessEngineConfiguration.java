package org.activiti.testing.needle.engine.test.function;

import static com.google.common.base.Preconditions.checkArgument;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.ProcessEngineImpl;

import com.google.common.base.Function;

/**
 * Hides the nasty "getConfiguration from given Engine Hack" in an easy to use
 * function.
 */
public enum GetProcessEngineConfiguration implements Function<ProcessEngine, ProcessEngineConfiguration> {
  INSTANCE;

  @Override
  public ProcessEngineConfiguration apply(final ProcessEngine processEngine) {
    checkArgument(processEngine instanceof ProcessEngineImpl, "processEngine must not be null and of type ProcessEngineImpl!");

    return ((ProcessEngineImpl) processEngine).getProcessEngineConfiguration();
  }

}
