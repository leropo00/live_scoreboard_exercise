package com.sportradar.scoreboard;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
}
