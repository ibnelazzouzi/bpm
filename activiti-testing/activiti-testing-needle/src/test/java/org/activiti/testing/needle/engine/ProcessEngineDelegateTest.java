package org.activiti.testing.needle.engine;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.testing.needle.engine.ProcessEngineDelegate;
import org.activiti.testing.needle.engine.test.cfg.MostUsefulProcessEngineConfiguration;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessEngineDelegateTest {

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public ProcessEngineDelegate delegate;
  private final ProcessEngineConfiguration processEngineConfiguration = MostUsefulProcessEngineConfiguration.mostUsefulProcessEngineConfiguration();

  @After
  public void cleanUpJustInCase() {
    if (delegate != null && delegate.getProcessEngine() != null) {
      delegate.getProcessEngine().close();
    }
  }

  @Test
  public void should_fail_on_creation_with_null_processEngine() {
    thrown.expect(IllegalArgumentException.class);
    new ProcessEngineDelegate((ProcessEngine) null);
  }

  @Test
  public void should_fail_on_creation_with_null_processEngineConfiguration() {
    thrown.expect(IllegalArgumentException.class);
    new ProcessEngineDelegate((ProcessEngineConfiguration) null);
  }

  @Test
  public void should_eager_initialize_engine() {
    delegate = new ProcessEngineDelegate(processEngineConfiguration, true);

    assertThat(delegate, notNullValue());
    assertThat(delegate.isInitialized(), is(true));
  }

  @Test
  public void should_lazy_initialize_engine() {
    delegate = new ProcessEngineDelegate(processEngineConfiguration, false);
    assertThat(delegate, notNullValue());
    assertThat(delegate.isInitialized(), is(false));
    delegate.initProcessEngine();
    assertThat(delegate.isInitialized(), is(true));
  }

  @Test
  public void should_initialize_with_prebuild_engine() {
    final ProcessEngine preBuildProcessEngine = processEngineConfiguration.buildProcessEngine();

    delegate = new ProcessEngineDelegate(preBuildProcessEngine);
    assertThat(delegate.isInitialized(), is(true));

  }

  @Test
  public void should_fail_to_re_initialize_engine_after_close_when_configuration() {

    final ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
    delegate = new ProcessEngineDelegate(processEngine);

    delegate.closeProcessEngine();
    assertThat(delegate.isInitialized(), is(false));

    delegate.initProcessEngine();
    assertThat(delegate.isInitialized(), is(true));
  }

  @Test
  public void should_fail_to_close_more_than_once() {
    final ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
    delegate = new ProcessEngineDelegate(processEngine);
    delegate.closeProcessEngine();

    thrown.expect(IllegalStateException.class);
    delegate.closeProcessEngine();
  }
}
