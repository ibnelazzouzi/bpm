package org.activiti.testing.needle.example;

import org.activiti.engine.runtime.ProcessInstance;

/**
 * Created by jangalinski on 04.02.14.
 *
 * @author Jan Galinski, Holisticon AG
 */
public interface TestProcessStarter {

  String VARIABLE_STARTED_BY = "processStartedBy";

  /**
   * Starts the process "test-process" and sets the variable "processStartedBy".
   *
   * @param startedByUser
   *          the user id of the user who started the process
   * @param businessKey
   *          teh process business key
   * @return the started instance
   */
  ProcessInstance startProcessWithUser(String startedByUser, String businessKey);

}
