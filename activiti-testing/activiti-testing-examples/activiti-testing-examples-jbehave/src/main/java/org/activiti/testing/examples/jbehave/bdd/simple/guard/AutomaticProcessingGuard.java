package org.activiti.testing.examples.jbehave.bdd.simple.guard;


import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.testing.examples.jbehave.simple.SimpleProcessConstants.Variables;
import org.activiti.testing.jbehave.data.ActivityGuard;

import static org.activiti.testing.jbehave.data.Guards.checkIsSet;

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
