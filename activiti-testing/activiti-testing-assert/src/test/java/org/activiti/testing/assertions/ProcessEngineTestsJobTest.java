package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineAssertions.reset;
import static org.activiti.testing.assertions.ProcessEngineTests.execute;
import static org.activiti.testing.assertions.ProcessEngineTests.job;
import static org.activiti.testing.assertions.ProcessEngineTests.jobQuery;
import static org.activiti.testing.assertions.ProcessEngineTests.runtimeService;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.activiti.engine.test.mock.Mocks;
import org.activiti.testing.assertions.helpers.Failure;
import org.activiti.testing.assertions.helpers.ProcessAssertTestCase;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Martin Schimak <martin.schimak@plexiti.com>
 */
public class ProcessEngineTestsJobTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @After
  public void tearDown() {
    reset();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-job.bpmn"
  })
  public void testJob_OnlyActivity_Success() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // When
    assertThat(processInstance).isNotNull();
    // Then
    assertThat(job()).isNotNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-job.bpmn"
  })
  public void testJob_OnlyActivity_Failure() {
    // Given
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        job();
      }
    }, IllegalStateException.class);
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-job.bpmn"
  })
  public void testJob_TwoActivities_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    assertThat(processInstance).isNotNull();
    // And
    Mocks.register("serviceTask_1", "someService");
    // And
    execute(job());
    // When
    expect(new Failure() {
      @Override
      public void when() {
        job();
      }
    }, ActivitiException.class);
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-job.bpmn"
  })
  public void testJob_activityId_OnlyActivity_Success() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // When
    assertThat(processInstance).isNotNull();
    // Then
    assertThat(job("ServiceTask_1")).isNotNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-job.bpmn"
  })
  public void testJob_activityId_TwoActivities_Success() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // When
    assertThat(processInstance).isNotNull();
    // And
    Mocks.register("serviceTask_1", "someService");
    // And
    execute(job());
    // Then
    assertThat(job("ServiceTask_2")).isNotNull();
    // And
    assertThat(job("ServiceTask_3")).isNotNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-job.bpmn"
  })
  public void testJob_activityId_OnlyActivity_Failure() {
    // Given
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        job("ServiceTask_1");
      }
    }, IllegalStateException.class);
  }
  
  @Test
  @Deployment(resources = {
    "ProcessEngineTests-job.bpmn"
  })
  public void testJob_jobQuery_OnlyActivity_Success() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // When
    assertThat(processInstance).isNotNull();
    // Then
    assertThat(job(jobQuery())).isNotNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-job.bpmn"
  })
  public void testJob_jobQuery_OnlyActivity_Failure() {
    // Given
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        job(jobQuery());
      }
    }, IllegalStateException.class);
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-job.bpmn"
  })
  public void testJob_jobQuery_TwoActivities_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    assertThat(processInstance).isNotNull();
    // And
    Mocks.register("serviceTask_1", "someService");
    // And
    execute(job());
    // When
    expect(new Failure() {
      @Override
      public void when() {
        job(jobQuery());
      }
    }, ActivitiException.class);
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-job.bpmn"
  })
  public void testJob_processInstance_OnlyActivity_Success() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // Then
    assertThat(job(processInstance)).isNotNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-job.bpmn"
  })
  public void testJob_TwoActivities_processInstance_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    Mocks.register("serviceTask_1", "someService");
    // And
    execute(job(processInstance));
    // When
    expect(new Failure() {
      @Override
      public void when() {
        job(processInstance);
      }
    }, ActivitiException.class);
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-job.bpmn"
  })
  public void testJob_jobDefinitionKey_processInstance_OnlyActivity_Success() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // Then
    assertThat(job("ServiceTask_1", processInstance)).isNotNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-job.bpmn"
  })
  public void testJob_jobDefinitionKey_processInstance_TwoActivities_Success() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    Mocks.register("serviceTask_1", "someService");
    // When
    execute(job(processInstance));
    // Then
    assertThat(job("ServiceTask_2", processInstance)).isNotNull();
    // And
    assertThat(job("ServiceTask_3", processInstance)).isNotNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-job.bpmn"
  })
  public void testJob_jobQuery_processInstance_OnlyActivity_Success() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // Then
    assertThat(job(jobQuery(), processInstance)).isNotNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-job.bpmn"
  })
  public void testJob_jobQuery_processInstance_TwoActivities_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-job"
    );
    // And
    Mocks.register("serviceTask_1", "someService");
    // And
    execute(job(processInstance));
    // When
    expect(new Failure() {
      @Override
      public void when() {
        job(jobQuery(), processInstance);
      }
    }, ActivitiException.class);
  }

}
