package com.sportradar.scoreboard.errors;

public class ScoreboardException extends Exception {
	public static String ERROR_DUPLICATE_TEAM = "TEAM_ALREADY_PLAYING";
	public ScoreboardException(String message) {
		super("Scoreboard exception: " + message);
	}
}

