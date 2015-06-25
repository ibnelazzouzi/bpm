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
 * @author Johannes Beck (mail@johannes-beck.name)
 */
public class ProcessInstanceAssertHasNotPassedTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-hasNotPassed.bpmn"
  })
  public void testHasNotPassed_OnlyActivity_RunningInstance_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-hasNotPassed"
    );
    // When
    complete(taskQuery().singleResult(), withVariables("doUserTask5", false));
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).hasNotPassed("UserTask_1");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-hasNotPassed.bpmn"
  })
  public void testHasNotPassed_OnlyActivity_RunningInstance_Success() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-hasNotPassed"
    );
    // Then
    assertThat(processInstance).hasNotPassed("UserTask_1");
    // And
    assertThat(processInstance).hasNotPassed("UserTask_2", "UserTask_3", "UserTask_4");
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-hasNotPassed.bpmn"
  })
  public void testHasNotPassed_ParallelActivities_RunningInstance_Success() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-hasNotPassed"
    );
    // When
    complete(taskQuery().singleResult(), withVariables("doUserTask5", false));
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_2").singleResult());
    // Then
    assertThat(processInstance).hasNotPassed("UserTask_3");
    // And
    assertThat(processInstance).hasNotPassed("UserTask_4");
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-hasNotPassed.bpmn"
  })
  public void testHasNotPassed_ParallelActivities_RunningInstance_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-hasNotPassed"
    );
    // When
    complete(taskQuery().singleResult(), withVariables("doUserTask5", false));
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_2").singleResult());
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).hasNotPassed("UserTask_2", "UserTask_3", "UserTask_4");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-hasNotPassed.bpmn"
  })
  public void testHasNotPassed_SeveralActivities_RunningInstance_Success() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-hasNotPassed"
    );
    // When
    complete(taskQuery().singleResult(), withVariables("doUserTask5", false));
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_2").singleResult());
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_3").singleResult());
    // Then
    assertThat(processInstance).hasNotPassed("UserTask_4", "UserTask_5");
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-hasNotPassed.bpmn"
  })
  public void testHasNotPassed_SeveralActivities_HistoricInstance_Success() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-hasNotPassed"
    );
    // When
    complete(taskQuery().singleResult(), withVariables("doUserTask5", false));
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_2").singleResult());
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_3").singleResult());
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_4").singleResult());
    // Then
    assertThat(processInstance).hasNotPassed("UserTask_5");
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-hasNotPassed.bpmn"
  })
  public void testHasNotPassed_SeveralActivities_HistoricInstance_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-hasNotPassed"
    );
    // When
    complete(taskQuery().singleResult(), withVariables("doUserTask5", true));
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_5").singleResult());
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_4").singleResult());
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).hasNotPassed("UserTask_5");
      }
    });
  }

}
