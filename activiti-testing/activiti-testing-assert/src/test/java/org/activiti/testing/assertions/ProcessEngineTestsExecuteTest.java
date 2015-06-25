package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineTests.execute;
import static org.activiti.testing.assertions.ProcessEngineTests.job;
import static org.activiti.testing.assertions.ProcessEngineTests.runtimeService;

import org.activiti.engine.runtime.Job;
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
public class ProcessEngineTestsExecuteTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-execute.bpmn"
  })
  public void testExecute_Success() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-execute"
    );
    // Then
    assertThat(processInstance).isNotEnded();
    // And
    assertThat(job()).isNotNull();
    // When
    execute(job());
    // Then
    assertThat(job()).isNull();
    // And
    assertThat(processInstance).isEnded();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-execute.bpmn"
  })
  public void testExecute_Failure() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-execute"
    );
    // And
    assertThat(processInstance).isNotEnded();
    // And
    final Job job = job();
    execute(job);
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        execute(job);
      }
    }, IllegalStateException.class);
  }

}
