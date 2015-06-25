package org.activiti.test;

import static com.google.common.base.Preconditions.checkArgument;
import static org.activiti.engine.test.cfg.MostUsefulProcessEngineConfiguration.mostUsefulProcessEngineConfiguration;
import static org.needle4j.injection.InjectionProviders.providerForInstance;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.mock.Mocks;
import org.needle4j.injection.InjectionProvider;
import org.needle4j.injection.InjectionTargetInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

//import org.activiti.engine.impl.util.ClockUtil;

/**
 * Helper for Camunda access.
 *
 * @author Simon Zambrovski, Holisticon AG
 */
public class ActivitiSupport implements InjectionProvider<ActivitiSupport> {

	private final Logger logger = LoggerFactory.getLogger(ActivitiSupport.class);
	private final InjectionProvider<ActivitiSupport> injectionProviderDelegate = providerForInstance(this);
	private final Set<String> deploymentIds = Sets.newHashSet();

	private ProcessEngine processEngine;
	private ProcessInstance processInstance;
	private Date startTime;

	/**
	 * Create support component with default process engine.
	 */
	public ActivitiSupport() {
		this(mostUsefulProcessEngineConfiguration().buildProcessEngine());
	}

	/**
	 * Create support component.
	 *
	 * @param processEngine
	 *          process engine.
	 */
	public ActivitiSupport(final ProcessEngine processEngine) {
		this.processEngine = processEngine;
		this.logger.debug("Camunda Support created.");
	}

	/**
	 * Checks deployment of the process definition.
	 *
	 * @param processModelResources
	 *          process definition file (BPMN)
	 */
	public void deploy(final String... processModelResources) {
		final DeploymentBuilder deploymentBuilder = this.processEngine.getRepositoryService().createDeployment();
		for (final String resource : processModelResources) {
			deploymentBuilder.addClasspathResource(resource);
		}
		this.deploymentIds.add(deploymentBuilder.deploy().getId());
		this.getStartTime();
	}

	/**
	 * Cleans up resources.
	 */
	public void undeploy() {
		for (final String deploymentId : this.deploymentIds) {
			this.deploymentIds.remove(deploymentId);
			this.processEngine.getRepositoryService().deleteDeployment(deploymentId, true);
		}
		Mocks.reset();
	}

	/**
	 * Starts process by process definition key with given payload.
	 *
	 * @param processDefinitionKey
	 *          process definition keys.
	 * @param variables
	 *          maps of initial payload variables.
	 * @return process instance id
	 * @see RuntimeService#startProcessInstanceByKey(String, Map)
	 */
	public ProcessInstance startProcessInstanceByKey(final String processDefinitionKey, final Map<String, Object> variables) {
		checkArgument(processDefinitionKey != null, "processDefinitionKey must not be null!");
		this.processInstance = this.processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey, variables);
		return this.processInstance;
	}

	/**
	 * Starts process by process definition key.
	 *
	 * @param processDefinitionKey
	 *          process definition keys.
	 * @return process instance id
	 */
	public ProcessInstance startProcessInstanceByKey(final String processDefinitionKey) {
		return this.startProcessInstanceByKey(processDefinitionKey, null);
	}

	/**
	 * Retrieves the process instance.
	 *
	 * @return running process instance.
	 */
	public ProcessInstance getProcessInstance() {
		return this.processInstance;
	}

	/**
	 * Sets time.
	 *
	 * @param currentTime
	 *          sets current time in the engine
	 */
	public void setCurrentTime(final Date currentTime) {
		//ClockUtil.setCurrentTime(currentTime);
	}

	/**
	 * Resets process engine clock.
	 */
	public void resetClock() {
		// ClockUtil.reset();
	}

	/**
	 * Retrieves process engine.
	 *
	 * @return process engine.
	 */
	public ProcessEngine getProcessEngine() {
		return this.processEngine;
	}

	/**
	 * Retrieves start time.
	 *
	 * @return time of deployment.
	 */
	public Date getStartTime() {
		if (this.startTime == null) {
			this.startTime = new Date();
		}
		return this.startTime;
	}

	/**
	 * Parses the verb and maps it to a boolean decision.
	 *
	 * @param negation
	 *          a way how the verb is negated. (e.G. not)
	 * @param value
	 *          part of text containing the verb in regular or negated form.
	 * @param defaultValue
	 *          default value, if parsing fails.
	 * @return true, if negation not found.
	 */
	public static boolean parseStatement(final String negation, final String value, final boolean defaultValue) {
		return value != null ? !value.contains(negation) : defaultValue;
	}

	/**
	 * Checks whether current process instance is still running.
	 *
	 * @return true, if process instance is running
	 */
	public boolean hasRunningProcessInstance() {
		return this.getProcessInstance() != null;
	}

	@Override
	public ActivitiSupport getInjectedObject(final Class<?> injectionPointType) {
		return this.injectionProviderDelegate.getInjectedObject(injectionPointType);
	}

	@Override
	public Object getKey(final InjectionTargetInformation injectionTargetInformation) {
		return this.injectionProviderDelegate.getKey(injectionTargetInformation);
	}

	@Override
	public boolean verify(final InjectionTargetInformation injectionTargetInformation) {
		return this.injectionProviderDelegate.verify(injectionTargetInformation);
	}
}
