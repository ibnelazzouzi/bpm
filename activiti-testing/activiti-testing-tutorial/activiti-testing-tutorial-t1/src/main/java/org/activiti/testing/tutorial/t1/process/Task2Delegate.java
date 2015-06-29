package org.activiti.testing.tutorial.t1.process;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class Task2Delegate implements JavaDelegate {

	@Override
	public void execute(final DelegateExecution arg0) throws Exception {
		System.out.println("Execute Task2 delegate");

	}

}
