package com.sportradar.scoreboard;

import java.util.ArrayList;
import java.util.List;

import com.sportradar.scoreboard.errors.ScoreboardException;

public class WorldCupScoreboard {
	private List<Match> scoreboard;
	
	public WorldCupScoreboard() {
		this.scoreboard = new ArrayList<>();
	}
	
	public void startNewMatch(Team homeTeam, Team awayTeam) throws ScoreboardException {
		for (Match match : this.scoreboard) {
			if (match.getHomeTeam().equals(homeTeam) || match.getHomeTeam().equals(awayTeam) || 
				match.getAwayTeam().equals(homeTeam) || match.getAwayTeam().equals(awayTeam)) {
				throw new ScoreboardException(ScoreboardException.ERROR_DUPLICATE_TEAM);
			}
		}
		this.scoreboard.add(new Match(homeTeam, awayTeam));
	}

	public List<Match> getScoreboard()  {
		return this.scoreboard;
	}
}
