package com.sportradar.scoreboard;

public class Match {
	private Team homeTeam;
	private Team awayTeam;
	private Integer homeScore;
	private Integer awayScore;
	private Long creationTimestamp;
	private ScoreLastIncremented lastIncremented;
	
	public Match(Team homeTeam, Team awayTeam) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.homeScore = 0;
		this.awayScore = 0;
		this.creationTimestamp = System.currentTimeMillis(); 
		this.lastIncremented = ScoreLastIncremented.NONE;
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
	
	public ScoreLastIncremented getLastIncremented() {
		return this.lastIncremented;
	}
    
    public void incrementHomeScore() {
    	this.homeScore += 1;
		this.lastIncremented = ScoreLastIncremented.HOME;    	
    }
    
    public void decrementHomeScore() {
    	this.homeScore -= 1;
		this.lastIncremented = ScoreLastIncremented.NONE;
    }
    
    public void incrementAwayScore() {
    	this.awayScore += 1;
		this.lastIncremented = ScoreLastIncremented.AWAY;    	
    }
    
    public void decrementAwayScore() {
    	this.awayScore -= 1;
		this.lastIncremented = ScoreLastIncremented.NONE;
    }
}
