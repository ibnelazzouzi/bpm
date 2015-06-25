package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineTests.complete;
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
public class ProcessInstanceAssertIsWaitingAtTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isWaitingAt.bpmn"
  })
  public void testIsWaitingAt_Only_Activity_Success() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-isWaitingAt"
    );
    // Then
    assertThat(processInstance).isWaitingAt("UserTask_1");
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isWaitingAt.bpmn"
  })
  public void testIsWaitingAt_Only_Activity_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-isWaitingAt"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).isWaitingAt("UserTask_2");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isWaitingAt.bpmn"
  })
  public void testIsWaitingAt_Non_Existing_Activity_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-isWaitingAt"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).isWaitingAt("NonExistingUserTask");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isWaitingAt.bpmn"
  })
  public void testIsWaitingAt_One_Of_Two_Activities_Success() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-isWaitingAt"
    );
    // When
    complete(taskQuery().singleResult());
    // Then
    assertThat(processInstance).isWaitingAt("UserTask_2");
    // And
    assertThat(processInstance).isWaitingAt("UserTask_3");
    // And
    assertThat(processInstance).isWaitingAt("UserTask_2", "UserTask_3");
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isWaitingAt.bpmn"
  })
  public void testIsWaitingAt_One_Of_Two_Activities_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-isWaitingAt"
    );
    // When
    complete(taskQuery().singleResult());
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).isWaitingAt("UserTask_1");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isWaitingAt.bpmn"
  })
  public void testIsWaitingAt_Null_Error() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-isWaitingAt"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        //noinspection NullArgumentToVariableArgMethod
        assertThat(processInstance).isWaitingAt(null);
      }
    });
    // And
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).isWaitingAt("ok", null);
      }
    });
    // And
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).isWaitingAt(null, "ok");
      }
    });
    // And
    expect(new Failure() {
      @Override
      public void when() {
        String[] args = new String[]{};
        assertThat(processInstance).isWaitingAt(args);
      }
    });
  }

}
