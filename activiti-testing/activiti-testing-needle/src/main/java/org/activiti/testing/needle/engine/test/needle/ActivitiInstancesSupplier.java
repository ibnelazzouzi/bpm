package org.activiti.testing.needle.engine.test.needle;

import static com.google.common.base.Preconditions.checkArgument;
import static org.needle4j.injection.InjectionProviders.providerForInstance;

import java.util.Set;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.testing.needle.engine.test.function.GetProcessEngineConfiguration;
import org.needle4j.injection.InjectionProvider;
import org.needle4j.injection.InjectionProviderInstancesSupplier;

import com.google.common.collect.Sets;

/**
 * Supplier for activiti services. Holds processEngine internally and exposes all
 * services via {@link InjectionProvider}.
 * 
 * @author Jan Galinski, Holisticon AG
 */
public class ActivitiInstancesSupplier implements InjectionProviderInstancesSupplier, ProcessEngine {

	private final Set<InjectionProvider<?>> providers = Sets.newHashSet();

	private final ProcessEngine processEngine;

	public ActivitiInstancesSupplier(final ProcessEngine processEngine) {
		checkArgument(processEngine != null);
		this.processEngine = processEngine;

		providers.add(providerForInstance(processEngine));
		providers.add(providerForInstance(getRepositoryService()));
		providers.add(providerForInstance(getRuntimeService()));
		providers.add(providerForInstance(getFormService()));
		providers.add(providerForInstance(getTaskService()));
		providers.add(providerForInstance(getHistoryService()));
		providers.add(providerForInstance(getIdentityService()));
		providers.add(providerForInstance(getManagementService()));
		providers.add(providerForInstance(getCommandExecutor()));
		providers.add(providerForInstance(getProcessEngineConfiguration()));
	}

	@Override
	public Set<InjectionProvider<?>> get() {
		return providers;
	}

	@Override
	public RepositoryService getRepositoryService() {
		return processEngine.getRepositoryService();
	}

	@Override
	public RuntimeService getRuntimeService() {
		return processEngine.getRuntimeService();
	}

	@Override
	public FormService getFormService() {
		return processEngine.getFormService();
	}

	@Override
	public TaskService getTaskService() {
		return processEngine.getTaskService();
	}

	@Override
	public HistoryService getHistoryService() {
		return processEngine.getHistoryService();
	}

	@Override
	public IdentityService getIdentityService() {
		return processEngine.getIdentityService();
	}

	@Override
	public ManagementService getManagementService() {
		return processEngine.getManagementService();
	}

	/**
	 * @return the current configuration
	 */
	public ProcessEngineConfiguration getProcessEngineConfiguration() {
		return GetProcessEngineConfiguration.INSTANCE.apply(processEngine);
	}

	/**
	 * @return current process engine
	 */
	public ProcessEngine getProcessEngine() {
		return processEngine;
	}

	/**
	 * @return a command executor for the processEngine
	 */
	public CommandExecutor getCommandExecutor() {
		return ((ProcessEngineImpl) getProcessEngine()).getProcessEngineConfiguration().getCommandExecutor();
	}

	@Override
	public String getName() {
		return processEngine.getName();
	}

	@Override
	public void close() {
		processEngine.close();

	}

}
