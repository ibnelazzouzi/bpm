Scenario: UserTaskReached

Given the process definition diagrams/process1.bpmn
When the process process1 is started
Then the step usertask1 is reached