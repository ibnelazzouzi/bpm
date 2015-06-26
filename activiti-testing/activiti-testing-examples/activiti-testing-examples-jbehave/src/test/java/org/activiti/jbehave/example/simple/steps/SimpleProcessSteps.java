package org.activiti.jbehave.example.simple.steps;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineTests.complete;
import static org.activiti.testing.assertions.ProcessEngineTests.task;
import static org.activiti.testing.assertions.ProcessEngineTests.withVariables;
import static org.activiti.testing.jbehave.support.ActivitiSupport.parseStatement;
import static org.mockito.Mockito.doThrow;

import javax.inject.Inject;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.mock.Mocks;
import org.activiti.jbehave.example.simple.unit.SimpleUnitTest;
import org.activiti.testing.examples.jbehave.simple.SimpleProcessAdapter;
import org.activiti.testing.examples.jbehave.simple.SimpleProcessConstants.Elements;
import org.activiti.testing.examples.jbehave.simple.SimpleProcessConstants.Events;
import org.activiti.testing.examples.jbehave.simple.SimpleProcessConstants.Variables;
import org.activiti.testing.jbehave.support.ActivitiSupport;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.mockito.Mockito;

/**
 * Specific process steps.
 * 
 * @author Simon Zambrovski, Holisticon AG.
 */
public class SimpleProcessSteps {

  @Inject
  private SimpleProcessAdapter simpleProcessAdapter;

  @Inject
  private ActivitiSupport support;

  @BeforeScenario
  public void initMocks() {
    Mocks.register(SimpleProcessAdapter.NAME, simpleProcessAdapter);
  }

  @AfterScenario
  public void resetMocks() {
    Mockito.reset(simpleProcessAdapter);
  }

  @Given("the contract $verb automatically processible")
  public void loadContractDataAutomatically(final String verb) {
    final boolean processingPossible = parseStatement("not", verb, false);

    SimpleUnitTest.mockLoadContract(simpleProcessAdapter, processingPossible);
  }

  @Given("the contract processing $verb")
  public void processingAutomatically(final String verb) {
    final boolean withErrors = parseStatement("succeeds", verb, false);
    if (withErrors) {
      doThrow(new BpmnError(Events.ERROR_PROCESS_AUTOMATICALLY_FAILED)).when(simpleProcessAdapter).processContract();
    }
  }

  @Then("the contract is loaded")
  public void contractIsLoaded() {
    final ProcessInstance instance = support.getProcessInstance();
    assertThat(instance).hasPassed(Elements.SERVICE_LOAD_CONTRACT_DATA);
  }

  @Then("the contract is processed automatically")
  public void contractIsProcessed() {
    assertThat(support.getProcessInstance()).hasPassed(Elements.SERVICE_PROCESS_CONTRACT_AUTOMATICALLY);
  }

  @Then("the contract processing is cancelled")
  public void cancelledProcessing() {
    assertThat(support.getProcessInstance()).hasPassed(Elements.SERVICE_CANCEL_PROCESSING);
  }

  @When("the contract is processed $withoutErrors")
  public void processManually(final String withoutErrors) {
    final boolean hasErrors = !parseStatement("with errors", withoutErrors, false);
    complete(task(), withVariables(Variables.ARE_PROCESSING_ERRORS_PRESENT, Boolean.valueOf(hasErrors)));
  }
}
