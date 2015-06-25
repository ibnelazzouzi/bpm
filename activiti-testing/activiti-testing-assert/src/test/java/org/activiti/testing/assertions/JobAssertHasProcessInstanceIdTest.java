package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineTests.jobQuery;
import static org.activiti.testing.assertions.ProcessEngineTests.runtimeService;

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
public class JobAssertHasProcessInstanceIdTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @Test
  @Deployment(resources = {
    "JobAssert-hasProcessInstanceId.bpmn"
  })
  public void testHasProcessInstanceId_Success() {
    // When
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "JobAssert-hasProcessInstanceId"
    );
    // Then
    assertThat(jobQuery().singleResult()).isNotNull();
    // And
    assertThat(jobQuery().singleResult()).hasProcessInstanceId(processInstance.getProcessInstanceId());
  }

  @Test
  @Deployment(resources = {
    "JobAssert-hasProcessInstanceId.bpmn"
  })
  public void testHasProcessInstanceId_Failure() {
    // When
    runtimeService().startProcessInstanceByKey(
      "JobAssert-hasProcessInstanceId"
    );
    // Then
    assertThat(jobQuery().singleResult()).isNotNull();
    // And
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(jobQuery().singleResult()).hasProcessInstanceId("someOtherId");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "JobAssert-hasProcessInstanceId.bpmn"
  })
  public void testHasProcessInstanceId_Error_Null() {
    // When
    runtimeService().startProcessInstanceByKey(
      "JobAssert-hasProcessInstanceId"
    );
    // Then
    assertThat(jobQuery().singleResult()).isNotNull();
    // And
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(jobQuery().singleResult()).hasProcessInstanceId(null);
      }
    });
  }

}
