package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineAssertions.reset;
import static org.activiti.testing.assertions.ProcessEngineTests.runtimeService;
import static org.activiti.testing.assertions.ProcessEngineTests.task;
import static org.activiti.testing.assertions.ProcessEngineTests.unclaim;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.activiti.testing.assertions.helpers.Failure;
import org.activiti.testing.assertions.helpers.ProcessAssertTestCase;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Jens Kanschik
 */
public class ProcessEngineTestsUnclaimTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @After
  public void tearDown() {
    reset();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-unclaim.bpmn"
  })
  public void testUnclaim_Success() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-unclaim"
    );
    // When
    assertThat(task(processInstance)).isNotNull().isAssignedTo("fozzie");
    unclaim(task(processInstance));
    // Then
    assertThat(task(processInstance)).isNotNull().hasDefinitionKey("UserTask_1").isNotAssigned();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-unclaim.bpmn"
  })
  public void testUnclaim_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-unclaim"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        unclaim(task("UserTask_2", processInstance));
      }
    }, IllegalArgumentException.class);
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-unclaim.bpmn"
  })
  public void testUnclaim_AlreadyUnclaimed() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-unclaim"
    );
    // When
    unclaim(task(processInstance));
    unclaim(task(processInstance));
    // Then
    assertThat(task(processInstance)).isNotNull().hasDefinitionKey("UserTask_1").isNotAssigned();
  }

}
