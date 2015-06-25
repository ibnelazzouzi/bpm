package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineTests.runtimeService;
import static org.activiti.testing.assertions.ProcessEngineTests.withVariables;

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
public class ProcessInstanceAssertVariablesTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-variables.bpmn"
  })
  public void testVariables_One_Success() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-variables", withVariables("aVariable", "aValue")
    );
    // Then
    assertThat(processInstance).variables().containsEntry("aVariable", "aValue");
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-variables.bpmn"
  })
  public void testVariables_One_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-variables", withVariables("aVariable", "aValue")
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).variables().containsKey("anotherVariable");
      }
    });
    // And
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).variables().containsKeys("aVariable", "anotherVariable");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-variables.bpmn"
  })
  public void testVariables_Two_Success() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-variables", withVariables("firstVariable", "firstValue", "secondVariable", "secondValue")
    );
    // Then
    assertThat(processInstance).variables();
    // And
    assertThat(processInstance).variables().containsKey("firstVariable");
    // And
    assertThat(processInstance).variables().containsKey("secondVariable");
    // And
    assertThat(processInstance).variables().containsKeys("firstVariable", "secondVariable");
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-variables.bpmn"
  })
  public void testVariables_Two_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-variables", withVariables("firstVariable", "firstValue", "secondVariable", "secondValue")
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).variables().containsKey("anotherVariable");
      }
    });
    // And
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).variables().containsKeys("firstVariable", "anotherVariable");
      }
    });
    // And
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).variables().containsKeys("secondVariable", "anotherVariable");
      }
    });
    // And
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).variables().containsKeys("firstVariable", "secondVariable", "anotherVariable");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-variables.bpmn"
  })
  public void testVariables_None_Success() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-variables"
    );
    // Then
    assertThat(processInstance).variables().isEmpty();
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-variables.bpmn"
  })
  public void testVariables_None_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-variables"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).variables().isNotEmpty();
      }
    });
    // And
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).variables().containsKey("aVariable");
      }
    });
  }

}
