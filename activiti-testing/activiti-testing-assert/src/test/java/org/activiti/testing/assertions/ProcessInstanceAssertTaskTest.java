package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineTests.complete;
import static org.activiti.testing.assertions.ProcessEngineTests.runtimeService;
import static org.activiti.testing.assertions.ProcessEngineTests.taskQuery;

import org.activiti.engine.ActivitiException;
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
public class ProcessInstanceAssertTaskTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-task.bpmn"
  })
  public void testTask_Single_Success() {
    // When
    final ProcessInstance processInstance = startProcess();
    // Then
    assertThat(processInstance).task().isNotNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-task.bpmn"
  })
  public void testTask_SingleWithQuery_Success() {
    // When
    final ProcessInstance processInstance = startProcess();
    // Then
    assertThat(processInstance).task(taskQuery().taskDefinitionKey("UserTask_1")).isNotNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-task.bpmn"
  })
  public void testTask_MultipleWithQuery_Success() {
    // When
    final ProcessInstance processInstance = startProcess();
    // And
    complete(taskQuery().singleResult());
    // Then
    assertThat(processInstance).task(taskQuery().taskDefinitionKey("UserTask_2")).isNotNull();
    // And
    assertThat(processInstance).task(taskQuery().taskDefinitionKey("UserTask_3")).isNotNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-task.bpmn"
  })
  public void testTask_NotYet_Failure() {
    // When
    final ProcessInstance processInstance = startProcess();
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task(taskQuery().taskDefinitionKey("UserTask_2")).isNotNull();
      }
    });
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-task.bpmn"
  })
  public void testTask_Passed_Failure() {
    // Given
    final ProcessInstance processInstance = startProcess();
    // When
    complete(taskQuery().singleResult());
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task(taskQuery().taskDefinitionKey("UserTask_1")).isNotNull();
      }
    });
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-task.bpmn"
  })
  public void testTask_MultipleWithQuery_Failure() {
    // When
    final ProcessInstance processInstance = startProcess();
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_1").singleResult());
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_2").singleResult());
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_3").singleResult());
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task(taskQuery().taskDefinitionKey("UserTask_4")).isNotNull();
      }
    }, ActivitiException.class);
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-task.bpmn"
  })
  public void testTask_MultipleWithTaskDefinitionKey_Success() {
    // When
    final ProcessInstance processInstance = startProcess();
    // And
    complete(taskQuery().singleResult());
    // Then
    assertThat(processInstance).task("UserTask_2").isNotNull();
    // And
    assertThat(processInstance).task("UserTask_3").isNotNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-task.bpmn"
  })
  public void testTask_MultipleWithTaskDefinitionKey_Failure() {
    // When
    final ProcessInstance processInstance = startProcess();
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_1").singleResult());
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_2").singleResult());
    // And
    complete(taskQuery().taskDefinitionKey("UserTask_3").singleResult());
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task("UserTask_4").isNotNull();
      }
    }, ActivitiException.class);
  }


  @Test
  @Deployment(resources = {
      "ProcessInstanceAssert-task.bpmn"
  })
  public void testTask_notWaitingAtTaskDefinitionKey() {
    final ProcessInstance processInstance = startProcess();
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task("UserTask_2").isNotNull();
      }
    });
  }

  private ProcessInstance startProcess() {
    return runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-task"
    );
  }
  
}
