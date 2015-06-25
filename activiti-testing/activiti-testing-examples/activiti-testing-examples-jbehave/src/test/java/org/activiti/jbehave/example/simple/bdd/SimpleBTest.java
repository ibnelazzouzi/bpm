package org.activiti.jbehave.example.simple.bdd;

import java.net.URL;
import java.util.List;

import org.activiti.bdd.JBehaveTestBase;
import org.activiti.bdd.steps.ActivitiSteps;
import org.activiti.jbehave.example.simple.steps.SimpleProcessSteps;
import org.activiti.test.ActivitiSupport;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.annotations.needle.UsingNeedle;

/**
 * JBehave Tests for processes.
 */
@UsingSteps(instances = { SimpleProcessSteps.class, ActivitiSteps.class })
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
