package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineTests.execute;
import static org.activiti.testing.assertions.ProcessEngineTests.jobQuery;
import static org.activiti.testing.assertions.ProcessEngineTests.runtimeService;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.activiti.engine.test.mock.Mocks;
import org.activiti.testing.assertions.helpers.Failure;
import org.activiti.testing.assertions.helpers.ProcessAssertTestCase;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Martin Schimak <martin.schimak@plexiti.com>
 */
public class ProcessInstanceAssertJobTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-job.bpmn"
  })
  public void testJob_Single_Success() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-job"
    );
    // Then
    assertThat(processInstance).job().isNotNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-job.bpmn"
  })
  public void testJob_SingleWithQuery_Success() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-job"
    );
    // Then
    assertThat(processInstance).job(jobQuery().executable()).isNotNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-job.bpmn"
  })
  public void testJob_MultipleWithQuery_Success() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-job"
    );
    // Then
    assertThat(processInstance).job("ServiceTask_1").isNotNull();
    // And
    Mocks.register("serviceTask_1", "someService");
    // And
    execute(jobQuery().singleResult());
    // Then
    assertThat(processInstance).job("ServiceTask_2").isNotNull();
    // And
    assertThat(processInstance).job("ServiceTask_3").isNotNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-job.bpmn"
  })
  public void testJob_NotYet_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-job"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).job("ServiceTask_2").isNotNull();
      }
    });
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-job.bpmn"
  })
  public void testJob_Passed_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-job"
    );
    // Then
    Mocks.register("serviceTask_1", "someService");
    // When
    execute(jobQuery().singleResult());
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task("ServiceTask_1").isNotNull();
      }
    });
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-job.bpmn"
  })
  public void testJob_MultipleWithQuery_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-job"
    );
    // And
    Mocks.register("serviceTask_1", "someService");
    execute(jobQuery().singleResult());
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).job(jobQuery().executable()).isNotNull();
      }
    }, ActivitiException.class);
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-job.bpmn"
  })
  public void testJob_MultipleWithTaskDefinitionKey_Success() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-job"
    );
    // And
    Mocks.register("serviceTask_1", "someService");
    execute(jobQuery().singleResult());
    // Then
    assertThat(processInstance).job("ServiceTask_2").isNotNull();
    // And
    assertThat(processInstance).job("ServiceTask_3").isNotNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-job.bpmn"
  })
  public void testJob_MultipleWithTaskDefinitionKey_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-job"
    );
    // And
    Mocks.register("serviceTask_1", "someService");
    execute(jobQuery().list().get(0));
    // And
    Mocks.register("serviceTask_2", "someService");
    execute(jobQuery().list().get(0));
    // And
    Mocks.register("serviceTask_3", "someService");
    execute(jobQuery().list().get(0));
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).job("ServiceTask_4").isNotNull();
      }
    }, ActivitiException.class);
  }

}
