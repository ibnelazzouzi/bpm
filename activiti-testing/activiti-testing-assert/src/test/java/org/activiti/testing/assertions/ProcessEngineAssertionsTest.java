package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineAssertions.init;
import static org.activiti.testing.assertions.ProcessEngineAssertions.processEngine;
import static org.activiti.testing.assertions.ProcessEngineAssertions.reset;
import static org.assertj.core.api.Assertions.assertThat;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.testing.assertions.JobAssert;
import org.activiti.testing.assertions.ProcessDefinitionAssert;
import org.activiti.testing.assertions.ProcessEngineAssertions;
import org.activiti.testing.assertions.ProcessEngineTests;
import org.activiti.testing.assertions.ProcessInstanceAssert;
import org.activiti.testing.assertions.TaskAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author Martin Schimak <martin.schimak@plexiti.com>
 */
public class ProcessEngineAssertionsTest {

  ProcessEngine processEngine;

  @Before
  public void setUp() {
    processEngine = Mockito.mock(ProcessEngine.class);
    init(processEngine);
  }

  @After
  public void tearDown() {
    reset();
  }

  @Test
  public void testProcessEngine() throws Exception {
    // When
    ProcessEngine returnedEngine = processEngine();
    // Then
    assertThat(returnedEngine).isNotNull().isSameAs(processEngine);
  }

  @Test
  public void testInit() throws Exception {
    // Given
    reset();
    // When
    init(processEngine);
    // Then
    assertThat(ProcessEngineAssertions.processEngine()).isNotNull().isSameAs(processEngine);
  }

  @Test
  public void testReset() throws Exception {
    // When
    reset();
    // Then
    assertThat(ProcessEngineTests.processEngine.get()).isNull();
  }

  @Test
  public void testAssertThat_ProcessDefinition() throws Exception {
    // Given
    ProcessDefinition processDefinition = Mockito.mock(ProcessDefinition.class);
    // When
    ProcessDefinitionAssert returnedAssert = assertThat(processDefinition);
    // Then
    assertThat(returnedAssert).isNotNull().isInstanceOf(ProcessDefinitionAssert.class);
    ProcessDefinitionAssert processDefinitionAssert = assertThat(processDefinition);
    assertThat(processDefinitionAssert.getActual()).isSameAs(processDefinition);
  }

  @Test
  public void testAssertThat_ProcessInstance() throws Exception {
    // Given
    ProcessInstance processInstance = Mockito.mock(ProcessInstance.class);
    // When
    ProcessInstanceAssert returnedAssert = assertThat(processInstance);
    // Then
    assertThat(returnedAssert).isNotNull().isInstanceOf(ProcessInstanceAssert.class);
    ProcessInstanceAssert processInstanceAssert = assertThat(processInstance);
    assertThat(processInstanceAssert.getActual()).isSameAs(processInstance);
  }

  @Test
  public void testAssertThat_Task() throws Exception {
    // Given
    Task task = Mockito.mock(Task.class);
    // When
    TaskAssert returnedAssert = assertThat(task);
    // Then
    assertThat(returnedAssert).isNotNull().isInstanceOf(TaskAssert.class);
    TaskAssert taskAssert = assertThat(task);
    assertThat(taskAssert.getActual()).isSameAs(task);
  }

  @Test
  public void testAssertThat_Job() throws Exception {
    // Given
    Job job = Mockito.mock(Job.class);
    // When
    JobAssert returnedAssert = assertThat(job);
    // Then
    assertThat(returnedAssert).isNotNull().isInstanceOf(JobAssert.class);
    JobAssert jobAssert = assertThat(job);
    assertThat(jobAssert.getActual()).isSameAs(job);
  }

}
