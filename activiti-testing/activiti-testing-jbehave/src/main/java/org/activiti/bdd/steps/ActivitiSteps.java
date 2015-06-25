package org.activiti.bdd.steps;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;

import javax.inject.Inject;

import org.activiti.engine.test.mock.Mocks;
import org.activiti.test.ActivitiSupport;
import org.activiti.testing.assertions.ProcessEngineAssertions;
import org.jbehave.core.annotations.AfterStory;
import org.jbehave.core.annotations.BeforeStory;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic Steps to control Camunda BPM from the test.
 *
 * @author Simon Zambrovski, Holisticon AG.
 */
public class ActivitiSteps {

	private static final Logger LOG = LoggerFactory.getLogger(ActivitiSteps.class);

	@Inject
	private ActivitiSupport support;

	@BeforeStory
	public void init() {
		LOG.debug("Initializing before a story run.");
		ProcessEngineAssertions.init(this.support.getProcessEngine());
	}

	/**
	 * Clean up all resources.
	 */
	@AfterStory(uponGivenStory = false)
	public void cleanUp() {
		LOG.debug("Cleaning up after story run.");
		Mocks.reset();
		//this.support.undeploy();
		this.support.resetClock();
		ProcessEngineAssertions.reset();
	}

	@When("the process definition $processDefinition")
	@Given("the process definition $processDefinition")
	public void deployProcess(final String processDefinition) {
		this.support.deploy(processDefinition);
	}

	@When("the process $processKey is started")
	public void startProcess(final String processKey) {
		this.support.startProcessInstanceByKey(processKey);
	}

	/**
	 * Process is finished.
	 */
	@Then("the process is finished")
	public void processIsFinished() {
		assertThat(this.support.getProcessInstance()).isEnded();
	}

	@Then("the process is finished with event $eventName")
	public void processFinishedSucessfully(final String eventName) {
		this.processIsFinished();
		assertThat(this.support.getProcessInstance()).hasPassed(eventName);
	}

	/**
	 * Process step reached.
	 *
	 * @param activityId
	 *          name of the step to reach.
	 */
	@Then("the step $activityId is reached")
	@When("the step $activityId is reached")
	public void stepIsReached(final String activityId) {
		assertThat(this.support.getProcessInstance()).isWaitingAt(activityId);
		LOG.debug("Step {} reached.", activityId);
	}
}
