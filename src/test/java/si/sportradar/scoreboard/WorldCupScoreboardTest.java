package si.sportradar.scoreboard;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WorldCupScoreboardTest {

	private WorldCupScoreboard scoreboard;
	
	@BeforeEach
	void init() {
		scoreboard = new WorldCupScoreboard();
	}
	
	@Test 
    @DisplayName("When scoreboard is initialized no matches are present")   
	void noMatchesAtStart()  {
		assertTrue(scoreboard.getScoreboard().isEmpty());
	}
	
}
