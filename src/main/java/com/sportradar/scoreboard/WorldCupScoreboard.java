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
	
	public List<Match> getScoreboard()  {
		return this.scoreboard;
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
		Match matchInProgress = findIfMatchIsPresent(inputMatch);
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
		else if (homeScoreDifference == -1 && awayScoreDifference == 0 && matchInProgress.getLastIncremented() == ScoreLastChange.HOME_INCREMENTED) {
			matchInProgress.decrementHomeScore();
		}
		else if (homeScoreDifference == 0 && awayScoreDifference == -1 && matchInProgress.getLastIncremented() == ScoreLastChange.AWAY_INCREMENTED) {
			matchInProgress.decrementAwayScore();
		}
		else {
			throw new ScoreboardException(ScoreboardException.ERROR_INVALID_SCORE);
		}
		
		return matchInProgress;
	}

	public Match finishMatch(Match inputMatch) throws ScoreboardException {
		Match matchInProgress = findIfMatchIsPresent(inputMatch);
		this.scoreboard.remove(matchInProgress);
		return matchInProgress;
	}
	
	public List<Match> getMatchesSummary() {
		return new ArrayList<>();
	}
	
	private Match findIfMatchIsPresent(Match searchMatch) throws ScoreboardException {
		/* 	
			search for match is done by matching teams, 
		 	this works on current searchboard assumption
		 	that each team is present only once,
			since it is playing only one match at a time
		*/
		Optional<Match> matchFound  = scoreboard.stream().filter((m) -> m.getHomeTeam().equals(searchMatch.getHomeTeam()) &&
				 m.getAwayTeam().equals(searchMatch.getAwayTeam()) &&
				 m.getCreationTimestamp() == searchMatch.getCreationTimestamp()
				).findFirst();
		if (!matchFound.isPresent()) {
			throw new ScoreboardException(ScoreboardException.MISSING_TEAM);
		}
		return matchFound.get();
	}
}
