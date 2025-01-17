package com.sportradar.scoreboard;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.sportradar.scoreboard.Team;
import com.sportradar.scoreboard.WorldCupScoreboard;
import com.sportradar.scoreboard.errors.ScoreboardException;

public class WorldCupScoreboardTest {
	private WorldCupScoreboard scoreboard;
	private static final String DUPLICATE_COUNTRY = "Mexico";
	
	@BeforeEach
	void init() {
		scoreboard = new WorldCupScoreboard();
	}
	
	@Test 
    @DisplayName("When scoreboard is initialized no matches are present")   
	void noMatchesAtStart()  {
		assertEquals(0, scoreboard.size());
	}
	
	@Test
	@DisplayName("After first match was started, scoreboard size is 1") 
	void startFirstMatch() {
        assertDoesNotThrow(() ->
        	scoreboard.startNewMatch(new Team("Mexico"), new Team("Canada")));
		assertEquals(1, scoreboard.size());
	}
	
	@Test
	@DisplayName("After two match were started, scoreboard size is 2") 
	void startTwoMatches() {
        assertDoesNotThrow(() -> {
			scoreboard.startNewMatch(new Team("Mexico"), new Team("Canada"));
			scoreboard.startNewMatch(new Team("Spain"), new Team("Brazil")); 
		});
		assertEquals(2, scoreboard.size());
	}

	// test all the possible combinations of duplicate country
	private static Stream<Arguments> provideArgumentsForDuplicateCountryTest() {
	    return Stream.of(
	    	Arguments.of(DUPLICATE_COUNTRY, "Canada", DUPLICATE_COUNTRY, "Brazil"), 
	    	Arguments.of(DUPLICATE_COUNTRY, "Canada", "Brazil", DUPLICATE_COUNTRY),
	    	Arguments.of("Canada", DUPLICATE_COUNTRY, DUPLICATE_COUNTRY, "Brazil"),
	    	Arguments.of("Canada", DUPLICATE_COUNTRY, "Brazil", DUPLICATE_COUNTRY)
	    );
	}
	
	@ParameterizedTest
	@MethodSource("provideArgumentsForDuplicateCountryTest")
	@DisplayName("If team is already present in scoreboard in active matches, exception will be thrown, team can only play 1 match at a time") 
	void checkExistingTeamInMatch(String homeTeam1, String awayTeam1, String homeTeam2, String awayTeam2) {
		assertThatThrownBy(() -> {
			scoreboard.startNewMatch(new Team(homeTeam1), new Team(awayTeam1));
			scoreboard.startNewMatch(new Team(homeTeam2), new Team(awayTeam2));
		}).isInstanceOf(ScoreboardException.class)
		  .hasMessageContaining(ScoreboardException.ERROR_DUPLICATE_TEAM);
	}
	
	@Test
	@DisplayName("After match was started, score can be incremented one by one")
	void updateMatchScore() throws ScoreboardException {
		final Match testMatch = scoreboard.startNewMatch(new Team("Mexico"), new Team("Canada"));
		final int[][] scores = {{1,0}, {1,1}, {1,2}, {2,2}, {2,3}};

		for (int i = scores.length; i < scores.length; i++)  {
			final int homeScore = scores[i][0];
			final int awayScore = scores[i][1];

			assertDoesNotThrow(() -> scoreboard.updateScore(testMatch, homeScore, awayScore));
			assertEquals(homeScore, testMatch.getHomeScore(), "Home score for match is correct");
			assertEquals(awayScore, testMatch.getAwayScore(), "Away score for match is correct");
			assertEquals(homeScore + awayScore, testMatch.getTotalScore(), "Total score for match is correct");
		}
	}

	private static Stream<Arguments> provideArgumentsForInvalidScoreTest() {
	    return Stream.of(
	    	Arguments.of(-1, -1), 
	    	Arguments.of(3,  0),
	    	Arguments.of(2,  1),
	    	Arguments.of(1,  2)
   		);
	}
	@ParameterizedTest
	@MethodSource("provideArgumentsForInvalidScoreTest")
	@DisplayName("Test combinations of invalid match score updated")
	void invalidMatchScoreUpdates(int homeScore, int awayScore) throws ScoreboardException {
		final Match testMatch = scoreboard.startNewMatch(new Team("Mexico"), new Team("Canada"));
		scoreboard.updateScore(testMatch, 1, 0);
		
		assertThatThrownBy(() -> scoreboard.updateScore(testMatch, homeScore, awayScore))
			.isInstanceOf(ScoreboardException.class)
			.hasMessageContaining(ScoreboardException.ERROR_INVALID_SCORE);
	}
	
	@Test
	@DisplayName("During match a goal can be rejected after it was score, only if it was final goal scored") 
	void goalWasRejectedScoreUpdates() throws ScoreboardException {
		final Match testMatch = createMatchWithScore(new Team("Mexico"), new Team("Canada"), 1, 2);		

		assertDoesNotThrow(() -> scoreboard.updateScore(testMatch, 1, 1));

		assertEquals(1, testMatch.getHomeScore(), "Home score for match is correct");
		assertEquals(1, testMatch.getAwayScore(), "Away score for match is correct");
		assertThatThrownBy(() -> scoreboard.updateScore(testMatch, 1, 0))
		   .isInstanceOf(ScoreboardException.class)
		   .hasMessageContaining(ScoreboardException.ERROR_INVALID_SCORE);
	}
	
	@Test
	@DisplayName("Error occurs if goal was rejected that wasnt scored last") 
	void goalWasRejectedInvalidScoreUpdates() throws ScoreboardException {
		final Match testMatch = createMatchWithScore(new Team("Mexico"), new Team("Canada"), 1, 1);	
		
		assertThatThrownBy(() -> scoreboard.updateScore(testMatch, 0, 1))
			.isInstanceOf(ScoreboardException.class)
			.hasMessageContaining(ScoreboardException.ERROR_INVALID_SCORE);
	}	
	
	@Test
	@DisplayName("Updating score with the same number of goals, does not throw an error or change game state")
	void sameScoreUpdates() throws ScoreboardException  {
		final Match testMatch = createMatchWithScore(new Team("Mexico"), new Team("Canada"), 2, 2);

		assertDoesNotThrow(() -> { 
			scoreboard.updateScore(testMatch, 2, 2);
			scoreboard.updateScore(testMatch, 2, 2);
			scoreboard.updateScore(testMatch, 3, 2);
			scoreboard.updateScore(testMatch, 3, 2);
		});
	}
	
	@Test
	@DisplayName("Finish match that is currently in progress, this can only happen once") 
	void finishExistingMatch() throws ScoreboardException {
		scoreboard.startNewMatch(new Team("Mexico"), new Team("Canada"));
		final Match forRemoval = scoreboard.startNewMatch(new Team("Spain"), new Team("Brazil"));
		scoreboard.startNewMatch(new Team("Germany"), new Team("France"));
		
		assertDoesNotThrow(() -> scoreboard.finishMatch(forRemoval));
		assertEquals(2, scoreboard.size());
		assertThatThrownBy(() -> scoreboard.finishMatch(forRemoval))
			.isInstanceOf(ScoreboardException.class)
			.hasMessageContaining(ScoreboardException.MISSING_TEAM);
	}
	
	@Test
	@DisplayName("Retrives summary of current matches order on total score")
	void testMatchesSummary() throws ScoreboardException {
		createMatchWithScore(new Team("Mexico"), new Team("Canada"), 0, 5);
		createMatchWithScore(new Team("Spain"), new Team("Brazil"), 10, 2);
		createMatchWithScore(new Team("Germany"), new Team("France"), 2, 2);
		createMatchWithScore(new Team("Uruguay"), new Team("Italy"), 6, 6);
		createMatchWithScore(new Team("Argentina"), new Team("Australia"), 3, 1);
		
		List<Match> summary = scoreboard.getMatchesSummary();
		
		assertEquals(scoreboard.size(), summary.size(), "Same number of matches in summary list");
		for (int i = 0; i < summary.size() - 1 ; i++ ) {
			assertTrue(summary.get(i).getTotalScore() > summary.get(i + 1).getTotalScore() || 
				( summary.get(i).getTotalScore() == summary.get(i + 1).getTotalScore() && summary.get(i).getCreationTimestamp() >= summary.get(i + 1).getCreationTimestamp()),
				"Condition for sorting of two consecutive matches");
		}
	}
	
	private Match createMatchWithScore (Team homeTeam, Team awayTeam, int homeScore, int awayScore) throws ScoreboardException {
		final Match testMatch = scoreboard.startNewMatch(homeTeam, awayTeam);
		for (int score = 1; score <= homeScore; score++) {
			scoreboard.updateScore(testMatch, score, 0);
		}		
		for (int score = 1; score <= awayScore; score++) {
			scoreboard.updateScore(testMatch, homeScore, score);
		}
		return testMatch;
	}
}
