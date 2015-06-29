package org.activiti.testing.tutorial.t1.process;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class Task1Delegate implements JavaDelegate {

	@Override
	public void execute(final DelegateExecution execution) throws Exception {
		System.out.println("Execute Task1 delegate");

	}

}
