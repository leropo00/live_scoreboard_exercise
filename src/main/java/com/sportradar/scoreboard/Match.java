package com.sportradar.scoreboard;

public class Match {
	private Team homeTeam;
	private Team awayTeam;
	private Integer homeScore;
	private Integer awayScore;
	private Long creationTimestamp;
	private ScoreLastChange lastIncremented;
	
	public Match(Team homeTeam, Team awayTeam) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.homeScore = 0;
		this.awayScore = 0;
		this.creationTimestamp = System.currentTimeMillis(); 
		this.lastIncremented = ScoreLastChange.NONE;
	}

	public Team getHomeTeam() {
		return this.homeTeam;
	}
	
	public Team getAwayTeam() {
		return this.awayTeam;
	}
	
	public Integer getHomeScore() {
		return this.homeScore;
	}

	public Integer getAwayScore() {
		return this.awayScore;
	}

	public Integer getTotalScore() {
		return this.homeScore + this.awayScore;
	}
	
	public Long getCreationTimestamp() {
		return this.creationTimestamp;
	}
	
	public ScoreLastChange getLastIncremented() {
		return this.lastIncremented;
	}
    
    public void incrementHomeScore() {
    	this.homeScore += 1;
		this.lastIncremented = ScoreLastChange.HOME_INCREMENTED;    	
    }
    
    public void decrementHomeScore() {
    	this.homeScore -= 1;
		this.lastIncremented = ScoreLastChange.HOME_DECREMENTED;
    }
    
    public void incrementAwayScore() {
    	this.awayScore += 1;
		this.lastIncremented = ScoreLastChange.AWAY_INCREMENTED;    	
    }
    
    public void decrementAwayScore() {
    	this.awayScore -= 1;
		this.lastIncremented = ScoreLastChange.AWAY_DECREMENTED;
    }
    
    public String toString() {
    	return String.format("%s %d - %s %d", this.homeTeam, this.getHomeScore(), this.awayTeam, this.awayScore);
    }
}
