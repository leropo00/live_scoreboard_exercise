package com.sportradar.scoreboard;

import java.util.ArrayList;
import java.util.List;

import com.sportradar.scoreboard.errors.ScoreboardException;

public class WorldCupScoreboard {
	private List<Match> scoreboard;
	
	public WorldCupScoreboard() {
		this.scoreboard = new ArrayList<>();
	}
	
	public Match startNewMatch(Team homeTeam, Team awayTeam) throws ScoreboardException {
		for (Match match : this.scoreboard) {
			if (match.getHomeTeam().equals(homeTeam) || match.getHomeTeam().equals(awayTeam) || 
				match.getAwayTeam().equals(homeTeam) || match.getAwayTeam().equals(awayTeam)) {
				throw new ScoreboardException(ScoreboardException.ERROR_DUPLICATE_TEAM);
			}
		}
		Match match = new Match(homeTeam, awayTeam);
		this.scoreboard.add(match);
		return match;
	}

	public Match updateScore(Match match, int homeScore, int awayScore) {
		return null;
	}
	
	public List<Match> getScoreboard()  {
		return this.scoreboard;
	}
}
