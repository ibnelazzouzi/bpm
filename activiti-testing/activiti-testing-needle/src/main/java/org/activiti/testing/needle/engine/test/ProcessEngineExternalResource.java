package org.activiti.testing.needle.engine.test;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.testing.needle.engine.ProcessEngineDelegate;
import org.junit.rules.ExternalResource;

/**
 * JUnit {@link org.junit.rules.ExternalResource} that provides process engine
 * services.
 * <p/>
 * Can be initialized in three ways:
 * <ol>
 * <li><b>Lazy</b> with a ProcessEngineConfiguration, the engine is created when
 * the test is started. Before the test is started, access to any of the
 * services results in an IllegalStateException.</li>
 * <li><b>Eager</b> with a ProcessEngineConfiguration, the engine is initialized
 * immediately</li>
 * <li><b>Delegate</b> processEngine is initialized externally and just
 * decorated by the resource. Useful when chaining rules.</li>
 * </ol>
 */
public class ProcessEngineExternalResource extends ExternalResource implements ProcessEngine {

	protected final ProcessEngineDelegate delegate;

	public ProcessEngineExternalResource(final ProcessEngineDelegate delegate) {
		this.delegate = delegate;
	}

	public ProcessEngineExternalResource(final ProcessEngineConfiguration processEngineConfiguration) {
		this(new ProcessEngineDelegate(processEngineConfiguration));
	}

	public ProcessEngineExternalResource(final ProcessEngineConfiguration processEngineConfiguration, boolean initialize) {
		this(new ProcessEngineDelegate(processEngineConfiguration, initialize));
	}

	/**
	 *
	 * @param processEngine
	 *          the internally wrapped process engine
	 */
	public ProcessEngineExternalResource(final ProcessEngine processEngine) {
		this(new ProcessEngineDelegate(processEngine));
	}

	@Override
	protected void before() throws Throwable {
		super.before();
		delegate.initProcessEngine();
	}

	@Override
	protected void after() {
		super.after();
		delegate.closeProcessEngine();
	}

	/**
	 * @return the internally wrapped engine.
	 */
	public ProcessEngine getProcessEngine() {
		return delegate.getProcessEngine();
	}

	@Override
	public RepositoryService getRepositoryService() {
		return delegate.getRepositoryService();
	}

	@Override
	public RuntimeService getRuntimeService() {
		return delegate.getRuntimeService();
	}

	@Override
	public FormService getFormService() {
		return delegate.getFormService();
	}

	@Override
	public TaskService getTaskService() {
		return delegate.getTaskService();
	}

	@Override
	public HistoryService getHistoryService() {
		return delegate.getHistoryService();
	}

	@Override
	public IdentityService getIdentityService() {
		return delegate.getIdentityService();
	}

	@Override
	public ManagementService getManagementService() {
		return delegate.getManagementService();
	}

	//  @Override
	//  public AuthorizationService getAuthorizationService() {
	//    return delegate.getAuthorizationService();
	//  }

	protected ProcessEngineDelegate getProcessEngineDelegate() {
		return delegate;
	}

	@Override
	public ProcessEngineConfiguration getProcessEngineConfiguration() {
		return delegate.getProcessEngineConfiguration();
	}

	@Override
	public String getName() {
		return delegate.getName();
	}

	@Override
	public void close() {
		delegate.close();
	}

}
