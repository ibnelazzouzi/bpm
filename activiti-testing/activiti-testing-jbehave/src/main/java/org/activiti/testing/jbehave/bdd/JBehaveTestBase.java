package org.activiti.testing.jbehave.bdd;

import static org.jbehave.core.io.CodeLocations.codeLocationFromPath;
import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML;
import static org.jbehave.core.reporters.Format.XML;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.jbehave.core.InjectableEmbedder;
import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.needle.NeedleAnnotatedEmbedderRunner;
import org.jbehave.core.reporters.PrintStreamStepdocReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.PrintStreamStepMonitor;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Base class for JBehave tests.
 * 
 * @author Simon Zambrovski, holisticon AG
 */
@RunWith(NeedleAnnotatedEmbedderRunner.class)
@UsingEmbedder(embedder = Embedder.class, generateViewAfterStories = true, ignoreFailureInStories = false, ignoreFailureInView = false, verboseFailures = true)
@Configure(stepMonitor = PrintStreamStepMonitor.class, pendingStepStrategy = FailingUponPendingStep.class, stepdocReporter = PrintStreamStepdocReporter.class, storyReporterBuilder = JBehaveTestBase.RichReporterBuilder.class)
public abstract class JBehaveTestBase extends InjectableEmbedder {

  private static final boolean REPORT_FAILURE_TRACE = false;
  private static final boolean COMPRESS_FAILURE_TRACE = true;
  static {
    Slf4jLoggerRule.DEFAULT.before();
  }

  /**
   * Retrieves the location of the stories. <br>
   * This method is intended to be overwritten on divergent location for stories
   * than src/test/resources
   * 
   * @return location of the stories to look for.
   */
  protected URL getStoryLocation() {
    return codeLocationFromPath("src/test/resources");
  }

  /**
   * Retrieves the location of the stories.
   */
  protected List<String> storyPaths() {
    return new StoryFinder().findPaths(getStoryLocation(), "**/*.story", "");
  }

  @Override
  @Test
  public void run() {
    injectedEmbedder().runStoriesAsPaths(storyPaths());
  }

  /**
   * Own report builder.
   */
  public static class RichReporterBuilder extends StoryReporterBuilder {

    /**
     * Constructs the builder.
     */
    public RichReporterBuilder() {
      withDefaultFormats().withViewResources(getViewResources()).withFormats(CONSOLE, HTML, XML).withFailureTrace(REPORT_FAILURE_TRACE)
          .withFailureTraceCompression(COMPRESS_FAILURE_TRACE);
    }

    /**
     * Retrieves the configuration of the view.
     */
    final static Properties getViewResources() {
      final Properties viewResources = new Properties();
      viewResources.put("decorateNonHtml", "false");
      return viewResources;
    }

  }

}
