package com.sportradar.scoreboard;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sportradar.scoreboard.Team;

class TeamTest {
	private Team team;
	private static final String BRAZIL = "Brazil";

	@BeforeEach
	void init() {
		team = new Team(BRAZIL);
	}
	
	@Test
	@DisplayName("Tests construction of team object")
	void testInitialization() {
		assertEquals(BRAZIL, team.getName());
		assertEquals(team, new Team(BRAZIL));
	}

	@Test
	@DisplayName("Tests hash code function of team object")
	void testHashCodeOperation() {
		Set<Team> teams = new HashSet<>();
		teams.add(team);
		
		assertTrue(teams.contains(new Team(BRAZIL)));
		assertFalse(teams.contains(new Team("Portugal")));
	}
}
