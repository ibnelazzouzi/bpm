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
public class TaskAssertHasDefinitionKeyTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @Test
  @Deployment(resources = {
    "TaskAssert-hasDefinitionKey.bpmn"
  })
  public void testHasDefinitionKey_Success() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasDefinitionKey"
    );
    // Then
    assertThat(processInstance).task().hasDefinitionKey("UserTask_1");
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasDefinitionKey.bpmn"
  })
  public void testHasDefinitionKey_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasDefinitionKey"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().hasDefinitionKey("otherDefinitionKey");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasDefinitionKey.bpmn"
  })
  public void testHasDefinitionKey_Null_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasDefinitionKey"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().hasDefinitionKey(null);
      }
    });
  }

}
