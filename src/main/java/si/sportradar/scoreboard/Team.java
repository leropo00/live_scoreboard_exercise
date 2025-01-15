package si.sportradar.scoreboard;

import java.util.Objects;

/**  
 * Team class encapsulates the team info.
 * While based on current scoreboard requirements a String would be enough,
 * class was use instead, in case of future requirements changes
 */
public class Team {
	//  team name is considered to be unique identifier
	private String name;

	public Team(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
	    if (o == this) {
	        return true;
	    }
	    if (!(o instanceof Team)) {
	        return false;
	    }
	    Team other = (Team)o;
	    return Objects.equals(this.name, other.getName());
	}

	@Override
	public int hashCode() {
	    return Objects.hash(name);
	}

}
