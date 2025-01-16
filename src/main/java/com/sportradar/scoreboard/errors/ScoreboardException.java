package com.sportradar.scoreboard.errors;

public class ScoreboardException extends Exception {
	public static String ERROR_DUPLICATE_TEAM = "TEAM_ALREADY_PLAYING";
	public static String ERROR_INVALID_SCORE  = "ERROR_INVALID_SCORE";
	
	public ScoreboardException(String message) {
		super("Scoreboard exception: " + message);
	}
}

