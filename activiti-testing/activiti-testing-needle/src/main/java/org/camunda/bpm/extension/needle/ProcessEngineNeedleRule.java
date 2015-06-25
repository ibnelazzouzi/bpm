package org.camunda.bpm.extension.needle;

import static org.needle4j.junit.NeedleBuilders.needleMockitoTestRule;

import java.util.Date;
import java.util.Set;

import javax.sql.DataSource;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.test.ChainedTestRule;
import org.activiti.engine.test.ProcessEngineTestRule;
import org.activiti.engine.test.ProcessEngineTestWatcher;
import org.activiti.engine.test.function.GetProcessEngineConfiguration;
import org.activiti.engine.test.needle.CamundaInstancesSupplier;
import org.activiti.testing.assertions.ProcessEngineAssertions;
import org.junit.rules.RuleChain;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.needle4j.injection.InjectionProvider;
import org.needle4j.injection.InjectionProviderInstancesSupplier;
import org.needle4j.injection.InjectionProviders;
import org.needle4j.junit.testrule.NeedleTestRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Combines the {@link org.activiti.engine.test.ProcessEngineRule} and the
 * {@link org.needle4j.junit.testrule.NeedleTestRule}via {@link RuleChain}.
 * Camunda Services can be injected in test instances and @Deployment-annotated
 * test methods are interpreted.
 * 
 * @author Jan Galinski, Holisticon AG (jan.galinski@holisticon.de)
 */
public class ProcessEngineNeedleRule extends ChainedTestRule<NeedleTestRule, ProcessEngineTestWatcher> implements ProcessEngineTestRule {

	/**
	 * @param testInstance
	 *          the test instance to inject to
	 * @return builder to create the rule
	 */
	public static ProcessEngineNeedleRuleBuilder fluentNeedleRule(final Object testInstance) {
		return new ProcessEngineNeedleRuleBuilder(testInstance);
	}

	/**
	 * The Logger for this class.
	 */
	private final Logger logger = LoggerFactory.getLogger(ProcessEngineNeedleRule.class);

	private final ProcessEngineTestWatcher innerRule;

	ProcessEngineNeedleRule(final Object testInstance, final ProcessEngine processEngine, final InjectionProviderInstancesSupplier additionalProvidersSupplier) {
		// @formatter:off
		super(needleMockitoTestRule(testInstance).addSupplier(new CamundaInstancesSupplier(processEngine)).addSupplier(additionalProvidersSupplier).build());
		// @formatter:on
		this.innerRule = new ProcessEngineTestWatcher(processEngine);
	}

	ProcessEngineNeedleRule(final Object testInstance, final ProcessEngineConfiguration configuration, final Set<InjectionProvider<?>> injectionProviders) {
		this(testInstance, configuration.buildProcessEngine(), InjectionProviders.supplierForInjectionProviders(injectionProviders));
	}

	@Override
	public Statement apply(Statement base, Description description) {
		ProcessEngineAssertions.init(getProcessEngine());
		try {
			return super.apply(base, description);
		} finally {
			ProcessEngineAssertions.reset();
		}
	}

	@Override
	protected ProcessEngineTestWatcher innerRule() {
		return innerRule;
	}

	@Override
	public void setCurrentTime(Date currentTime) {
		innerRule.setCurrentTime(currentTime);
	}

	@Override
	public String getDeploymentId() {
		return innerRule.getDeploymentId();
	}

	@Override
	public ProcessEngine getProcessEngine() {
		return innerRule.getProcessEngine();
	}

	@Override
	public RepositoryService getRepositoryService() {
		return innerRule.getRepositoryService();
	}

	@Override
	public RuntimeService getRuntimeService() {
		return innerRule.getRuntimeService();
	}

	@Override
	public FormService getFormService() {
		return innerRule.getFormService();
	}

	@Override
	public TaskService getTaskService() {
		return innerRule.getTaskService();
	}

	@Override
	public HistoryService getHistoryService() {
		return innerRule.getHistoryService();
	}

	@Override
	public IdentityService getIdentityService() {
		return innerRule.getIdentityService();
	}

	@Override
	public ManagementService getManagementService() {
		return innerRule.getManagementService();
	}

	public ProcessEngineConfiguration getProcessEngineConfiguration() {
		return GetProcessEngineConfiguration.INSTANCE.apply(getProcessEngine());
	}

	public DataSource getProcessEngineDataSource() {
		return getProcessEngineConfiguration().getDataSource();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return innerRule.getName();
	}

	@Override
	public void close() {
		innerRule.close();

	}

	//  @Override
	//  public AuthorizationService getAuthorizationService() {
	//    return innerRule.getAuthorizationService();
	//  }

}
