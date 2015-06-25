package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineAssertions.reset;
import static org.activiti.testing.assertions.ProcessEngineTests.complete;
import static org.activiti.testing.assertions.ProcessEngineTests.runtimeService;
import static org.activiti.testing.assertions.ProcessEngineTests.task;
import static org.activiti.testing.assertions.ProcessEngineTests.withVariables;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
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
public class ProcessEngineTestsCompleteTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @After
  public void tearDown() {
    reset();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-complete.bpmn"
  })
  public void testComplete_Success() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-complete"
    );
    // When
    complete(task(processInstance));
    // Then
    assertThat(processInstance).isEnded();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-complete.bpmn"
  })
  public void testComplete_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-complete"
    );
    // And
    final Task task = task(processInstance);
    // When
    complete(task);
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        complete(task);
      }
    }, ActivitiException.class);
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-complete.bpmn"
  })
  public void testComplete_WithVariables_Success() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-complete"
    );
    // When
    complete(task(processInstance), withVariables("a", "b"));
    // Then
    assertThat(processInstance).isEnded();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-complete.bpmn"
  })
  public void testComplete_WithVariables_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-complete"
    );
    // And
    final Task task = task(processInstance);
    // When
    complete(task);
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        complete(task, withVariables("a", "b"));
      }
    }, ActivitiException.class);
  }

}
