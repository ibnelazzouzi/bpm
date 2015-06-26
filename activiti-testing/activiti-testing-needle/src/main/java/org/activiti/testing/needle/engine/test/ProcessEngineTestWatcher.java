package org.activiti.testing.needle.engine.test;

import java.util.Date;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.test.TestHelper;
import org.activiti.testing.needle.engine.ProcessEngineDelegate;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * Rewrite of the ProcessEngineRule since TestWatchman is deprecated for
 * junit>4.9.
 * 
 * @author Jan Galinski, Holisticon AG (jan.galinski@holisticon.de)
 */
public class ProcessEngineTestWatcher extends ChainedTestRule<ProcessEngineExternalResource, TestWatcher> implements ProcessEngineTestRule {

	/**
	 * Holds the deploymentId after execution of @Deployment. can be used to query
	 * the {@link RepositoryService}.
	 */
	protected String deploymentId;

	/**
	 * Like the old ProcessEngineRule ...
	 */
	private final TestWatcher innerRule;

	/**
	 * Use default engine.
	 */
	public ProcessEngineTestWatcher(ProcessEngineDelegate processEngineDelegate) {
		super(new ProcessEngineExternalResource(processEngineDelegate));

		this.innerRule = new TestWatcher() {
			@Override
			protected void starting(Description description) {
				deploymentId = TestHelper.annotationDeploymentSetUp(outerRule.getProcessEngine(), description.getTestClass(), description.getMethodName());
			}

			@Override
			protected void finished(Description description) {
				TestHelper.annotationDeploymentTearDown(outerRule.getProcessEngine(), deploymentId, description.getTestClass(), description.getMethodName());
				//ClockUtil.reset();
			}
		};
	}

	/**
	 * Create a new rule instance for given process engine.
	 * 
	 * @param processEngine
	 *          the process engine
	 */
	public ProcessEngineTestWatcher(final ProcessEngine processEngine) {
		this(new ProcessEngineDelegate(processEngine));
	}

	@Override
	protected TestWatcher innerRule() {
		return innerRule;
	}

	@Override
	public void setCurrentTime(final Date currentTime) {
		//ClockUtil.setCurrentTime(currentTime);
	}

	@Override
	public ProcessEngine getProcessEngine() {
		return outerRule.getProcessEngine();
	}

	@Override
	public String getDeploymentId() {
		return deploymentId;
	}

	@Override
	public RepositoryService getRepositoryService() {
		return outerRule.getRepositoryService();
	}

	@Override
	public RuntimeService getRuntimeService() {
		return outerRule.getRuntimeService();
	}

	@Override
	public FormService getFormService() {
		return outerRule.getFormService();
	}

	@Override
	public TaskService getTaskService() {
		return outerRule.getTaskService();
	}

	@Override
	public HistoryService getHistoryService() {
		return outerRule.getHistoryService();
	}

	@Override
	public IdentityService getIdentityService() {
		return outerRule.getIdentityService();
	}

	@Override
	public ManagementService getManagementService() {
		return outerRule.getManagementService();
	}

	@Override
	public String getName() {
		return outerRule.getName();
	}

	@Override
	public void close() {
		outerRule.close();

	}

	@Override
	public ProcessEngineConfiguration getProcessEngineConfiguration() {
		return outerRule.getProcessEngineConfiguration();
	}

	//  @Override
	//  public AuthorizationService getAuthorizationService() {
	//    return outerRule.getAuthorizationService();
	//  }

}
