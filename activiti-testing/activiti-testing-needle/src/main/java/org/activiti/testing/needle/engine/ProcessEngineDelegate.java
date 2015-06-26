package org.activiti.testing.needle.engine;

import static com.google.common.base.Preconditions.checkState;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.testing.needle.engine.test.function.GetProcessEngineConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds a process engine delegate and provides process engine services.
 *
 * @author Jan Galinski, Holisticon AG
 */
public class ProcessEngineDelegate implements ProcessEngine {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private ProcessEngine processEngine;
	private ProcessEngineConfiguration processEngineConfiguration;

	public ProcessEngineDelegate(ProcessEngineConfiguration processEngineConfiguration) {
		this(processEngineConfiguration, false);
	}

	public ProcessEngineDelegate(ProcessEngineConfiguration processEngineConfiguration, boolean eagerInitialization) {
		this(null, processEngineConfiguration, eagerInitialization);
	}

	public ProcessEngineDelegate(ProcessEngine processEngine) {
		this(processEngine, null, false);
	}

	private ProcessEngineDelegate(ProcessEngine processEngine, ProcessEngineConfiguration processEngineConfiguration, boolean eagerInitialization) {
		if (processEngine != null) {
			this.processEngine = processEngine;
			this.processEngineConfiguration = GetProcessEngineConfiguration.INSTANCE.apply(processEngine);
			return;
		} else if (processEngineConfiguration != null) {
			this.processEngineConfiguration = processEngineConfiguration;
			if (eagerInitialization) {
				initProcessEngine();
			}
			return;
		}
		throw new IllegalArgumentException("either processEngine or processEngineConfiguration must be provided for creation!");
	}

	public ProcessEngine initProcessEngine() {
		if (isInitialized()) {
			logger.info("process engine is already initialized, skipping creation.");
			return null;
		}

		return processEngine = processEngineConfiguration.buildProcessEngine();
	}

	public void closeProcessEngine() {
		assertProcessEngineInitialized();
		processEngine.close();
		this.processEngine = null;
	}

	@Override
	public RepositoryService getRepositoryService() {
		assertProcessEngineInitialized();
		return processEngine.getRepositoryService();
	}

	@Override
	public RuntimeService getRuntimeService() {
		assertProcessEngineInitialized();
		return processEngine.getRuntimeService();
	}

	@Override
	public FormService getFormService() {
		assertProcessEngineInitialized();
		return processEngine.getFormService();
	}

	@Override
	public TaskService getTaskService() {
		assertProcessEngineInitialized();
		return processEngine.getTaskService();
	}

	@Override
	public HistoryService getHistoryService() {
		assertProcessEngineInitialized();
		return processEngine.getHistoryService();
	}

	@Override
	public IdentityService getIdentityService() {
		assertProcessEngineInitialized();
		return processEngine.getIdentityService();
	}

	@Override
	public ManagementService getManagementService() {
		assertProcessEngineInitialized();
		return processEngine.getManagementService();
	}
	
	@Override
	public ProcessEngineConfiguration getProcessEngineConfiguration() {
		return processEngineConfiguration;
	}

	@Override
	public String getName() {
		assertProcessEngineInitialized();
		return processEngine.getName();
	}

	@Override
	public void close() {
		assertProcessEngineInitialized();
		processEngine.close();
	}

	//  @Override
	//  public AuthorizationService getAuthorizationService() {
	//    assertProcessEngineInitialized();
	//    return processEngine.getAuthorizationService();
	//  }

	/**
	 * @return <code>true</code> if the inner process engine is not
	 *         <code>null</code>, else <code>false</code>.
	 */
	boolean isInitialized() {
		return processEngine != null;
	}

	/**
	 * @throws java.lang.IllegalStateException
	 *           when inner processEngine was not initialized.
	 */
	private void assertProcessEngineInitialized() {
		checkState(isInitialized(), "processEngine has not been initialized.");
	}

	public ProcessEngine getProcessEngine() {
		return processEngine;
	}

	
	
	
		   
}
