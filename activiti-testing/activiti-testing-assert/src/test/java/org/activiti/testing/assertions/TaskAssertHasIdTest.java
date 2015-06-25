package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineTests.runtimeService;
import static org.activiti.testing.assertions.ProcessEngineTests.taskQuery;

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
public class TaskAssertHasIdTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @Test
  @Deployment(resources = {
    "TaskAssert-hasId.bpmn"
  })
  public void testHasId_Success() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasId"
    );
    // Then
    assertThat(processInstance).task().hasId(taskQuery().singleResult().getId());
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasId.bpmn"
  })
  public void testHasId_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasId"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().hasId("otherDefinitionKey");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasId.bpmn"
  })
  public void testHasId_Null_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasId"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().hasId(null);
      }
    });
  }

}
