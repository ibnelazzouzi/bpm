package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineTests.runtimeService;

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
public class ProcessInstanceAssertIsActiveTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isActive.bpmn"
  })
  public void testIsActive_Success() {
    // When
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-isActive"
    );
    // Then
    assertThat(processInstance).isActive();
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isActive.bpmn"
  })
  public void testIsActive_AfterActivation_Success() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-isActive"
    );
    // And
    runtimeService().suspendProcessInstanceById(processInstance.getId());
    // When
    runtimeService().activateProcessInstanceById(processInstance.getId());
    // Then
    assertThat(processInstance).isActive();
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isActive.bpmn"
  })
  public void testIsActive_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-isActive"
    );
    // When
    runtimeService().suspendProcessInstanceById(processInstance.getId());
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).isActive();
      }
    });
  }

}
