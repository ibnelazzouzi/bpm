package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineTests.complete;
import static org.activiti.testing.assertions.ProcessEngineTests.runtimeService;
import static org.activiti.testing.assertions.ProcessEngineTests.taskQuery;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.activiti.testing.assertions.helpers.Failure;
import org.activiti.testing.assertions.helpers.ProcessAssertTestCase;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Martin Schimak <martin.schimak@plexiti.com>
 */
public class ProcessInstanceAssertIsStartedTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isStarted.bpmn"
  })
  public void testIsStarted_AndActive_Success() {
    // When
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-isStarted"
    );
    // Then
    assertThat(processInstance).isStarted();
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isStarted.bpmn"
  })
  public void testIsStarted_AndEnded_Success() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-isStarted"
    );
    // When
    complete(taskQuery().singleResult());
    // Then
    assertThat(processInstance).isStarted();
  }
  
  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isStarted.bpmn"
  })
  public void testIsStarted_Failure() {
    // When
    final ProcessInstance processInstance = mock(ProcessInstance.class);
    // And
    when(processInstance.getId()).thenReturn("someNonExistingId");
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).isStarted();
      }
    });
  }

}
