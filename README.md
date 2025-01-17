# CODING EXERCISE: Live Football World Cup ScoreBoard

## Running the project
The project is structured as a Maven project. It is recommended to use an IDE, to run the tests.
You can also run tests via the command line, by being in the project root directory and using the command: 

``` mvn test```

Class WorldCupScoreboardTest, contains all tests for required scoreboard operations.

## Commit history
In almost all commits, commit message starts with one of TDD steps: "red", "green" or "refactor". Ordering of commits occurs based on the order of steps in TDD: 1. "red", 2. "green", 3. "refactor".
Occasionally refactor step is missing if no refactoring was done, or if changes were larger, is split into multiple commits.
Since this is a short exercise, work was done just in the main branch for simplicity.

## Implementation has the following assumptions:
-  Team name is unique, so if the team has the same name, it is the same team.
-  Since the scoreboard has only matches in progress, a team can appear on the scoreboard only once, regardless if it already appears as a home team or an away team.
-  Update score takes as parameter current score for both teams and follows the rules:
    - Score must always be either 0 or positive numbers, a negative score results in an exception.
   - The difference between current and new score must be one of the following scenarios:
   
        	1.) Same score -> Both scores are the same, the scoreboard permits updating the same score 
                and doesn't throw an exception for such operation.
        	2.) Goal scored -> Score is larger by exactly 1 for one of the teams and same for the the other team.
        	3.) Goal rejected -> Team that scored the last goal, has score smaller by exactly 1, 
                while the other team has the same score.	
                This scenario is based on 2022 FIFA World Cup rules, 
                where a goal could be overturned by the Video Assistant Referee (VAR)
                as long as the match had not yet restarted. 

