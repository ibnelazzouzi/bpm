package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineTests.runtimeService;
import static org.activiti.testing.assertions.ProcessEngineTests.taskQuery;
import static org.activiti.testing.assertions.ProcessEngineTests.taskService;

import java.util.Date;

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
public class TaskAssertHasDueDateTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @Test
  @Deployment(resources = {
    "TaskAssert-hasDueDate.bpmn"
  })
  public void testHasDueDate_Success() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasDueDate"
    );
    final Date dueDate = new Date();
    Task task = taskQuery().singleResult();
    task.setDueDate(dueDate);
    taskService().saveTask(task);
    // Then
    assertThat(processInstance).task().hasDueDate(dueDate);
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasDueDate.bpmn"
  })
  public void testHasDueDate_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasDueDate"
    );
    // When
    final Date dueDate = new Date();
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().hasDueDate(dueDate);
      }
    });
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasDueDate.bpmn"
  })
  public void testHasDueDate_Null_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasDueDate"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().hasDueDate(null);
      }
    });
  }

}
