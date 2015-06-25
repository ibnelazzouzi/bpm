package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineTests.complete;
import static org.activiti.testing.assertions.ProcessEngineTests.runtimeService;
import static org.activiti.testing.assertions.ProcessEngineTests.taskQuery;
import static org.activiti.testing.assertions.ProcessEngineTests.taskService;

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
public class TaskAssertHasCandidateGroupTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @Test
  @Deployment(resources = {
    "TaskAssert-hasCandidateGroup.bpmn"
  })
  public void testHasCandidateGroup_PreDefined_Success() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasCandidateGroup"
    );
    // Then
    assertThat(processInstance).task().hasCandidateGroup("candidateGroup");
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasCandidateGroup.bpmn"
  })
  public void testHasCandidateGroup_PreDefined_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasCandidateGroup"
    );
    // When
    complete(taskQuery().singleResult());
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().hasCandidateGroup("candidateGroup");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasCandidateGroup.bpmn"
  })
  public void testHasCandidateGroup_Predefined_Removed_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasCandidateGroup"
    );
    // When
    taskService().deleteCandidateGroup(taskQuery().singleResult().getId(), "candidateGroup");
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().hasCandidateGroup("candidateGroup");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasCandidateGroup.bpmn"
  })
  public void testHasCandidateGroup_PreDefined_Other_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasCandidateGroup"
    );
    // When
    taskService().deleteCandidateGroup(taskQuery().singleResult().getId(), "candidateGroup");
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().hasCandidateGroup("otherCandidateGroup");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasCandidateGroup.bpmn"
  })
  public void testHasCandidateGroup_ExplicitelySet_Success() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasCandidateGroup"
    );
    // When
    complete(taskQuery().singleResult());
    // And
    taskService().addCandidateGroup(taskQuery().singleResult().getId(), "explicitCandidateGroupId");
    // Then
    assertThat(processInstance).task().hasCandidateGroup("explicitCandidateGroupId");
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasCandidateGroup.bpmn"
  })
  public void testHasCandidateGroup_ExplicitelySet_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasCandidateGroup"
    );
    // When
    complete(taskQuery().singleResult());
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().hasCandidateGroup("candidateGroup");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasCandidateGroup.bpmn"
  })
  public void testHasCandidateGroup_ExplicitelySet_Removed_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasCandidateGroup"
    );
    // When
    complete(taskQuery().singleResult());
    // And
    taskService().addCandidateGroup(taskQuery().singleResult().getId(), "explicitCandidateGroupId");
    // When
    taskService().deleteCandidateGroup(taskQuery().singleResult().getId(), "explicitCandidateGroupId");
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().hasCandidateGroup("explicitCandidateGroupId");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasCandidateGroup.bpmn"
  })
  public void testHasCandidateGroup_ExplicitelySet_Other_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasCandidateGroup"
    );
    // When
    complete(taskQuery().singleResult());
    // And
    taskService().addCandidateGroup(taskQuery().singleResult().getId(), "explicitCandidateGroupId");
    // When
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().hasCandidateGroup("otherCandidateGroup");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasCandidateGroup.bpmn"
  })
  public void testHasCandidateGroup_MoreThanOne_Success() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasCandidateGroup"
    );
    // When
    taskService().addCandidateGroup(taskQuery().singleResult().getId(), "explicitCandidateGroupId");
    // Then
    assertThat(processInstance).task().hasCandidateGroup("candidateGroup");
    // And
    assertThat(processInstance).task().hasCandidateGroup("explicitCandidateGroupId");
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasCandidateGroup.bpmn"
  })
  public void testHasCandidateGroup_MoreThanOne_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasCandidateGroup"
    );
    // When
    taskService().addCandidateGroup(taskQuery().singleResult().getId(), "explicitCandidateGroupId");
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().hasCandidateGroup("otherCandidateGroup");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasCandidateGroup.bpmn"
  })
  public void testHasCandidateGroup_Null_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasCandidateGroup"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().hasCandidateGroup(null);
      }
    });
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasCandidateGroup.bpmn"
  })
  public void testHasCandidateGroup_NonExistingTask_Failure() {
    // Given
    runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasCandidateGroup"
    );
    // When
    final Task task = taskQuery().singleResult();
    complete(task);
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(task).hasCandidateGroup("candidateGroup");
      }
    });
  }

}
