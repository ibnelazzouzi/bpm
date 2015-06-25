package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineTests.claim;
import static org.activiti.testing.assertions.ProcessEngineTests.complete;
import static org.activiti.testing.assertions.ProcessEngineTests.runtimeService;
import static org.activiti.testing.assertions.ProcessEngineTests.taskQuery;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.activiti.testing.assertions.helpers.Failure;
import org.activiti.testing.assertions.helpers.ProcessAssertTestCase;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Martin Schimak <martin.schimak@plexiti.com>
 */
public class TaskAssertIsAssignedToTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @Test
  @Deployment(resources = {
    "TaskAssert-isAssignedTo.bpmn"
  })
  public void testIsAssignedTo_Success() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-isAssignedTo"
    );
    // When
    claim(taskQuery().singleResult(), "fozzie");
    // Then
    assertThat(processInstance).task().isAssignedTo("fozzie");
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-isAssignedTo.bpmn"
  })
  public void testIsAssignedTo_NotAssigned_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-isAssignedTo"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().isAssignedTo("fozzie");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-isAssignedTo.bpmn"
  })
  public void testIsAssignedTo_OtherAssignee_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-isAssignedTo"
    );
    // When
    claim(taskQuery().singleResult(), "fozzie");
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().isAssignedTo("gonzo");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-isAssignedTo.bpmn"
  })
  public void testIsAssignedTo_Null_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-isAssignedTo"
    );
    // When
    claim(taskQuery().singleResult(), "fozzie");
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().isAssignedTo(null);
      }
    });
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-isAssignedTo.bpmn"
  })
  public void testIsAssignedTo_NonExistingTask_Failure() {
    // Given
    runtimeService().startProcessInstanceByKey(
      "TaskAssert-isAssignedTo"
    );
    // When
    final Task task = taskQuery().singleResult();
    complete(task);
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(task).isAssignedTo("fozzie");
      }
    });
  }

}
