package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
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
public class TaskAssertHasNameTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @Test
  @Deployment(resources = {
    "TaskAssert-hasName.bpmn"
  })
  public void testHasName_Success() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasName"
    );
    // Then
    assertThat(processInstance).task().hasName("name");
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasName.bpmn"
  })
  public void testHasName_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasName"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().hasName("otherName");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "TaskAssert-hasName.bpmn"
  })
  public void testHasName_Null_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "TaskAssert-hasName"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).task().hasName(null);
      }
    });
  }

}
