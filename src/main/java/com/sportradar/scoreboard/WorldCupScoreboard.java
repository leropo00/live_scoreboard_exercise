package com.sportradar.scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	public Match updateScore(Match inputMatch, int homeScore, int awayScore) throws ScoreboardException {
		// search for match is done by matching teams, this works based on assumption that only 
		Optional<Match> matchFound  = scoreboard.stream().filter((m) -> m.getHomeTeam().equals(inputMatch.getHomeTeam()) &&
				 m.getAwayTeam().equals(inputMatch.getAwayTeam()) &&
				 m.getCreationTimestamp() == inputMatch.getCreationTimestamp()
				).findFirst();
		if (!matchFound.isPresent()) {
			throw new ScoreboardException(ScoreboardException.MISSING_TEAM);
		}
		Match matchInProgress = matchFound.get();
		if (homeScore < 0 || awayScore < 0) {
			throw new ScoreboardException(ScoreboardException.ERROR_INVALID_SCORE);
		}
		
		final int homeScoreDifference = homeScore - matchInProgress.getHomeScore();
		final int awayScoreDifference = awayScore - matchInProgress.getAwayScore();

		if (homeScoreDifference ==  1 && awayScoreDifference == 0) {
			matchInProgress.incrementHomeScore();
		}
		else if (homeScoreDifference ==  0 && awayScoreDifference == 1) {
			matchInProgress.incrementAwayScore();
		}	
		else if (homeScoreDifference == -1 && awayScoreDifference == 0 && matchInProgress.getLastIncremented() == ScoreLastIncremented.HOME) {
			matchInProgress.decrementHomeScore();
		}
		else if (homeScoreDifference == 0 && awayScoreDifference == -1 && matchInProgress.getLastIncremented() == ScoreLastIncremented.AWAY) {
			matchInProgress.decrementAwayScore();
		}
		else {
			throw new ScoreboardException(ScoreboardException.ERROR_INVALID_SCORE);
		}
		
		return matchInProgress;
	}
	
	public List<Match> getScoreboard()  {
		return this.scoreboard;
	}
}
