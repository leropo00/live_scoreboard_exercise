package si.sportradar.scoreboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MatchTest {
	@Test
	@DisplayName("Tests initialization of match object")
	void testInitialization() {
		Team homeTeam = new Team("Mexico");
		Team awayTeam = new Team("Canada");
		
		Match match = new Match(homeTeam, awayTeam);
		
		assertSame(homeTeam, match.getHomeTeam());
		assertSame(awayTeam, match.getAwayTeam());
		assertEquals(0, match.getHomeScore());
		assertEquals(0, match.getAwayScore());
		assertEquals(0, match.getTotalScore());
	}
}
