package si.sportradar.scoreboard;
/**  
 * Team class encapsulates the team info.
 * While based on current scoreboard requirements a String would be enough,
 * class was use instead, in case of future requirements changes
 */
public class Team {

	private String name;
	
	public Team(String name) {
		
	}
	
	public String getName() {
		return name;
	}
}
