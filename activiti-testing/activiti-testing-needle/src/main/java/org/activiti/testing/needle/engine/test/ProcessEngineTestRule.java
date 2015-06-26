package org.activiti.testing.needle.engine.test;

import java.util.Date;

import org.activiti.engine.ProcessEngine;
import org.junit.rules.TestRule;

/**
 * Combined interface of {@link org.activiti.testing.needle.engine.ProcessEngineServices}
 * and {@link TestRule}.
 */
public interface ProcessEngineTestRule extends TestRule, ProcessEngine {

  /**
   * Sets current time of in memory engine. Use to test timers etc.
   *
   * @param currentTime
   *          time to set
   */
  void setCurrentTime(Date currentTime);

  /**
   * Provide deployment id after deploying with @Deployment-annotation.
   *
   * @return current deployment id
   */
  String getDeploymentId();

  /**
   * Get the process engine.
   *
   * @return the process engine
   */
  ProcessEngine getProcessEngine();

}
