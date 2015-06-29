package org.activiti.testing.tutorial.t1.unit;

import org.activiti.engine.test.Deployment;
import org.activiti.testing.jbehave.bdd.Slf4jLoggerRule;
import org.activiti.testing.needle.ProcessEngineNeedleRule;
import org.activiti.testing.tutorial.t1.Process1Constants;
import org.junit.Rule;
import org.junit.Test;

public class Process1UnitTest {

	static {
		Slf4jLoggerRule.DEFAULT.before();
	}

	@Rule
	public ProcessEngineNeedleRule processEngine = ProcessEngineNeedleRule.fluentNeedleRule(this).build();

	@Test
	@Deployment(resources = Process1Constants.BPMN)
	public void shouldDeploy() {
		// nothing to do.
	}
}
