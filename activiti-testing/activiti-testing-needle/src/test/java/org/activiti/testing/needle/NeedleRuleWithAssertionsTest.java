package org.activiti.testing.needle;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineTests.claim;
import static org.activiti.testing.assertions.ProcessEngineTests.task;
import static org.activiti.testing.needle.ProcessEngineNeedleRule.fluentNeedleRule;

import javax.inject.Inject;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;
import org.activiti.testing.needle.ProcessEngineNeedleRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class NeedleRuleWithAssertionsTest {

  static {
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
  }

  @Rule
  public final ProcessEngineNeedleRule processEngineNeedleRule = fluentNeedleRule(this).build();

  @Inject
  private RuntimeService runtimeService;

  @Inject
  private TaskService taskService;

  @Test
  @Deployment(resources = "test-process.bpmn")
  public void waits_in_user_task_after_start() {
    ProcessInstance instance = runtimeService.startProcessInstanceByKey("test-process");

    assertThat(instance).isStarted().isWaitingAt("task_wait").task();

    claim(task(), "foo");
    assertThat(instance).task().isAssignedTo("foo");
  }
}
