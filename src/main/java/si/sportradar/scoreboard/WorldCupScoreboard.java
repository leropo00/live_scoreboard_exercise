package si.sportradar.scoreboard;

import java.util.ArrayList;
import java.util.List;

public class WorldCupScoreboard {

	private List<Match> scoreboard;
	
	public WorldCupScoreboard() {
		
		this.scoreboard = new ArrayList();
	}

	public List<Match> getScoreboard()  {
		
		return this.scoreboard;
	}
	
}
