package com.sportradar.scoreboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.sportradar.scoreboard.errors.ScoreboardException;

public class WorldCupScoreboard {
	private List<Match> scoreboard;
	
	public WorldCupScoreboard() {
		this.scoreboard = new ArrayList<>();
	}
	
	public int size()  {
		return this.scoreboard.size();
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

	public void updateScore(Match match, int homeScore, int awayScore) throws ScoreboardException {
		if (!this.scoreboard.contains(match)) {
			throw new ScoreboardException(ScoreboardException.MISSING_TEAM);
		}
		if (homeScore < 0 || awayScore < 0) {
			throw new ScoreboardException(ScoreboardException.ERROR_INVALID_SCORE);
		}
		
		final int homeScoreDifference = homeScore - match.getHomeScore();
		final int awayScoreDifference = awayScore - match.getAwayScore();
		// if same score is sent no error occurs, but nothing changes in match
		if (homeScoreDifference == 0 && awayScoreDifference == 0) {
			return;
		}

		if (homeScoreDifference ==  1 && awayScoreDifference == 0) {
			match.incrementHomeScore();
		}
		else if (homeScoreDifference ==  0 && awayScoreDifference == 1) {
			match.incrementAwayScore();
		}	
		else if (homeScoreDifference == -1 && awayScoreDifference == 0 && match.getLastIncremented() == ScoreLastChange.HOME_INCREMENTED) {
			match.decrementHomeScore();
		}
		else if (homeScoreDifference == 0 && awayScoreDifference == -1 && match.getLastIncremented() == ScoreLastChange.AWAY_INCREMENTED) {
			match.decrementAwayScore();
		}
		else {
			throw new ScoreboardException(ScoreboardException.ERROR_INVALID_SCORE);
		}
	}

	public void finishMatch(Match match) throws ScoreboardException {
		if (!this.scoreboard.contains(match)) {
			throw new ScoreboardException(ScoreboardException.MISSING_TEAM);
		}
		this.scoreboard.remove(match);
	}
	
	public List<Match> getMatchesSummary() {
		List<Match> summary = new ArrayList<>();
		summary.addAll(this.scoreboard);
		summary.sort(Comparator.comparing(Match::getTotalScore).reversed()
    	      .thenComparing(Comparator.comparing(Match::getCreationTimestamp).reversed()));
		return summary;
	}
	
	public Match findMatch(Team homeTeam, Team awayTeams) throws ScoreboardException {
		/* 	
			search for match is done by matching teams, 
		 	this works on current scoreboard assumption
		 	that each team is present only once,
			since it is playing only one match at a time
		*/
		Optional<Match> matchFound  = this.scoreboard.stream().filter((m) -> m.getHomeTeam().equals(homeTeam) &&
				 m.getAwayTeam().equals(awayTeams))
				.findFirst();
		if (!matchFound.isPresent()) {
			throw new ScoreboardException(ScoreboardException.MISSING_TEAM);
		}
		return matchFound.get();
	}
}
