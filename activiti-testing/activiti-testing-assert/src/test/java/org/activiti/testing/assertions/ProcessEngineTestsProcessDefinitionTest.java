package org.activiti.testing.assertions;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineAssertions.reset;
import static org.activiti.testing.assertions.ProcessEngineTests.complete;
import static org.activiti.testing.assertions.ProcessEngineTests.processDefinition;
import static org.activiti.testing.assertions.ProcessEngineTests.processDefinitionQuery;
import static org.activiti.testing.assertions.ProcessEngineTests.runtimeService;
import static org.activiti.testing.assertions.ProcessEngineTests.task;
import static org.assertj.core.api.Assertions.assertThat;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.activiti.testing.assertions.helpers.Failure;
import org.activiti.testing.assertions.helpers.ProcessAssertTestCase;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Martin Schimak <martin.schimak@plexiti.com>
 */
public class ProcessEngineTestsProcessDefinitionTest extends ProcessAssertTestCase {

  @Rule
  public ActivitiRule processEngineRule = new ActivitiRule();

  @After
  public void tearDown() {
    reset();
  }

  @Test
  public void testProcessDefinition_No_Definition() {
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        processDefinition();
      }
    }, IllegalStateException.class);
  }

  @Test
  public void testProcessDefinition_No_Definition_Via_ProcessDefinitionKey() {
    // Then
    assertThat(processDefinition("nonExistingKey")).isNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-processDefinition.bpmn"
  })
  public void testProcessDefinition_One_Definition() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-processDefinition"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        processDefinition();
      }
    }, IllegalStateException.class);
    // When
    assertThat(processInstance).isNotNull();
    // Then
    assertThat(processDefinition()).isNotNull();
    // And
    assertThat(processDefinition().getId())
      .isEqualTo(processInstance.getProcessDefinitionId());
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-processDefinition.bpmn"
  })
  public void testProcessDefinition_One_Definition_Via_ProcessInstance() {
    // When
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-processDefinition"
    );
    // Then
    assertThat(processDefinition(processInstance)).isNotNull();
    // And
    assertThat(processDefinition(processInstance).getId())
      .isEqualTo(processInstance.getProcessDefinitionId());
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-processDefinition.bpmn"
  })
  public void testProcessDefinition_One_Definition_Via_ProcessDefinitionKey() {
    // When
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-processDefinition"
    );
    // Then
    assertThat(processDefinition("ProcessEngineTests-processDefinition")).isNotNull();
    // And
    assertThat(processDefinition("ProcessEngineTests-processDefinition").getId())
      .isEqualTo(processInstance.getProcessDefinitionId());
    // And
    assertThat(processDefinition("nonExistingKey")).isNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-processDefinition.bpmn"
  })
  public void testProcessDefinition_One_Definition_Via_ProcessDefinitionQuery() {
    // When
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-processDefinition"
    );
    // Then
    assertThat(processDefinition(processDefinitionQuery())).isNotNull();
    // And
    assertThat(processDefinition(processDefinitionQuery()).getId())
      .isEqualTo(processInstance.getProcessDefinitionId());
    // And
    assertThat(processDefinition(processDefinitionQuery().processDefinitionKey("nonExistingKey"))).isNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-processDefinition.bpmn"
  })
  public void testProcessDefinition_One_Definition_Instance_Ended() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-processDefinition"
    );
    // And
    assertThat(processInstance).isNotNull();
    // When
    complete(task());
    // Then
    assertThat(processDefinition()).isNotNull();
    // And
    assertThat(processDefinition().getId())
      .isEqualTo(processInstance.getProcessDefinitionId());
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-processDefinition.bpmn", "ProcessEngineTests-processDefinition2.bpmn"
  })
  public void testProcessDefinition_Two_Definitions() {
    // Given
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-processDefinition2"
    );
    // When
    assertThat(processInstance).isNotNull();
    // Then
    assertThat(processDefinition()).isNotNull();
    // And
    assertThat(processDefinition().getId())
      .isEqualTo(processInstance.getProcessDefinitionId());
    // And
    assertThat(processDefinition().getKey())
      .isEqualTo("ProcessEngineTests-processDefinition2");
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-processDefinition.bpmn", "ProcessEngineTests-processDefinition2.bpmn"
  })
  public void testProcessDefinition_Two_Definitions_Via_ProcessInstance() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-processDefinition"
    );
    // And
    final ProcessInstance processInstance2 = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-processDefinition2"
    );
    // Then
    assertThat(processDefinition(processInstance)).isNotNull();
    // And
    assertThat(processDefinition(processInstance2)).isNotNull();
    // And
    assertThat(processDefinition(processInstance).getKey())
      .isEqualTo("ProcessEngineTests-processDefinition");
    // And
    assertThat(processDefinition(processInstance2).getKey())
      .isEqualTo("ProcessEngineTests-processDefinition2");
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-processDefinition.bpmn", "ProcessEngineTests-processDefinition2.bpmn"
  })
  public void testProcessDefinition_Two_Definitions_Via_ProcessDefinitionKey() {
    // Given
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-processDefinition"
    );
    // And
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-processDefinition2"
    );
    // Then
    assertThat(processDefinition("ProcessEngineTests-processDefinition")).isNotNull();
    // And
    assertThat(processDefinition("ProcessEngineTests-processDefinition2")).isNotNull();
    // And
    assertThat(processDefinition("ProcessEngineTests-processDefinition").getKey())
      .isEqualTo("ProcessEngineTests-processDefinition");
    // And
    assertThat(processDefinition("ProcessEngineTests-processDefinition2").getKey())
      .isEqualTo("ProcessEngineTests-processDefinition2");
    // And
    assertThat(processDefinition("nonExistingKey")).isNull();
  }

  @Test
  @Deployment(resources = {
    "ProcessEngineTests-processDefinition.bpmn", "ProcessEngineTests-processDefinition2.bpmn"
  })
  public void testProcessDefinition_Two_Definitions_Via_ProcessDefinitionQuery() {
    // Given
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-processDefinition"
    );
    // And
    runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-processDefinition2"
    );
    // Then
    assertThat(processDefinition(processDefinitionQuery().processDefinitionKey("ProcessEngineTests-processDefinition"))).isNotNull();
    // And
    assertThat(processDefinition(processDefinitionQuery().processDefinitionKey("ProcessEngineTests-processDefinition2"))).isNotNull();
    // And
    assertThat(processDefinition(processDefinitionQuery().processDefinitionKey("ProcessEngineTests-processDefinition")).getKey())
      .isEqualTo("ProcessEngineTests-processDefinition");
    // And
    assertThat(processDefinition(processDefinitionQuery().processDefinitionKey("ProcessEngineTests-processDefinition2")).getKey())
      .isEqualTo("ProcessEngineTests-processDefinition2");
    // And
    assertThat(processDefinition("nonExistingKey")).isNull();
    // And
    expect(new Failure() {
      @Override
      public void when() {
        processDefinition(processDefinitionQuery());
      }
    }, ActivitiException.class);
  }

}
