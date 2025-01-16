package com.sportradar.scoreboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sportradar.scoreboard.Match;
import com.sportradar.scoreboard.Team;

public class MatchTest {
	@Test
	@DisplayName("Tests initialization of match object")
	void testInitialization() {
		final Team homeTeamInput = new Team("Mexico");
		final Team awayTeamInput = new Team("Canada");
		
		Match match = new Match(homeTeamInput, awayTeamInput);
		
		assertSame(homeTeamInput, match.getHomeTeam());
		assertSame(awayTeamInput, match.getAwayTeam());
		assertEquals(0, match.getHomeScore());
		assertEquals(0, match.getAwayScore());
		assertEquals(0, match.getTotalScore());
	}
	
	@Test
	@DisplayName("Tests creation timestamp of match object")
	void testCreationTimestamp() {
		final Long beforeCreationTimestamp = System.currentTimeMillis();
		Match match = new Match(new Team("Mexico"), new Team("Canada"));
	    final Long afterCreationTimestamp = System.currentTimeMillis();
		
		assertTrue(beforeCreationTimestamp <= match.getCreationTimestamp(), "Match creation time is valid, compared to timestamp before creation");
		assertTrue(match.getCreationTimestamp() <= afterCreationTimestamp,  "Match creation time is valid, compared to timestamp after creation");
	}
}
