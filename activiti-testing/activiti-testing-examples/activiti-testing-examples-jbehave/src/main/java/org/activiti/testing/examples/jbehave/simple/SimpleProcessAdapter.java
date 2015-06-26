package org.activiti.testing.examples.jbehave.simple;

import javax.inject.Named;

import org.activiti.engine.delegate.DelegateExecution;

@Named(SimpleProcessAdapter.NAME)
public interface SimpleProcessAdapter {
  
    String NAME = "simpleProcessAdapter";

    boolean loadContractData(DelegateExecution execution);

    void processContract();

    void cancelProcessing();

}
