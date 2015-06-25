package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineTests.runtimeService;
import static org.activiti.testing.assertions.ProcessEngineTests.withVariables;

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
public class ProcessInstanceAssertHasNoVariablesTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-hasNoVariables.bpmn"
  })
  public void testHasNoVariables_None_Success() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-hasNoVariables"
    );
    // Then
    assertThat(processInstance).hasNoVariables();
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-hasNoVariables.bpmn"
  })
  public void testHasNoVariables_One_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-hasNoVariables", withVariables("aVariable", "aValue")
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).hasNoVariables();
      }
    });
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-hasNoVariables.bpmn"
  })
  public void testHasNoVariables_Two_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-hasNoVariables", withVariables("firstVariable", "firstValue", "secondVariable", "secondValue")
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).hasNoVariables();
      }
    });
  }

}
