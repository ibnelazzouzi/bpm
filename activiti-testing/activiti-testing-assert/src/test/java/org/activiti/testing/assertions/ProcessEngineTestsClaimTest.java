package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineAssertions.reset;
import static org.activiti.testing.assertions.ProcessEngineTests.claim;
import static org.activiti.testing.assertions.ProcessEngineTests.runtimeService;
import static org.activiti.testing.assertions.ProcessEngineTests.task;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.activiti.testing.assertions.helpers.Failure;
import org.activiti.testing.assertions.helpers.ProcessAssertTestCase;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Martin Schimak <martin.schimak@plexiti.com>
 */
public class ProcessEngineTestsClaimTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @After
  public void tearDown() {
    reset();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-claim.bpmn"
  })
  public void testClaim_Success() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-claim"
    );
    // When
    claim(task(processInstance), "fozzie");
    // Then
    assertThat(task(processInstance)).isNotNull().hasDefinitionKey("UserTask_1").isAssignedTo("fozzie");
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-claim.bpmn"
  })
  public void testClaim_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-claim"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        claim(task("UserTask_2", processInstance), "fozzie");
      }
    }, IllegalArgumentException.class);
  }

}
