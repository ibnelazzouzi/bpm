package org.activiti.testing.tutorial.t1.bdd;

import java.net.URL;
import java.util.List;

import org.activiti.testing.jbehave.bdd.JBehaveTestBase;
import org.activiti.testing.jbehave.bdd.steps.ActivitiSteps;
import org.activiti.testing.jbehave.support.ActivitiSupport;
import org.activiti.testing.tutorial.t1.steps.Process1Steps;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.annotations.needle.UsingNeedle;

/**
 * JBehave Tests for processes.
 */
@UsingSteps(instances = { Process1Steps.class, ActivitiSteps.class })
@UsingNeedle(provider = { ActivitiSupport.class })
public class SimpleBTest extends JBehaveTestBase {
	@Override
	protected URL getStoryLocation() {
		return this.getClass().getResource("/");
	}

	@Override
	protected List<String> storyPaths() {
		return super.storyPaths();
	}
}
