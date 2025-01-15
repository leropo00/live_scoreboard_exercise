package si.sportradar.scoreboard;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.assertj.core.api.Assertions;

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
	
	@Test
	@DisplayName("After first match was started, scoreboard size is 1") 
	void startFirstMatch() {
		scoreboard.startNewMatch(new Team("Mexico"), new Team("Canada"));

		Assertions.assertThat(scoreboard.getScoreboard()).hasSize(1);
	}
	
	@Test
	@DisplayName("After two match were started, scoreboard size is 2") 
	void startTwoMatches() {
		scoreboard.startNewMatch(new Team("Mexico"), new Team("Canada"));
		scoreboard.startNewMatch(new Team("Spain"), new Team("Brazil"));

		Assertions.assertThat(scoreboard.getScoreboard()).hasSize(2);
	}
}
