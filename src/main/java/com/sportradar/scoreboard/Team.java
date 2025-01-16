package com.sportradar.scoreboard;

/**  
 * This encapsulates the team info.
 * While based on current scoreboard requirements a String would be enough,
 * class was use instead, in case of future requirements changes.
 * Team name is considered to be unique identifier.
 */
public record Team (String name) {}
