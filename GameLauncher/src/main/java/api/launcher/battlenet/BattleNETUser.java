package api.launcher.battlenet;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class BattleNETUser {
	
	public String name = "";
	public String email = "";
	public long lastLogIn = 0;
	
	public String getName() {
		if(name.isEmpty())
			return email;
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public long getLastLogIn() {
		return lastLogIn;
	}
	
	public void setLastLogIn(long lastLogIn) {
		this.lastLogIn = lastLogIn;
	}
}
