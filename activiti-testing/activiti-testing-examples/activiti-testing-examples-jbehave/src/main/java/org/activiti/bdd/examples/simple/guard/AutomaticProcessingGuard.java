package org.activiti.bdd.examples.simple.guard;


import org.activiti.data.ActivityGuard;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.jbehave.example.simple.SimpleProcessConstants.Variables;

import static org.activiti.data.Guards.checkIsSet;

/**
 * Guard of the isAutomatic variable.
 */
public class AutomaticProcessingGuard extends ActivityGuard {

    private static final long serialVersionUID = 1L;

    @Override
    public void checkPostconditions(final DelegateExecution execution) throws IllegalStateException {
        checkIsSet(execution, Variables.IS_AUTOMATIC);
        checkIsSet(execution, Variables.CONTRACT_ID);
    }
}
