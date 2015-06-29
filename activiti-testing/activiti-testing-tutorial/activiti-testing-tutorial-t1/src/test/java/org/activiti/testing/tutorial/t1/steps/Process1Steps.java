package org.activiti.testing.tutorial.t1.steps;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineTests.complete;
import static org.activiti.testing.assertions.ProcessEngineTests.task;
import static org.activiti.testing.assertions.ProcessEngineTests.withVariables;

import javax.inject.Inject;

import org.activiti.engine.test.mock.Mocks;
import org.activiti.testing.jbehave.support.ActivitiSupport;
import org.activiti.testing.tutorial.t1.Process1Adapter;
import org.activiti.testing.tutorial.t1.Process1Constants;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.mockito.Mockito;

public class Process1Steps {

	@Inject
	private Process1Adapter process1Adapter;

	@Inject
	private ActivitiSupport support;

	@BeforeScenario
	public void initMocks() {
		Mocks.register(Process1Adapter.NAME, this.process1Adapter);
	}

	@AfterScenario
	public void resetMocks() {
		Mockito.reset(this.process1Adapter);
	}

	@When("the usertask1 is processed with decision $decision")
	public void processUserTask1(final String decision) {
		complete(task(), withVariables(Process1Constants.Variables.DECISION, decision));
	}

	@Then("the $taskId is processed")
	public void servicetask1Processed(final String taskId) {
		assertThat(this.support.getProcessInstance()).hasPassed(taskId);
	}
}
