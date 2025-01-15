package si.sportradar.scoreboard;

import java.util.ArrayList;
import java.util.List;

public class WorldCupScoreboard {

	private List<Match> scoreboard;
	
	public WorldCupScoreboard() {
		this.scoreboard = new ArrayList<>();
	}
	
	public void startNewMatch(Team homeTeam, Team awayTeam)  {
		this.scoreboard.add(new Match(homeTeam, awayTeam));
	}

	public List<Match> getScoreboard()  {
		return this.scoreboard;
	}
	
}
