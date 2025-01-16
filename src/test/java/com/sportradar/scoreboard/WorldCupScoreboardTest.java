package com.sportradar.scoreboard;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.assertj.core.api.Assertions;

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
		assertTrue(scoreboard.getScoreboard().isEmpty());
	}
	
	@Test
	@DisplayName("After first match was started, scoreboard size is 1") 
	void startFirstMatch() {
        assertDoesNotThrow(() ->
        	scoreboard.startNewMatch(new Team("Mexico"), new Team("Canada")));
		Assertions.assertThat(scoreboard.getScoreboard()).hasSize(1);
	}
	
	@Test
	@DisplayName("After two match were started, scoreboard size is 2") 
	void startTwoMatches() {
        assertDoesNotThrow(() -> {
			scoreboard.startNewMatch(new Team("Mexico"), new Team("Canada"));
			scoreboard.startNewMatch(new Team("Spain"), new Team("Brazil")); 
		});
		Assertions.assertThat(scoreboard.getScoreboard()).hasSize(2);
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
	@DisplayName("Finish match that is currently in progress, this can only happen once") 
	void finishExistingMatch() throws ScoreboardException {
		scoreboard.startNewMatch(new Team("Mexico"), new Team("Canada"));
		final Match forRemoval = scoreboard.startNewMatch(new Team("Spain"), new Team("Brazil"));
		scoreboard.startNewMatch(new Team("Germany"), new Team("France"));
		
		assertDoesNotThrow(() -> scoreboard.finishMatch(forRemoval));
		assertEquals(2, scoreboard.getScoreboard().size());
		assertThatThrownBy(() -> scoreboard.finishMatch(forRemoval))
			.isInstanceOf(ScoreboardException.class)
			.hasMessageContaining(ScoreboardException.MISSING_TEAM);
	}
	
	private Match createMatchWithScore (Team homeTeam, Team awayTeam, int homeScore, int awayScore) throws ScoreboardException {
		final Match testMatch = scoreboard.startNewMatch(homeTeam, awayTeam);
		for(int score = 1; score <= homeScore; score++) {
			scoreboard.updateScore(testMatch, score, 0);
		}		
		for(int score = 1; score <= awayScore; score++) {
			scoreboard.updateScore(testMatch, homeScore, score);
		}
		return testMatch;
	}
}
