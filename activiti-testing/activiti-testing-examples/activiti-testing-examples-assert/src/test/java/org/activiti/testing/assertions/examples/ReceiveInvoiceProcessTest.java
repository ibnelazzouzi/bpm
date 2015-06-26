package org.activiti.testing.assertions.examples;

import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

/**
 * Sanity test to verify the example process is syntactical correct and can be
 * deployed to the engine. All "real" tests should be implemented in the
 * according modules (jbehave, assertions, needle, ...).
 */
public class ReceiveInvoiceProcessTest {

  @Rule
  public final ActivitiRule processEngineRule = new ActivitiRule();

  @Test
  @Deployment(resources = "camunda-testing-invoice-en.bpmn")
  public void shouldDeployWithoutErrors() throws Exception {
    // nothing here, test successful if deployment works
  }
}
