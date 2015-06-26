package org.activiti.jbehave.example.simple.unit;

import static org.activiti.testing.assertions.ProcessEngineAssertions.assertThat;
import static org.activiti.testing.assertions.ProcessEngineTests.complete;
import static org.activiti.testing.assertions.ProcessEngineTests.withVariables;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;

import java.util.UUID;

import javax.inject.Inject;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.activiti.engine.test.mock.Mocks;
import org.activiti.testing.examples.jbehave.simple.SimpleProcessAdapter;
import org.activiti.testing.examples.jbehave.simple.SimpleProcessConstants;
import org.activiti.testing.jbehave.bdd.Slf4jLoggerRule;
import org.activiti.testing.needle.ProcessEngineNeedleRule;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit test of simple process.
 *
 * @author Simon Zambrovski, Holisticon AG.
 */
public class SimpleUnitTest {

	static {
		Slf4jLoggerRule.DEFAULT.before();
	}

	@Rule
	public ProcessEngineNeedleRule processEngine = ProcessEngineNeedleRule.fluentNeedleRule(this).build();

	@Inject
	private SimpleProcessAdapter simpleProcessAdapter;

	private ProcessInstance processInstance;

	public static void mockLoadContract(final SimpleProcessAdapter mock, final boolean isAutomatically) {
		doAnswer(invocation -> {
			final DelegateExecution execution = (DelegateExecution) invocation.getArguments()[0];

			// set contract id to random uuid
			execution.setVariable(SimpleProcessConstants.Variables.CONTRACT_ID, UUID.randomUUID().toString());

			return isAutomatically;
		}).when(mock).loadContractData(any(DelegateExecution.class));

	}

	/**
	 * Glue code in order to separate all application control code from test
	 * assertions.
	 */
	class Glue {

		public void loadContractData(final boolean isAutomatically) {
			mockLoadContract(SimpleUnitTest.this.simpleProcessAdapter, isAutomatically);
		}

		public void startSimpleProcess() {
			SimpleUnitTest.this.processInstance = SimpleUnitTest.this.processEngine.getRuntimeService().startProcessInstanceByKey(SimpleProcessConstants.PROCESS);
			System.out.println("Process : " + SimpleUnitTest.this.processInstance.getId());
			assertThat(SimpleUnitTest.this.processInstance).isNotNull();
		}

		public void processAutomatically(final boolean withErrors) {
			if (withErrors) {
				doThrow(new BpmnError(SimpleProcessConstants.Events.ERROR_PROCESS_AUTOMATICALLY_FAILED)).when(SimpleUnitTest.this.simpleProcessAdapter).processContract();
			}
		}

		/**
		 * Assert process end event.
		 *
		 * @param name
		 *          name of the end event.
		 */
		private void assertEndEvent(final String name) {
			assertThat(SimpleUnitTest.this.processInstance).hasPassed(name).isEnded();
		}

		public void waitsInManualProcessing() {
			assertThat(SimpleUnitTest.this.processInstance).isWaitingAt(SimpleProcessConstants.Elements.TASK_PROCESS_MANUALLY);
		}

		/**
		 * Process manually.
		 *
		 * @param withErrors
		 */
		public void processManually(final boolean withErrors) {
			final Task task = SimpleUnitTest.this.processEngine.getTaskService().createTaskQuery().processInstanceId(SimpleUnitTest.this.processInstance.getId()).singleResult();
			complete(task, withVariables("processingErrorsPresent", Boolean.valueOf(withErrors)));
		}

	}

	private final Glue glue = new Glue();

	@Before
	public void initMocks() {
		Mocks.register(SimpleProcessAdapter.NAME, this.simpleProcessAdapter);
	}

	@Test
	@Deployment(resources = SimpleProcessConstants.BPMN)
	public void shouldDeploy() {
		// nothing to do.
	}

	@Test
	@Deployment(resources = SimpleProcessConstants.BPMN)
	public void shouldStartAndWaitForManual() {

		// given
		this.glue.loadContractData(false);

		// when
		this.glue.startSimpleProcess();

		// then
		this.glue.waitsInManualProcessing();
	}

	@Test
	@Ignore
	@Deployment(resources = SimpleProcessConstants.BPMN)
	public void shouldStartProcessAutomaticallyAndWaitForManual() {

		// given
		this.glue.loadContractData(true);
		this.glue.processAutomatically(true);

		// when
		this.glue.startSimpleProcess();

		// then
		this.glue.waitsInManualProcessing();
	}

	@Test
	@Deployment(resources = SimpleProcessConstants.BPMN)
	public void shouldStartAndRunAutomatically() {

		// given
		this.glue.loadContractData(true);
		this.glue.processAutomatically(false);

		// when
		this.glue.startSimpleProcess();

		// then
		this.glue.assertEndEvent(SimpleProcessConstants.Events.EVENT_CONTRACT_PROCESSED);
	}

	@Test
	@Deployment(resources = SimpleProcessConstants.BPMN)
	public void shouldProcessContractManuallySuccessfully() {
		// given
		this.glue.loadContractData(false);
		this.glue.startSimpleProcess();
		this.glue.waitsInManualProcessing();

		// when
		this.glue.processManually(false);

		// then
		this.glue.assertEndEvent(SimpleProcessConstants.Events.EVENT_CONTRACT_PROCESSED);
	}

	@Test
	@Deployment(resources = SimpleProcessConstants.BPMN)
	public void shouldProcessContractManuallyAndCancel() {
		// given
		this.glue.loadContractData(false);
		this.glue.startSimpleProcess();
		this.glue.waitsInManualProcessing();

		// when
		this.glue.processManually(true);

		// then
		this.glue.assertEndEvent(SimpleProcessConstants.Events.EVENT_CONTRACT_PROCESSING_CANCELLED);
	}

}
