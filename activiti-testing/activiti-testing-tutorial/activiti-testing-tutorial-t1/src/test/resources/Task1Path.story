Scenario:Task1Path

GivenStories: UserTaskReached.story
When the usertask1 is processed with decision goTask1
Then the servicetask1 is processed
And the process is finished
