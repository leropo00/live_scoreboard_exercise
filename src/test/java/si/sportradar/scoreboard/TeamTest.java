package si.sportradar.scoreboard;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TeamTest {
	private Team team;

	@BeforeEach
	void init() {
		team = new Team("Brazil");
	}
	
	@Test
	@DisplayName("Tests construction of team object")
	void testInitialization() {
		assertEquals(team.getName(), "Brazil");
		assertEquals(team, new Team("Brazil"));
	}

	@Test
	@DisplayName("Tests hash code function of team object")
	void testHashCodeOperation() {
		Set<Team> teams = new HashSet<>();
		teams.add(team);
		
		assertTrue(teams.contains(new Team("Brazil")));
		assertFalse(teams.contains(new Team("Portugal")));
	}
}
