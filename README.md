# CODING EXERCISE: Live Football World Cup Score Board

## Running the project
Project is structured as a Maven project. It is recommended to use an IDE, to run the tests.
You can also run tests via command line, by being in project root directoy with command: 

``` mvn test```

Class WorldCupScoreboardTest, contains all tests for required scoreboard operations.

## Commit history
In almost all commits, commit message starts with one of TDD steps: "red", "green" or "refactor". Ordering of commits occurs based on order of step in TDD: 1. "red", 2. "green", 3. "refactor".
Occasionaly refactor step is missing if no refactoring is done, or it is split in multiple commits,
if changes were larger.

## Implementation has the following assumptions:
-  Team name is unique, so if team has the same name, it is the same team.
-  Since scoreboard has only matches in progress, team can appear on scoreboard only once, regardless if it is home team or away team.
-  Update score takes as parameter current score for both teams and follows the rules:
    - Score must always be either 0 or positive numbers, negative score results in exception.
    - Difference between current and new score must be one of the following scenarios:
        	1.) Same score -> Both scores are the same, scoreboard permits updating the same score and doesn't throw	an exception for such operation.
        	2.) Goal scored -> Score is larger by exactly 1 for one of the teams and same for the the other team.
        	3.) Goal rejected -> Team that scored last goal, has score smaller by exactly 1, while other team has the same score.	This scenario is based on 2022 FIFA World Cup rules, where a goal could be overturned by the Video Assistant Referee (VAR) as long as the match had not yet restarted. 

