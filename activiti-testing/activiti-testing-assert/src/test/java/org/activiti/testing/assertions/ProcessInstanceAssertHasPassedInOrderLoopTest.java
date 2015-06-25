package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineTests.complete;
import static org.activiti.testing.assertions.ProcessEngineTests.runtimeService;
import static org.activiti.testing.assertions.ProcessEngineTests.taskQuery;
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
public class ProcessInstanceAssertHasPassedInOrderLoopTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();
  
  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-hasPassedInOrder-loop.bpmn"
  })
  public void testHasPassedInOrder_SeveralActivities_HistoricInstance() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-hasPassedInOrder-loop", 
      withVariables("exit", false)
    );
    // When
    complete(taskQuery().taskDefinitionKey("UserTask_1").singleResult());
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_2").singleResult());
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_3").singleResult());
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_4").singleResult());
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_3").singleResult());
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_4").singleResult(), withVariables("exit", true));
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_5").singleResult());
    // Then
    assertThat(processInstance).hasPassedInOrder("UserTask_1", "UserTask_2", "UserTask_5");
    // And
    assertThat(processInstance).hasPassedInOrder("UserTask_1", "UserTask_3", "UserTask_4", "UserTask_3", "UserTask_4", "UserTask_5");
    // And
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).hasPassedInOrder("UserTask_1", "UserTask_3", "UserTask_4", "UserTask_3", "UserTask_4", "UserTask_3", "UserTask_4", "UserTask_5");
      }
    });
  }

}
