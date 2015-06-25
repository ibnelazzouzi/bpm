package org.activiti.testing.assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricDetailQuery;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.JobQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.TaskQuery;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ListAssert;
import org.assertj.core.api.MapAssert;
import org.assertj.core.util.Lists;
import org.camunda.bpm.engine.test.util.HistoricActivityInstanceComparator;

/**
 * Assertions for a {@link ProcessInstance}
 * @author Martin Schimak <martin.schimak@plexiti.com>
 * @author Rafael Cordones <rafael@cordones.me>
 */
public class ProcessInstanceAssert extends AbstractProcessAssert<ProcessInstanceAssert, ProcessInstance> {

  protected ProcessInstanceAssert(final ProcessEngine engine, final ProcessInstance actual) {
    super(engine, actual, ProcessInstanceAssert.class);
  }

  protected ProcessInstanceAssert(final ProcessEngine engine, final ProcessInstance actual, Class<?> selfType) {
    super(engine, actual, selfType);
  }

  protected static ProcessInstanceAssert assertThat(final ProcessEngine engine, final ProcessInstance actual) {
    return new ProcessInstanceAssert(engine, actual);
  }

  @Override
  protected ProcessInstance getCurrent() {
    return processInstanceQuery().singleResult();
  }

  @Override
  protected String toString(ProcessInstance processInstance) {
    return processInstance != null ?
      String.format("actual %s {" +
        "id='%s', " +
        "processDefinitionId='%s', " +
        "businessKey='%s'" +
        "}",
        ProcessInstance.class.getSimpleName(),
        processInstance.getId(),
        processInstance.getProcessDefinitionId(),
        processInstance.getBusinessKey())
      : null;
  }

  /**
   * Verifies the expectation that the {@link ProcessInstance} is currently waiting 
   * at one or more specified activities.
   * 
   * @param   activityIds the id's of the activities the process instance is Expecting to 
   *          be waiting at
   * @return  this {@link ProcessInstanceAssert}
   */
  public ProcessInstanceAssert isWaitingAt(final String... activityIds) {
    return isWaitingAt(activityIds, true, false);
  }

  /**
   * Verifies the expectation that the {@link ProcessInstance} is currently NOT waiting 
   * at one or more specified activities.
   *
   * @param   activityIds the id's of the activities the process instance is expected 
   *          not to be waiting at
   * @return  this {@link ProcessInstanceAssert}
   */
  public ProcessInstanceAssert isNotWaitingAt(final String... activityIds) {
    return isWaitingAt(activityIds, false, false);
  }

  /**
   * Verifies the expectation that the {@link ProcessInstance} is currently waiting 
   * at exactly one or more specified activities.
   * 
   * @param   activityIds the id's of the activities the process instance is Expecting to 
   *          be waiting at
   * @return  this {@link ProcessInstanceAssert}
   */
  public ProcessInstanceAssert isWaitingAtExactly(final String... activityIds) {
    return isWaitingAt(activityIds, true, true);
  }

  private ProcessInstanceAssert isWaitingAt(final String[] activityIds, boolean isWaitingAt, boolean exactly) {
    ProcessInstance current = getExistingCurrent();
    Assertions.assertThat(activityIds)
      .overridingErrorMessage("Expecting list of activityIds not to be null, not to be empty and not to contain null values: %s."
        , Lists.newArrayList(activityIds))
      .isNotNull().isNotEmpty().doesNotContainNull();
    final List<String> activeActivityIds = runtimeService().getActiveActivityIds(actual.getId());
    final String message = "Expecting %s " +
      (isWaitingAt ? "to be waiting at " + (exactly ? "exactly " : "") + "%s, ": "NOT to be waiting at %s, ") +
      "but it is actually waiting at %s.";
    ListAssert<String> assertion = (ListAssert<String>) Assertions.assertThat(activeActivityIds)
      .overridingErrorMessage(message, 
        toString(current),
        Lists.newArrayList(activityIds), 
        activeActivityIds);
    if (exactly) {
        String[] sorted = activityIds.clone();
        Arrays.sort(sorted);
      if (isWaitingAt) {
        assertion.containsExactly(sorted);
      } else {
        throw new UnsupportedOperationException(); 
        // "isNotWaitingAtExactly" is unsupported
      }
    } else {
      if (isWaitingAt) {
        assertion.contains(activityIds);
      } else {
        assertion.doesNotContain(activityIds);
      }
    }
    return this;
  }

  /**
   * Verifies the expectation that the {@link ProcessInstance} is currently waiting 
   * for one or more specified messages.
   *
   * @param   messageNames the names of the message the process instance is expected to 
   *          be waiting for
   * @return  this {@link ProcessInstanceAssert}
   */
  public ProcessInstanceAssert isWaitingFor(final String... messageNames) {
    return isWaitingFor(messageNames, true);
  }

  /**
   * Verifies the expectation that the {@link ProcessInstance} is currently waiting 
   * for one or more specified messages.
   *
   * @param   messageNames the names of the message the process instance is expected to 
   *          be waiting for
   * @return  this {@link ProcessInstanceAssert}
   */
  public ProcessInstanceAssert isNotWaitingFor(final String... messageNames) {
    return isWaitingFor(messageNames, false);
  }

  private ProcessInstanceAssert isWaitingFor(final String[] messageNames, boolean isWaitingFor) {
    isNotNull();
    Assertions.assertThat(messageNames)
      .overridingErrorMessage("Expecting list of messageNames not to be null, not to be empty and not to contain null values: %s."
        , Lists.newArrayList(messageNames))
      .isNotNull().isNotEmpty().doesNotContainNull();
    for (String messageName: messageNames) {
      List<Execution> executions = executionQuery().messageEventSubscriptionName(messageName).list();
      ListAssert<Execution> assertion = (ListAssert<Execution>) Assertions.assertThat(executions).overridingErrorMessage("Expecting %s " +
        (isWaitingFor ? "to be waiting for %s, ": "NOT to be waiting for %s, ") +
        "but actually did " + (isWaitingFor ? "not ": "") + "find it to be waiting for message [%s].", 
        actual, Arrays.asList(messageNames), messageName);
      if (isWaitingFor) {
        assertion.isNotEmpty();
      } else {
        assertion.isEmpty();
      }
    }
    return this;
  }
  
  /**
   * Verifies the expectation that the {@link ProcessInstance} has passed one or 
   * more specified activities.
   * 
   * @param   activityIds the id's of the activities expected to have been passed    
   * @return  this {@link ProcessInstanceAssert}
   */
  public ProcessInstanceAssert hasPassed(final String... activityIds) {
    return hasPassed(activityIds, true, false);
  }

  /**
   * Verifies the expectation that the {@link ProcessInstance} has passed one or 
   * more specified activities exactly in the given order.
   *
   * @param   activityIds the id's of the activities expected to have been passed    
   * @return  this {@link ProcessInstanceAssert}
   */
  public ProcessInstanceAssert hasPassedInOrder(final String... activityIds) {
    return hasPassed(activityIds, true, true);
  }

  /**
   * Verifies the expectation that the {@link ProcessInstance} has NOT passed one 
   * or more specified activities.
   *
   * @param   activityIds the id's of the activities expected NOT to have been passed    
   * @return  this {@link ProcessInstanceAssert}
   */
  public ProcessInstanceAssert hasNotPassed(final String... activityIds) {
    return hasPassed(activityIds, false, false);
  }
  
  private ProcessInstanceAssert hasPassed(final String[] activityIds, boolean hasPassed, boolean inOrder) {
    isNotNull();
    Assertions.assertThat(activityIds)
      .overridingErrorMessage("Expecting list of activityIds not to be null, not to be empty and not to contain null values: %s." 
        , Lists.newArrayList(activityIds))
      .isNotNull().isNotEmpty().doesNotContainNull();
    List<HistoricActivityInstance> finishedInstances = historicActivityInstanceQuery().finished().list();
    Collections.sort(finishedInstances, new HistoricActivityInstanceComparator());
    List<String> finished = new ArrayList<String>(finishedInstances.size());
    for (HistoricActivityInstance instance: finishedInstances) {
      finished.add(instance.getActivityId());
    }
    final String message = "Expecting %s " +
      (hasPassed ? "to have passed activities %s at least once" 
        + (inOrder? " and in order" : "") + ", "
        : "NOT to have passed activities %s, ") +
      "but actually we instead we found that it passed %s. (Please make sure you have set the history " +
      "service of the engine to at least 'activity' or a higher level before making use of this assertion!)";
    ListAssert<String> assertion = (ListAssert<String>) Assertions.assertThat(finished)
      .overridingErrorMessage(message, 
        actual, 
        Lists.newArrayList(activityIds), 
        Lists.newArrayList(finished)
      );
    if (hasPassed) {
      assertion.contains(activityIds);
      if (inOrder) {
        List<String> remainingFinished = finished;
        for (int i = 0; i< activityIds.length; i++) {
          Assertions.assertThat(remainingFinished)
            .overridingErrorMessage(message,
              actual,
              Lists.newArrayList(activityIds),
              Lists.newArrayList(finished)
            )
            .contains(activityIds[i]);
          remainingFinished = remainingFinished.subList(remainingFinished.indexOf(activityIds[i]) + 1, remainingFinished.size());
        }
      }
    }
    else
      assertion.doesNotContain(activityIds);
    return this;
  }

  /**
   * Verifies the expectation that the {@link ProcessInstance} holds one or 
   * more process variables with the specified names. 
   *
   * @param   names the names of the process variables expected to exist. In
   *          case no variable name is given, the existence of at least one
   *          variable will be verified.
   * @return  this {@link ProcessInstanceAssert}
   */
  public ProcessInstanceAssert hasVariables(final String... names) {
    return hasVars(names);
  }

  /**
   * Verifies the expectation that the {@link ProcessInstance} holds no 
   * process variables at all.
   *
   * @return  this {@link ProcessInstanceAssert}
   */
  public ProcessInstanceAssert hasNoVariables() {
    return hasVars(null);
  }

  private ProcessInstanceAssert hasVars(final String[] names) {
    boolean shouldHaveVariables = names != null;
    boolean shouldHaveSpecificVariables = names != null && names.length > 0;

    ProcessInstance current = getExistingCurrent();
    Map<String, Object> vars = runtimeService().getVariables(getExistingCurrent().getProcessInstanceId());
    MapAssert<String, Object> assertion = (MapAssert<String, Object>) Assertions.assertThat(vars)
      .overridingErrorMessage("Expecting %s to hold " + 
        (shouldHaveVariables ? "process variables" + (shouldHaveSpecificVariables ? " %s, " : ", ") : "no variables at all, ") +
        "instead we found it to hold " + (vars.isEmpty() ? "no variables at all." : "the variables %s."),
        toString(current), shouldHaveSpecificVariables ? Arrays.asList(names) : vars.keySet(), vars.keySet()
      );
    if (shouldHaveVariables) {
      if (shouldHaveSpecificVariables) {
        assertion.containsKeys(names);
      } else {
        assertion.isNotEmpty();
      }
    } else {
      assertion.isEmpty();
    }
    return this;
  }
  
  /**
   * Verifies the expectation that the {@link ProcessInstance} has the 
   * given processDefinitionKey.
   *
   * @param processDefinitionKey the expected key
   * @return  this {@link ProcessInstanceAssert}
   */
  public ProcessInstanceAssert hasProcessDefinitionKey(String processDefinitionKey) {
    isNotNull();
    final String message = "Expecting %s to have process definition key '%s', " +
      "but instead found it to have process definition key '%s'.";
    ProcessDefinition processDefinition = processDefinitionQuery().singleResult();
    Assertions.assertThat(processDefinitionKey)
      .overridingErrorMessage(message, toString(actual), processDefinitionKey, processDefinition.getKey())
      .isEqualTo(processDefinition.getKey());
    return this;
  }
  
  /**
   * Verifies the expectation that the {@link ProcessInstance} is ended.
   * 
   * @return  this {@link ProcessInstanceAssert}
   */
  public ProcessInstanceAssert isEnded() {
    isNotNull();
    final String message = "Expecting %s to be ended, but it is not!. (Please " +
      "make sure you have set the history service of the engine to at least " +
      "'activity' or a higher level before making use of this assertion!)";
    Assertions.assertThat(processInstanceQuery().singleResult())
      .overridingErrorMessage(message,
        toString(actual))
      .isNull();
    Assertions.assertThat(historicProcessInstanceQuery().singleResult())
      .overridingErrorMessage(message, 
        toString(actual))
      .isNotNull();
    return this;
  }

  /**
   * Verifies the expectation that the {@link ProcessInstance} is currently 
   * suspended.
   * 
   * @return  this {@link ProcessInstanceAssert}
   */
  public ProcessInstanceAssert isSuspended() {
    ProcessInstance current = getExistingCurrent();
    Assertions.assertThat(current.isSuspended())
      .overridingErrorMessage("Expecting %s to be suspended, but it is not!", 
        toString(actual))
      .isTrue();
    return this;
  }

  /**
   * Verifies the expectation that the {@link ProcessInstance} is not ended.
   * 
   * @return  this {@link ProcessInstanceAssert}
   */
  public ProcessInstanceAssert isNotEnded() {
    ProcessInstance current = getExistingCurrent();
    Assertions.assertThat(current)
      .overridingErrorMessage("Expecting %s not to be ended, but it is!", 
        toString(current))
      .isNotNull();
    return this;
  }

  /**
   * Verifies the expectation that the {@link ProcessInstance} is currently active, 
   * iow not suspended and not ended.
   * 
   * @return  this {@link ProcessInstanceAssert}
   */
  public ProcessInstanceAssert isActive() {
    ProcessInstance current = getExistingCurrent();
    isStarted();
    isNotEnded();
    Assertions.assertThat(current.isSuspended())
      .overridingErrorMessage("Expecting %s not to be suspended, but it is!", 
        toString(current))
      .isFalse();
    return this;
  }

  /**
   * Verifies the expectation that the {@link ProcessInstance} is started. This is 
   * also true, in case the process instance already ended.
   * 
   * @return  this {@link ProcessInstanceAssert}
   */
  public ProcessInstanceAssert isStarted() {
    Object pi = getCurrent();
    if (pi == null) 
      pi = historicProcessInstanceQuery().singleResult();
    Assertions.assertThat(pi)
      .overridingErrorMessage("Expecting %s to be started, but it is not!", 
        toString(actual))
      .isNotNull();
    return this;
  }

  /**
   * Enter into a chained task assert inspecting the one and mostly 
   * one task currently available in the context of the process instance
   * under test of this ProcessInstanceAssert.
   * 
   * @return  TaskAssert inspecting the only task available. Inspecting a 
   *          'null' Task in case no such Task is available.
   * @throws  org.camunda.bpm.engine.ProcessEngineException in case more 
   *          than one task is delivered by the query (after being narrowed 
   *          to actual ProcessInstance)
   */
  public TaskAssert task() {
    return task(taskQuery());
  }

  /**
   * Enter into a chained task assert inspecting the one and mostly 
   * one task of the specified task definition key currently available in the 
   * context of the process instance under test of this ProcessInstanceAssert.
   * 
   * @param   taskDefinitionKey definition key narrowing down the search for 
   *          tasks
   * @return  TaskAssert inspecting the only task available. Inspecting a 
   *          'null' Task in case no such Task is available.
   * @throws  org.camunda.bpm.engine.ProcessEngineException in case more than one 
   *          task is delivered by the query (after being narrowed to actual 
   *          ProcessInstance)
   */
  public TaskAssert task(String taskDefinitionKey) {
    isWaitingAt(taskDefinitionKey);
    return task(taskQuery().taskDefinitionKey(taskDefinitionKey));
  }

  /**
   * Enter into a chained task assert inspecting only tasks currently
   * available in the context of the process instance under test of this
   * ProcessInstanceAssert. The query is automatically narrowed down to
   * the actual ProcessInstance under test of this assertion.
   *
   * @param   query TaskQuery further narrowing down the search for tasks
   *          The query is automatically narrowed down to the actual 
   *          ProcessInstance under test of this assertion.
   * @return  TaskAssert inspecting the only task resulting from the given
   *          search. Inspecting a 'null' Task in case no such Task is 
   *          available.
   * @throws  org.camunda.bpm.engine.ProcessEngineException in case more than 
   *          one task is delivered by the query (after being narrowed to 
   *          actual ProcessInstance)
   */
  public TaskAssert task(final TaskQuery query) {
    if (query == null)
      throw new IllegalArgumentException("Illegal call of task(query = 'null') - but must not be null!");
    isNotNull();
    TaskQuery narrowed = query.processInstanceId(actual.getId());
    return TaskAssert.assertThat(engine, narrowed.singleResult());
  }

  /**
   * Enter into a chained process instance assert inspecting the one and mostly 
   * one called process instance currently available in the context of the process instance
   * under test of this ProcessInstanceAssert.
   *
   * @return  ProcessInstanceAssert inspecting the only called process instance available. Inspecting a 
   *          'null' process instance in case no such Task is available.
   * @throws  org.camunda.bpm.engine.ProcessEngineException in case more 
   *          than one process instance is delivered by the query (after being narrowed 
   *          to actual ProcessInstance)
   */
  public ProcessInstanceAssert calledProcessInstance() {
    return calledProcessInstance(super.processInstanceQuery());
  }

  /**
   * Enter into a chained process instance assert inspecting the one and mostly 
   * one called process instance of the specified process definition key currently available in the 
   * context of the process instance under test of this ProcessInstanceAssert.
   *
   * @param   processDefinitionKey definition key narrowing down the search for 
   *          process instances
   * @return  ProcessInstanceAssert inspecting the only such process instance available. 
   *          Inspecting a 'null' ProcessInstance in case no such ProcessInstance is available.
   * @throws  org.camunda.bpm.engine.ProcessEngineException in case more than one 
   *          process instance is delivered by the query (after being narrowed to actual 
   *          ProcessInstance)
   */
  public ProcessInstanceAssert calledProcessInstance(String processDefinitionKey) {
    return calledProcessInstance(super.processInstanceQuery().processDefinitionKey(processDefinitionKey));
  }

  /**
   * Enter into a chained process instance assert inspecting a called process instance 
   * called by and currently available in the context of the process instance under test 
   * of this ProcessInstanceAssert. The query is automatically narrowed down to the actual 
   * ProcessInstance under test of this assertion.
   *
   * @param   query ProcessDefinitionQuery further narrowing down the search for process 
   *          instances. The query is automatically narrowed down to the actual 
   *          ProcessInstance under test of this assertion.
   * @return  ProcessInstanceAssert inspecting the only such process instance resulting 
   *          from the given search. Inspecting a 'null' ProcessInstance in case no such 
   *          ProcessInstance is available.
   * @throws  org.camunda.bpm.engine.ProcessEngineException in case more than 
   *          one ProcessInstance is delivered by the query (after being narrowed to 
   *          actual ProcessInstance)
   */
  public ProcessInstanceAssert calledProcessInstance(ProcessInstanceQuery query) {
    if (query == null)
      throw new IllegalArgumentException("Illegal call of calledProcessInstance(query = 'null') - but must not be null!");
    isNotNull();
    ProcessInstanceQuery narrowed = query.superProcessInstanceId(actual.getId());
    return CalledProcessInstanceAssert.assertThat(engine, narrowed.singleResult());
  }

  /**
   * Enter into a chained job assert inspecting the one and mostly 
   * one job currently available in the context of the process 
   * instance under test of this ProcessInstanceAssert.
   * 
   * @return  JobAssert inspecting the only job available. Inspecting 
   *          a 'null' Job in case no such Job is available.
   * @throws  org.camunda.bpm.engine.ProcessEngineException in case more 
   *          than one task is delivered by the query (after being narrowed 
   *          to actual ProcessInstance)
   */
  public JobAssert job() {
    return job(jobQuery());
  }

  /**
   * Enter into a chained task assert inspecting the one and mostly 
   * one task of the specified task definition key currently available in the 
   * context of the process instance under test of this ProcessInstanceAssert.
   *
   * @param   activityId id narrowing down the search for jobs
   * @return  JobAssert inspecting the retrieved job. Inspecting a 
   *          'null' Task in case no such Job is available.
   * @throws  org.camunda.bpm.engine.ProcessEngineException in case more than one 
   *          job is delivered by the query (after being narrowed to actual 
   *          ProcessInstance)
   */
  public JobAssert job(String activityId) {
    Execution execution = executionQuery().activityId(activityId)
    		//.active()
    		.singleResult();
    return JobAssert.assertThat(
      engine,
      execution != null ? jobQuery().executionId(execution.getId()).singleResult() : null
    );
  }
  
  /**
   * Enter into a chained job assert inspecting only jobs currently
   * available in the context of the process instance under test of this
   * ProcessInstanceAssert. The query is automatically narrowed down to
   * the actual ProcessInstance under test of this assertion.
   *
   * @param   query JobQuery further narrowing down the search for 
   *          jobs. The query is automatically narrowed down to the 
   *          actual ProcessInstance under test of this assertion.
   * @return  JobAssert inspecting the only job resulting from the 
   *          given search. Inspecting a 'null' job in case no such job 
   *          is available.
   * @throws  org.camunda.bpm.engine.ProcessEngineException in case more 
   *          than one job is delivered by the query (after being narrowed 
   *          to actual ProcessInstance)
   */
  public JobAssert job(JobQuery query) {
    if (query == null)
      throw new IllegalArgumentException("Illegal call of job(query = 'null') - but must not be null!");
    isNotNull();
    JobQuery narrowed = query.processInstanceId(actual.getId());
    return JobAssert.assertThat(engine, narrowed.singleResult());
  }

  /**
   * Enter into a chained map assert inspecting the variables currently
   * available in the context of the process instance under test of this
   * ProcessInstanceAssert.
   *
   * @return  MapAssert<String, Object> inspecting the process variables. 
   *          Inspecting a 'null' map in case no such variables are available.
   */
  public MapAssert<String, Object> variables() {
    return (MapAssert<String, Object>) Assertions.assertThat(runtimeService().getVariables(getExistingCurrent().getProcessInstanceId()));
  }

  /**
   * Enter into a chained map assert inspecting the variables 
   * available in the context of the (finished) process instance under test of this
   * ProcessInstanceAssert.
   *
   * @return  MapAssert<String, Object> inspecting the process variables. 
   *          Inspecting a 'null' map in case no such variables are available.
   */
  public MapAssert<String, Object> historicVariables() {
    List<HistoricVariableInstance> result = 
        historyService().createHistoricVariableInstanceQuery().processInstanceId(actual.getId()).list();
    Map<String, Object> map = new HashMap<String, Object>();
    for (HistoricVariableInstance instance : result) {
      map.put(instance.getVariableName(), instance.getValue());
    }
    return (MapAssert<String, Object>) Assertions.assertThat(map);
  }
  
  
  /* TaskQuery, automatically narrowed to actual {@link ProcessInstance} */
  @Override
  protected TaskQuery taskQuery() {
    return super.taskQuery().processInstanceId(actual.getId());
  }

  /* JobQuery, automatically narrowed to actual {@link ProcessInstance} */
  @Override
  protected JobQuery jobQuery() {
    return super.jobQuery().processInstanceId(actual.getId());
  }

  /* ProcessInstanceQuery, automatically narrowed to actual {@link ProcessInstance} */
  @Override
  protected ProcessInstanceQuery processInstanceQuery() {
    return super.processInstanceQuery().processInstanceId(actual.getId());
  }

  /* ExecutionQuery, automatically narrowed to actual {@link ProcessInstance} */
  @Override
  protected ExecutionQuery executionQuery() {
    return super.executionQuery().processInstanceId(actual.getId());
  }

  /* VariableInstanceQuery, automatically narrowed to actual {@link ProcessInstance} */
//  @Override
//  protected VariableInstanceQuery variableInstanceQuery() {
//    return super.variableInstanceQuery().processInstanceIdIn(actual.getId());
//  }

  /* HistoricActivityInstanceQuery, automatically narrowed to actual {@link ProcessInstance} */
  @Override
  protected HistoricActivityInstanceQuery historicActivityInstanceQuery() {
    return super.historicActivityInstanceQuery().processInstanceId(actual.getId());
  }

  /* HistoricDetailQuery, automatically narrowed to actual {@link ProcessInstance} */
  @Override
  protected HistoricDetailQuery historicDetailQuery() {
    return super.historicDetailQuery().processInstanceId(actual.getId());
  }

  /* HistoricProcessInstanceQuery, automatically narrowed to actual {@link ProcessInstance} */
  @Override
  protected HistoricProcessInstanceQuery historicProcessInstanceQuery() {
    return super.historicProcessInstanceQuery().processInstanceId(actual.getId());
  }

  /* HistoricTaskInstanceQuery, automatically narrowed to actual {@link ProcessInstance} */
  @Override
  protected HistoricTaskInstanceQuery historicTaskInstanceQuery() {
    return super.historicTaskInstanceQuery().processInstanceId(actual.getId());
  }

  /* HistoricVariableInstanceQuery, automatically narrowed to actual {@link ProcessInstance} */
  @Override
  protected HistoricVariableInstanceQuery historicVariableInstanceQuery() {
    return super.historicVariableInstanceQuery().processInstanceId(actual.getId());
  }

  /* ProcessDefinitionQuery, automatically narrowed to {@link ProcessDefinition} 
   * of actual {@link ProcessInstance} 
   */
  @Override
  protected ProcessDefinitionQuery processDefinitionQuery() {
    return super.processDefinitionQuery().processDefinitionId(actual.getProcessDefinitionId());
  }

}