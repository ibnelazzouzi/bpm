package org.activiti.testing.needle.engine.test.function;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.testing.needle.engine.ProcessEngineDelegate;
import org.activiti.testing.needle.engine.test.cfg.MostUsefulProcessEngineConfiguration;

import com.google.common.base.Supplier;

/**
 * Creates a new ProcessEngineConfiguration based on camunda.cfg.xml. Falls back
 * to activiti.cfg.xml for compatibility reasons. If no cfg.xml files can be
 * found, a MostUsefulProcessEngineConfiguration is created.
 *
 * @author Jan Galinski, Holisticon AG
 */
public enum CreateConfigurationFromResource implements Supplier<ProcessEngineConfiguration> {
	INSTANCE;

	/**
	 * Creates a new ProcessEngineConfiguration from source. Returns null if
	 * config can not be created (file does not exist).
	 */
	private static class ConfigurationSupplier implements Supplier<ProcessEngineConfiguration> {

		private final String cfgXmlFilename;

		private ConfigurationSupplier(final String cfgXmlFilename) {
			this.cfgXmlFilename = cfgXmlFilename;
		}

		@Override
		public ProcessEngineConfiguration get() {
			try {
				return ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(this.cfgXmlFilename);
			} catch (Exception e) {
				//e.printStackTrace();
				return null;
			}
		}
	}

	private final Supplier<ProcessEngineConfiguration> activitiCfgXmlSupplier = new ConfigurationSupplier("activiti.cfg.xml");

	@Override
	public ProcessEngineConfiguration get() {
		ProcessEngineConfiguration configuration = this.activitiCfgXmlSupplier.get();
		if (configuration == null) {
			configuration = MostUsefulProcessEngineConfiguration.SUPPLIER.get();
		}

		return configuration;
	}

	public ProcessEngine buildProcessEngine() {
		return this.get().buildProcessEngine();
	}

	public ProcessEngineDelegate createProcessEngineDelegate(final boolean eager) {
		return new ProcessEngineDelegate(this.get(), eager);
	}
}
