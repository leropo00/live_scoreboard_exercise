package com.sportradar.scoreboard;

public class Match {
	private Team homeTeam;
	private Team awayTeam;
	private Integer homeScore;
	private Integer awayScore;
	private Long creationTimestamp;
	
	public Match(Team homeTeam, Team awayTeam) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.homeScore = 0;
		this.awayScore = 0;
		this.creationTimestamp = System.currentTimeMillis(); 
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
		return creationTimestamp;
	}
}